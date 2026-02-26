package com.atek.scs.feature.domain.station_summary

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.atek.scs.feature.common.database.entity.EquipmentConfig
import com.atek.scs.feature.common.database.service.EquipmentConfigService
import com.atek.scs.feature.common.theme.PreviewWithTheme
import com.atek.scs.feature.common.theme.components.EquipmentOptionDialog
import com.atek.scs.feature.common.theme.imageResource
import com.atek.scs.feature.domain.authentication.AuthService
import com.atek.scs.feature.domain.authentication.LoginScreen
import com.atek.scs.feature.domain.special_fare_modes.SpecialFareModesScreen
import com.atek.scs.feature.domain.station_reporting.StationReportingScreen
import com.atek.scs.feature.domain.station_summary.StationSummaryScreen.View
import com.atek.scs.service.EquipmentCommService
import com.atek.scs.utils.EquipmentType
import com.atek.scs.utils.GateMode
import com.atek.scs.utils.MediaType
import com.atek.scs.utils.ServiceMode
import database.GetSummaryByMediaType
import kotlin.math.roundToInt

object StationSummaryScreen : Screen {

 fun readResolve(): Any = StationSummaryScreen

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = rememberScreenModel {
            StationSummaryViewModel(
                equipmentConfigService = EquipmentConfigService,
                equipmentCommService = EquipmentCommService,
                authService = AuthService
            )
        }
        val uiState by viewModel.uiState.collectAsState()
        View(
            uiState = uiState,
            onSetCoordinates = viewModel::setCoordinates,
            onChangeMode = viewModel::changeMode,
            onUpdateStationEmergency = viewModel::updateStationEmergency,
            onUpdateEquipmentServiceMode = viewModel::updateEquipmentServiceMode,
            onSaveLayout = viewModel::saveLayout,
            onLogout = viewModel::logout,
            onDialogDismissed = viewModel::onDialogDismissed,
            onNavigateToLogin = { navigator.replaceAll(LoginScreen) },
            onNavigateToSpecialModes = { navigator.push(SpecialFareModesScreen) },
            onNavigateToEOSReporting = { navigator.push(StationReportingScreen) },

        )
    }

    @Composable
    fun View(
        uiState: StationSummaryUiState,
        onSetCoordinates: (EquipmentConfig, Float, Float) -> Unit,
        onChangeMode: (EquipmentConfig, GateMode) -> Unit,
        onUpdateStationEmergency: (Boolean) -> Unit,
        onUpdateEquipmentServiceMode: (String, ServiceMode) -> Unit,
        onSaveLayout: () -> Unit,
        onLogout: () -> Unit,
        onDialogDismissed: () -> Unit,
        onNavigateToLogin: () -> Unit,
        onNavigateToSpecialModes: () -> Unit,
        onNavigateToEOSReporting: () -> Unit = {},

        ) {
        uiState.dialogMessage?.let { message ->
            AlertDialog(
                onDismissRequest = onDialogDismissed,
                title = { Text("Information") },
                text = { Text(message) },
                confirmButton = { Button(onClick = onDialogDismissed) { Text("OK") } }
            )
        }

        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize().padding(10.dp)) {
                Row(modifier = Modifier.fillMaxHeight(0.82f)) {
                    Column(modifier = Modifier.fillMaxHeight().fillMaxWidth(0.8f)) {
                        EquipmentContainer(uiState, onSetCoordinates, onChangeMode, onUpdateEquipmentServiceMode)
                    }
                    Column(modifier = Modifier.fillMaxSize().padding(start = 10.dp)) {
                        CommandContainer(uiState, onUpdateStationEmergency, onSaveLayout, onLogout, onNavigateToLogin, onNavigateToSpecialModes, onNavigateToEOSReporting)
                    }
                }
                Row(modifier = Modifier.fillMaxWidth().padding(top = 6.dp).background(color = Color(0Xffdfefee)).clip(RoundedCornerShape(8.dp))) {
                    BottomBar(uiState.analytics)
                }
            }

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }

    @Composable
    private fun EquipmentContainer(
        uiState: StationSummaryUiState,
        onSetCoordinates: (EquipmentConfig, Float, Float) -> Unit,
        onChangeMode: (EquipmentConfig, GateMode) -> Unit,
        onUpdateEquipmentServiceMode: (String, ServiceMode) -> Unit
    ) {
        Card(modifier = Modifier.fillMaxSize(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
            Box(modifier = Modifier.fillMaxSize().background(color = Color(0xffdfefee)),) {
                for (equipment in uiState.equipments) {
                    Equipment(equipment, uiState.isLoggedIn, onSetCoordinates, onChangeMode, onUpdateEquipmentServiceMode)
                }
            }
        }
    }

    @Composable
    private fun Equipment(
        equipment: EquipmentConfig,
        isLoggedIn: Boolean,
        onSetCoordinates: (EquipmentConfig, Float, Float) -> Unit,
        onChangeMode: (EquipmentConfig, GateMode) -> Unit,
        onUpdateEquipmentServiceMode: (String, ServiceMode) -> Unit
    ) {
        var offsetX by remember(equipment.cordX) { mutableStateOf(equipment.cordX.toFloat()) }
        var offsetY by remember(equipment.cordY) { mutableStateOf(equipment.cordY.toFloat()) }
        var showDialog by remember { mutableStateOf(false) }

        val imageName = when (equipment.eqTypeId) {

            EquipmentType.AG.id -> {
                when (equipment.eqModeId) {

                    GateMode.ENTRY.id -> "entry_ag"

                    GateMode.EXIT.id -> "exit_ag"

                    GateMode.BI_DI.id -> {
                        when (equipment.currentModeId) {
                            GateMode.ENTRY.id -> "entry_ag"
                            GateMode.EXIT.id -> "exit_ag"
                            else -> "bidi_to_bidi"
                        }
                    }

                    else -> "mono_tom1"
                }
            }

            else -> "mono_tom"
        }

        Box(
            modifier = Modifier
                .size(80.dp)
                .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                .pointerInput(Unit) {
                    detectDragGesturesAfterLongPress { change, dragAmount ->
                        if (!isLoggedIn) return@detectDragGesturesAfterLongPress
                        change.consume()
                        offsetX = (offsetX + dragAmount.x).coerceIn(0F, 1500F)
                        offsetY = (offsetY + dragAmount.y).coerceIn(0F, 665F)
                        onSetCoordinates(equipment, offsetX, offsetY)
                    }
                }
                .clickable { showDialog = true },
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Image(
                    modifier = Modifier.fillMaxWidth().weight(2f),
                    bitmap = imageResource(/*if (equipment.eqTypeId == EquipmentType.AG.id) "gates" else "tom"*/
                        imageName),
                    contentDescription = "Gate ${equipment.getName()}",
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = equipment.getName(),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

            if (showDialog) {
                EquipmentOptionDialog(
                    equipment = equipment,
                    onChangeMode = { mode -> onChangeMode(equipment, mode) },
                    onEnableEmergency = { onUpdateEquipmentServiceMode(equipment.eqId, ServiceMode.EMERGENCY_START) },
                    onMakeInService = { onUpdateEquipmentServiceMode(equipment.eqId, ServiceMode.EMERGENCY_STOP) },
                    onDismiss = { showDialog = false },
                    isLoggedIn = isLoggedIn
                )
            }
        }
    }

    @Composable
    fun CommandContainer(
        uiState: StationSummaryUiState,
        onUpdateStationEmergency: (Boolean) -> Unit,
        onSaveLayout: () -> Unit,
        onLogout: () -> Unit,
        onNavigateToLogin: () -> Unit,
        onNavigateToSpecialModes: () -> Unit,
        onNavigateToEOSReporting: () -> Unit,
    ) {
        var showDialog by remember { mutableStateOf(false) }
        var dialogTitle by remember { mutableStateOf("") }
        var isEmergency by remember { mutableStateOf(false) }

        if (showDialog) {
            ConfirmDialog(
                title = dialogTitle,
                onConfirm = {
                    onUpdateStationEmergency(isEmergency)
                    showDialog = false
                },
                onDismiss = { showDialog = false }
            )
        }

        Card(modifier = Modifier.fillMaxSize(), colors = CardDefaults.cardColors(containerColor = Color(0xffdfefee)))
                {
            Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                Text("EMERGENCY OPTIONS", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        isEmergency = true
                        dialogTitle = "Are you sure you want to put station in emergency?"
                        showDialog = true
                    },
                    shape = ShapeDefaults.Medium,
                    enabled = uiState.isLoggedIn
                ) {
                    Text("EMERGENCY")
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        isEmergency = false
                        dialogTitle = "Are you sure you want to restore station from emergency?"
                        showDialog = true
                    },
                    shape = ShapeDefaults.Medium,
                    enabled = uiState.isLoggedIn
                ) {
                    Text("RESTORE")
                }

                HorizontalDivider(Modifier.fillMaxWidth().padding(vertical = 10.dp))
                Text("USER OPTIONS", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { if (uiState.isLoggedIn) onLogout() else onNavigateToLogin() },
                    shape = ShapeDefaults.Medium,
                ) {
                    Text(if (uiState.isLoggedIn) "LOGOUT" else "LOGIN")
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    shape = ShapeDefaults.Medium,
                    onClick = onSaveLayout,
                    enabled = uiState.isLoggedIn
                ) {
                    Text("SAVE LAYOUT")
                }

                HorizontalDivider(Modifier.fillMaxWidth().padding(vertical = 10.dp))
                Text("OTHERS", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    shape = ShapeDefaults.Medium,
                    onClick = onNavigateToSpecialModes,
                    enabled = uiState.isLoggedIn
                ) {
                    Text("SPECIAL COMMAND")
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    shape = ShapeDefaults.Medium,
                    onClick = onNavigateToEOSReporting,
                    enabled = uiState.isLoggedIn
                ) {
                    Text("STATION REPORT")
                }
            }
                  Column(modifier = Modifier.weight(1f)) { }
        }
    }

    @Composable
    fun ConfirmDialog(title: String, onConfirm: () -> Unit, onDismiss: () -> Unit) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(text = title) },
            confirmButton = { TextButton(onClick = onConfirm) { Text("Confirm") } },
            dismissButton = { TextButton(onClick = onDismiss) { Text("Dismiss") } }
        )
    }

    @Composable
    fun BottomBar(analytics: Map<MediaType, GetSummaryByMediaType?>) {
        // The implementation of this Composable can remain largely the same,
        // as it was already receiving the data it needed as a parameter.
        val mediaTypes = listOf(MediaType.PQR, MediaType.MQR, MediaType.OL, MediaType.CL)
        Column(modifier = Modifier.fillMaxSize().clip(MaterialTheme.shapes.medium).background(color = Color(0xffdfefee)), verticalArrangement = Arrangement.Center) {
            Row(modifier = Modifier.fillMaxWidth().padding(2.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                mediaTypes.forEach { mediaType ->
                    OutlinedCard(
                        modifier = Modifier.height(100.dp).width(250.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = MaterialTheme.shapes.small,
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Row(Modifier.fillMaxWidth().background(color = Color(0xffdfefee)) ) {
                            Column(
                                modifier = Modifier.weight(.5f).fillMaxHeight().background(MaterialTheme.colorScheme.primary),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = mediaType.name,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                            Column(
                                modifier = Modifier.weight(1f).fillMaxSize().padding(8.dp).background(color = Color(0xffdfefee)),
                                verticalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("ENTRY : ", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                                    Text(
                                        text = (analytics[mediaType]?.totalEntryCount ?: 0).toString(),
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("EXIT : ", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                                    Text(
                                        text = (analytics[mediaType]?.totalExitCount ?: 0).toString(),
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
                Image(
                    modifier = Modifier
                        .size(100.dp),
                    // Fixed: use "atek_logo" which exists in resources
                    bitmap = imageResource("atek_logo"),
                    contentDescription = "MMMOCL Logo",
                )
            }
        }
    }
}

@Preview(device = "spec:width=3620px,height=1980px,dpi=440")
@Composable
private fun Preview() {
    PreviewWithTheme {
        View(
            uiState = StationSummaryUiState(isLoggedIn = true),
            onSetCoordinates = { _, _, _ -> },
            onChangeMode = { _, _ -> },
            onUpdateStationEmergency = {},
            onUpdateEquipmentServiceMode = { _, _ -> },
            onSaveLayout = {},
            onLogout = {},
            onDialogDismissed = {},
            onNavigateToLogin = {},
            onNavigateToSpecialModes = {}
        ) {}
    }
}