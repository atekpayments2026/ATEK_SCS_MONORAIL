package com.atek.scs.feature.domain.authentication

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.core.lifecycle.LifecycleEffectOnce
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.atek.scs.feature.common.theme.AtekScsTheme
import com.atek.scs.feature.domain.station_summary.StationSummaryScreen

object LoginScreen : Screen {

    private fun readResolve(): Any = LoginScreen

    @OptIn(ExperimentalVoyagerApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = rememberScreenModel {
            LoginViewModel {
                navigator.replace(StationSummaryScreen)
            }
        }
        LifecycleEffectOnce { viewModel.onCreated() }
        View(viewModel)
    }

    @Composable
    fun View(viewModel: LoginViewModel) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            ElevatedCard(
                modifier = Modifier.align(Alignment.Center)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.3f)
                        .padding(32.dp)
                ) {
                    Text(
                        text = "LOGIN",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(Modifier.height(16.dp))
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = viewModel.username,
                        shape = MaterialTheme.shapes.medium,
                        singleLine = true,
                        enabled = !viewModel.isLoading.value,
                        onValueChange = {
                            viewModel.username = it
                        },
                        label = {
                            Text("Username")
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        )
                    )
                    Spacer(Modifier.height(16.dp))
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = viewModel.password,
                        shape = MaterialTheme.shapes.medium,
                        singleLine = true,
                        enabled = !viewModel.isLoading.value,
                        onValueChange = {
                            viewModel.password = it
                        },
                        label = {
                            Text("Password")
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                viewModel.onLogin()
                            }
                        )
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            viewModel.onLogin()
                        },
                        enabled = !viewModel.isLoading.value,
                        shape = MaterialTheme.shapes.medium
                    ) {
                        if (viewModel.isLoading.value) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .size(24.dp),
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        } else {
                            Text(
                                modifier = Modifier.padding(16.dp),
                                text = "CONTINUE",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }
    }
}


@Preview
@Composable
private fun Preview() {
    AtekScsTheme {
        LoginScreen.View(
            LoginViewModel {

            }
        )
    }
}