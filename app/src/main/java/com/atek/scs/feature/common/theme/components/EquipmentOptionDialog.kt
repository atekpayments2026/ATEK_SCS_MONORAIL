package com.atek.scs.feature.common.theme.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.atek.scs.feature.common.database.entity.EquipmentConfig
import com.atek.scs.feature.common.theme.imageResource
import com.atek.scs.utils.EquipmentType
import com.atek.scs.utils.GateMode

/**
 * A stateless dialog for displaying equipment options.
 *
 * This dialog is decoupled from the ViewModel and follows unidirectional data flow principles.
 * Data (`equipment`, `isLoggedIn`) flows in, and events (`onDismiss`, `onChangeMode`, etc.) flow out.
 *
 * @param equipment The configuration of the equipment to display.
 * @param isLoggedIn A boolean indicating if a user is logged in, used to control button enabled states.
 * @param onDismiss Lambda to be invoked when the dialog is dismissed.
 * @param onChangeMode Lambda to be invoked when a user selects a new gate mode.
 * @param onEnableEmergency Lambda to be invoked when the emergency button is clicked.
 * @param onMakeInService Lambda to be invoked when the restore/in-service button is clicked.
 */
@Composable
fun EquipmentOptionDialog(
    equipment: EquipmentConfig,
    isLoggedIn: Boolean,
    onDismiss: () -> Unit,
    onChangeMode: (GateMode) -> Unit,
    onEnableEmergency: () -> Unit,
    onMakeInService: () -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "EQUIPMENT OPERATIONS: ${equipment.getName()}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

                EquipmentDetails(equipment = equipment)

                if (equipment.eqTypeId == EquipmentType.AG.id) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

                    // Pass events up to the caller. The dialog itself doesn't know what to do with them.
                    EquipmentModeOptions(
                        isEnabled = isLoggedIn,
                        onChangeMode = { mode ->
                            onChangeMode(mode)
                            onDismiss() // Dismiss dialog after action
                        }
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

                    EquipmentEmergencyOptions(
                        isEnabled = isLoggedIn,
                        onEnableEmergency = {
                            onEnableEmergency()
                            onDismiss() // Dismiss dialog after action
                        },
                        onMakeInService = {
                            onMakeInService()
                            onDismiss() // Dismiss dialog after action
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun EquipmentEmergencyOptions(
    isEnabled: Boolean,
    onEnableEmergency: () -> Unit,
    onMakeInService: () -> Unit,
) {
    Text("EMERGENCY OPTIONS", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
    Row(
        modifier = Modifier.wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
            onClick = onEnableEmergency,
            shape = ShapeDefaults.Medium,
            enabled = isEnabled
        ) {
            Text(text = "EMERGENCY", style = MaterialTheme.typography.bodyLarge)
        }
        Button(
            modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
            onClick = onMakeInService,
            shape = ShapeDefaults.Medium,
            enabled = isEnabled
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "RESTORE", style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.width(8.dp))
                Image(
                    modifier = Modifier.size(20.dp),
                    bitmap = imageResource("emergency_restore"),
                    contentDescription = "Restore Emergency Icon"
                )
            }
        }
    }
}

@Composable
private fun EquipmentModeOptions(
    isEnabled: Boolean,
    onChangeMode: (GateMode) -> Unit,
) {
    Text("CHANGE GATE MODE", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
    Row(
        modifier = Modifier.wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        val buttonModifier = Modifier.weight(1f).padding(horizontal = 4.dp)

        Button(
            modifier = buttonModifier,
            onClick = { onChangeMode(GateMode.BI_DI) },
            shape = ShapeDefaults.Medium,
            enabled = isEnabled
        ) {
            Text("BI-DI", style = MaterialTheme.typography.bodyLarge)
        }
        Button(
            modifier = buttonModifier,
            onClick = { onChangeMode(GateMode.ENTRY) },
            shape = ShapeDefaults.Medium,
            enabled = isEnabled
        ) {
            Text("ENTRY", style = MaterialTheme.typography.bodyLarge)
        }
        Button(
            modifier = buttonModifier,
            onClick = { onChangeMode(GateMode.EXIT) },
            shape = ShapeDefaults.Medium,
            enabled = isEnabled
        ) {
            Text("EXIT", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
private fun EquipmentDetails(
    modifier: Modifier = Modifier,
    equipment: EquipmentConfig,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            modifier = Modifier.weight(1f).size(100.dp).padding(8.dp),
            bitmap = imageResource(if (equipment.eqTypeId == EquipmentType.AG.id) "gates" else "tom"),
            contentDescription = "Equipment Icon: ${equipment.getName()}",
        )
        Column(modifier = Modifier.weight(2f).padding(8.dp)) {
            Text("EQUIPMENT ROLE: ${equipment.eqRole}", fontWeight = FontWeight.Bold)
            Text("NETWORK: ${if (equipment.isConnected) "CONNECTED" else "DISCONNECTED"}")

            if (equipment.eqTypeId == EquipmentType.AG.id) {
                Text("CURRENT MODE: ${equipment.currentModeId}")
                // Add service mode display if available in EquipmentConfig
                // equipment.serviceMode?.let { Text("STATUS: ${it.name.replace("_", " ")}") }
            }
        }
    }
}