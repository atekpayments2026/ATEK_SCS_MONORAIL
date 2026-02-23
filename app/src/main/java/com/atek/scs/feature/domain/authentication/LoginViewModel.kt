package com.atek.scs.feature.domain.authentication

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import cafe.adriel.voyager.core.model.ScreenModel
import com.atek.scs.feature.common.database.service.ConfigService
import com.atek.scs.feature.common.database.service.UserConfigService
import com.atek.scs.feature.common.theme.withLoading
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginViewModel(
    val navigateToOperations: () -> Unit
) : ScreenModel {

    var isLoading = mutableStateOf(true)
    var username by mutableStateOf(TextFieldValue())
    var password by mutableStateOf(TextFieldValue())

    fun onCreated() = withLoading(isLoading) {
        val config = ConfigService.getConfig()
            ?: throw Exception("Configuration not found")
        if (config.loginUser != null) {
            withContext(Dispatchers.Main) {
                navigateToOperations()
            }
        }
    }

    fun onLogin() = withLoading(isLoading) {
        val username = username.text
        val password = password.text

        if (username.isEmpty())
            throw Exception("Username is required")

        if (password.isEmpty())
            throw Exception("Password is required")

        val user = UserConfigService.findByUsername(username)
            ?: throw Exception("User not found with username: $username")

        if (user.password != password)
            throw Exception("Invalid password")

        val config = ConfigService.getConfig()
            ?: throw Exception("Configuration not found")

        // PROCESS LOGIN
        AuthService.login(username)

        // NAVIGATE TO OPERATIONS
        withContext(Dispatchers.Main) {
            navigateToOperations()
        }

    }

}