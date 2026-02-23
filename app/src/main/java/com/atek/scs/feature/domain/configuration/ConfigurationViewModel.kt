package com.atek.scs.feature.domain.configuration

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.atek.scs.feature.common.database.service.CommonDatabaseService
import com.atek.scs.feature.common.database.service.ConfigService
import com.atek.scs.feature.domain.configuration.model.ConfigRequest
import com.atek.scs.utils.getIPAddress
import kotlinx.coroutines.*
import org.tinylog.Logger

class ConfigurationViewModel(
    val navigateToLogin: () -> Unit
) : ScreenModel {

    var message by mutableStateOf("Configuration device, please wait...")

    fun onCreated() = screenModelScope.launch(Dispatchers.IO) {

        val config = ConfigService.getConfig()

        // CHECK IF ALREADY CONFIGURED
        if (config != null) {
            message = "Device already configured"
            withContext(Dispatchers.Main) {
                navigateToLogin()
            }
            return@launch
        }

        // GET CONFIG FROM SERVER
        while (isActive) {
            try {
                fetchConfig()
                break
            } catch (e: Exception) {
                showError(e)
            }
            delay(1000)
        }

        // NAVIGATE TO LOGIN
        withContext(Dispatchers.Main) {
            navigateToLogin()
        }

    }

    private suspend fun fetchConfig() {

        val response = ConfigRepository.getConfig(ConfigRequest(getIPAddress()))
        if (!response.status)
            throw Exception(response.error ?: "Failed to get config from server")

        val configData = response.data
            ?: throw Exception("Response data is null")

        if (configData.equipments == null ||
            configData.users == null
        ) throw Exception("Config data is null")

        // SAVE RESPONSE
        CommonDatabaseService.saveAllConfig(
            config = configData.config,
            equipments = configData.equipments,
            users = configData.users,
        )
    }

    private suspend fun showError(
        exception: Exception
    ) = withContext(Dispatchers.Main) {
        Logger.error(exception)
        message = exception.message ?: "An error occurred"
    }

}