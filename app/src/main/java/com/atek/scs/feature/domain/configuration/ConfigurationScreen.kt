package com.atek.scs.feature.domain.configuration

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.core.lifecycle.LifecycleEffectOnce
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.atek.scs.feature.common.theme.AtekScsTheme
import com.atek.scs.feature.common.theme.imageResource
import com.atek.scs.feature.domain.station_summary.StationSummaryScreen

object ConfigurationScreen : Screen {

    private fun readResolve(): Any = ConfigurationScreen

    @OptIn(ExperimentalVoyagerApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = rememberScreenModel {
            ConfigurationViewModel {
                navigator.replace(StationSummaryScreen)
            }
        }
        LifecycleEffectOnce { viewModel.onCreated() }
        View(viewModel)
    }

    @Composable
    fun View(viewModel: ConfigurationViewModel) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            ElevatedCard(
                modifier = Modifier.align(Alignment.Center).background(MaterialTheme.colorScheme.primary)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        modifier = Modifier.size(100.dp),
                        bitmap = imageResource("config_logo"),
                        contentDescription = "Configuration Logo"
                    )
                    CircularProgressIndicator(
                        modifier = Modifier.padding(32.dp)
                    )
                    Text(text = viewModel.message)
                }
            }
        }
    }

}

@Preview
@Composable
private fun Preview() {
    AtekScsTheme {
        ConfigurationScreen.View(
            ConfigurationViewModel {
                // Do nothing
            }
        )
    }
}