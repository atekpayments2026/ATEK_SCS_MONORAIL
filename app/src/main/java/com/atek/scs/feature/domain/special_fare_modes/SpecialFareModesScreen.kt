package com.atek.scs.feature.domain.special_fare_modes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.core.lifecycle.LifecycleEffectOnce
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.atek.scs.feature.common.database.entity.EquipmentConfig
import com.atek.scs.feature.common.database.service.EquipmentConfigService
import com.atek.scs.feature.common.theme.components.FullScreenLoader
import com.atek.scs.service.EquipmentCommService

object SpecialFareModesScreen : Screen {

    private fun readResolve(): Any = SpecialFareModesScreen

    @OptIn(ExperimentalVoyagerApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val viewModel = rememberScreenModel {
            SpecialFareModesViewModel(
                equipmentConfigService = EquipmentConfigService,
                equipmentCommService = EquipmentCommService
            )
        }
        LifecycleEffectOnce {
            viewModel.onCreate()
        }
        val uiState by viewModel.uiState.collectAsState()
        View(
            uiState = uiState,
            onToggleAllModes = viewModel::onToggleAllModes,
            onSpecialModeSelected = viewModel::onSpecialModeSelected,
            onToggleAllEquipments = viewModel::onToggleAllEquipments,
            onEquipmentSelected = { equipment, isChecked -> viewModel.onEquipmentSelected(equipment, isChecked) },
            onSendCommand = viewModel::sendCommand,
            onClear = viewModel::clearAllToggles,
            onBack = { navigator.pop() },
            onDialogDismissed = viewModel::onDialogDismissed
        )
    }

    @Composable
    private fun View(
        uiState: SpecialFareModesUiState,
        onToggleAllModes: (Boolean) -> Unit,
        onSpecialModeSelected: (String, Boolean) -> Unit,
        onToggleAllEquipments: (Boolean) -> Unit,
        onEquipmentSelected: (EquipmentConfig, Boolean) -> Unit,
        onSendCommand: () -> Unit,
        onClear: () -> Unit,
        onBack: () -> Unit,
        onDialogDismissed: () -> Unit,
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Card(
                modifier = Modifier.fillMaxSize().padding(8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                // Main content area for modes and equipment
                Row(modifier = Modifier.weight(1f)) {
                    ModesPanel(uiState, onToggleAllModes, onSpecialModeSelected)
                    EquipmentPanel(uiState, onToggleAllEquipments, onEquipmentSelected)
                }
                // Bottom action bar
                ActionBar(
                    uiState = uiState,
                    onSendCommand = onSendCommand,
                    onClear = onClear,
                    onBack = onBack
                )
            }

            // Full-screen loader overlay
            if (uiState.isLoading) {
                FullScreenLoader()
            }

            // Result/Error Dialog
            uiState.dialogMessage?.let { message ->
                AlertDialog(
                    onDismissRequest = onDialogDismissed,
                    title = { Text("Command Result") },
                    text = { Text(message) },
                    confirmButton = { Button(onClick = onDialogDismissed) { Text("OK") } }
                )
            }
        }
    }

    @Composable
    private fun RowScope.ModesPanel(
        uiState: SpecialFareModesUiState,
        onToggleAllModes: (Boolean) -> Unit,
        onSpecialModeSelected: (String, Boolean) -> Unit
    ) {
        Column(
            Modifier.weight(1f).fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("SPECIAL MODES", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(8.dp))
            HorizontalDivider(thickness = 1.5.dp, color = MaterialTheme.colorScheme.primary)
            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SelectableRow(
                    text = "SELECT ALL",
                    isChecked = uiState.selectAllModes,
                    onCheckedChange = onToggleAllModes,
                    isBold = true
                )
                // Use the helper map from the updated SpecialFareModes model
                uiState.specialFareModes.modesAsMap.forEach { (modeName, isChecked) ->
                    SelectableRow(
                        text = modeName,
                        isChecked = isChecked,
                        onCheckedChange = { newCheckedState -> onSpecialModeSelected(modeName, newCheckedState) }
                    )
                }
            }
        }
    }

    @Composable
    private fun RowScope.EquipmentPanel(
        uiState: SpecialFareModesUiState,
        onToggleAllEquipments: (Boolean) -> Unit,
        onEquipmentSelected: (EquipmentConfig, Boolean) -> Unit
    ) {
        Column(
            Modifier.weight(2.8f).fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("EQUIPMENTS", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(8.dp))
            HorizontalDivider(thickness = 1.5.dp, color = MaterialTheme.colorScheme.primary)
            SelectableRow(
                text = "SELECT ALL",
                isChecked = uiState.selectAllEquipments,
                onCheckedChange = onToggleAllEquipments,
                isBold = true
            )
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 150.dp),
                modifier = Modifier.fillMaxSize().padding(4.dp),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(uiState.equipments, key = { it.first.eqId }) { (equipment, isChecked) ->
                    SelectableRow(
                        text = equipment.getName(),
                        isChecked = isChecked,
                        onCheckedChange = { newCheckedState -> onEquipmentSelected(equipment, newCheckedState) }
                    )
                }
            }
        }
    }

    @Composable
    private fun ActionBar(
        uiState: SpecialFareModesUiState,
        onSendCommand: () -> Unit,
        onClear: () -> Unit,
        onBack: () -> Unit
    ) {
        Column {
            HorizontalDivider(thickness = 1.5.dp, color = MaterialTheme.colorScheme.primary)
            Row(
                Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val buttonModifier = Modifier.weight(1f).padding(horizontal = 16.dp)
                val buttonTextStyle = MaterialTheme.typography.titleMedium

                Button(onClick = onBack, modifier = buttonModifier, shape = MaterialTheme.shapes.medium) {
                    Text("BACK", style = buttonTextStyle)
                }
                Button(onClick = onClear, modifier = buttonModifier, shape = MaterialTheme.shapes.medium) {
                    Text("CLEAR", style = buttonTextStyle)
                }
                Button(
                    onClick = onSendCommand,
                    modifier = buttonModifier,
                    shape = MaterialTheme.shapes.medium,
                    enabled = uiState.equipments.any { it.second }
                ) {
                    Text("SEND", style = buttonTextStyle)
                }
            }
        }
    }

    @Composable
    private fun SelectableRow(
        text: String,
        isChecked: Boolean,
        onCheckedChange: (Boolean) -> Unit,
        isBold: Boolean = false
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = isChecked, onCheckedChange = onCheckedChange)
            Text(text, fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal)
        }
    }
}


@Preview(device = "spec:parent=pixel_5,orientation=landscape")
@Composable
private fun Preview() {
    SpecialFareModesScreen.Content()
}