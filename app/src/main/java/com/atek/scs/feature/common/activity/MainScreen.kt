package com.atek.scs.feature.common.activity

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberNavigatorScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.atek.scs.BuildConfig
import com.atek.scs.feature.common.theme.AtekScsTheme
import com.atek.scs.feature.common.theme.components.AlertDialog
import com.atek.scs.feature.common.theme.components.FullScreenLoader
import com.atek.scs.feature.common.theme.imageResource
import com.atek.scs.feature.common.database.entity.Config
import com.atek.scs.feature.domain.configuration.ConfigurationScreen
import com.atek.scs.service.MaintenanceService
import com.atek.scs.utils.date
import com.atek.scs.utils.getIPAddress
import kotlinx.coroutines.delay

object MainScreen : Screen {

    private fun readResolve(): Any = MainScreen

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = navigator.rememberNavigatorScreenModel { MainViewModel() }
        LaunchedEffect(Unit) {
            viewModel.onCreate()
        }
        View(viewModel)
    }

    @Composable
    fun View(viewModel: MainViewModel) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primary),
            ) {
                TopStatusBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Black),
                    config = viewModel.config,
                    ccsStatus = viewModel.ccsStatus,
                )
                TopBar(viewModel)
                // Use weight to ensure Navigator takes remaining space
                Box(modifier = Modifier.weight(1f)) {
                    Navigator(ConfigurationScreen)
                }
            }

            // SHOW ERROR MESSAGE
            if (AlertDialog.showDialog) {
                AlertDialog()
            }

            if (viewModel.isLoading.value) {
                FullScreenLoader()
            }
        }
    }

    @Composable
    private fun TopStatusBar(
        modifier: Modifier,
        config: Config?,
        ccsStatus: Boolean
    ) {
        Row(
            modifier = modifier.padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "VERSION: ${BuildConfig.VERSION_NAME}   |   EQUIPMENT: ${config?.eqId ?: "N/A"}   |   IP: ${
                    getIPAddress(
                        true
                    )
                }   |   USER: ${config?.loginUser ?: "N/A"}",
                color = Color.White,
                style = MaterialTheme.typography.bodySmall
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier.size(20.dp),
                    bitmap = imageResource(
                        if (ccsStatus) "ccs_status_on"
                        else "ccs_status_off"
                    ),
                    contentDescription = null
                )
            }
        }
    }

    @Composable
    private fun TopBar(
        viewModel: MainViewModel
    ) {

        var dateTime by remember { mutableStateOf("03-02-1997 12:30 PM") }
        // Fixed: changed 'key' to 'Unit'
        LaunchedEffect(Unit) {
            while (true) {
                dateTime = date()
                delay(1000)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp, start = 10.dp, end = 10.dp)
                .clip(MaterialTheme.shapes.medium)
                .height(90.dp)
                .background(MaterialTheme.colorScheme.surface),
        ) {

            // MONO LOGO
            Image(
                modifier = Modifier
                    .weight(1.5f)
                    .fillMaxHeight()
                    .padding(8.dp),
                bitmap = imageResource("monorail_logo"),
                contentDescription = "MMOPL Logo",
                contentScale = ContentScale.Fit
            )

            // STATION NAME / DATE TIME
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(6f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
//                        .padding(start = 150.dp),
                    text = viewModel.config?.stnName?.uppercase() ?: "N/A",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = Modifier.weight(1f),
                    text = dateTime,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            }

            Column(
                modifier = Modifier
                    .weight(1.5f)
                    .fillMaxHeight()
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    modifier = Modifier
                        .weight(2f)
                        .fillMaxWidth()
                        .padding(8.dp),
                    bitmap = imageResource("mmmocl_logo"),
                    contentDescription = "MMMOCL Logo",
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.size(16.dp))

            if (viewModel.isFirmwareUpdateAvailable) {
                ElevatedCard(shape = MaterialTheme.shapes.small, onClick = {
                    MaintenanceService.getInstance().implFirmwareUpdate()
                }) {
                    Image(
                        modifier = Modifier.padding(16.dp),
                        bitmap = imageResource("install_update"),
                        contentDescription = "Firmware Update",
                    )
                }

                Spacer(modifier = Modifier.size(16.dp))
            }

        }
    }
}

@Composable
@Preview(device = "spec:parent=pixel_5,orientation=landscape")
private fun Preview() {
    AtekScsTheme {
        MainScreen.View(MainViewModel())
    }
}