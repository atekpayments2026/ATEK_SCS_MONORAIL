package com.atek.scs.feature.domain.special_fare_modes

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.atek.scs.feature.common.database.entity.EquipmentConfig
import com.atek.scs.feature.common.database.service.EquipmentConfigService
import com.atek.scs.feature.common.network.model.SpecialFareModes
import com.atek.scs.service.EquipmentCommService
import com.atek.scs.utils.EquipmentType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Manages the UI state and business logic for the Special Fare Modes screen.
 *
 * @param equipmentConfigService Service for accessing local equipment data.
 * @param equipmentCommService Service for sending commands to equipment.
 */
class SpecialFareModesViewModel(
    private val equipmentConfigService: EquipmentConfigService,
    private val equipmentCommService: EquipmentCommService
) : ScreenModel {

    private val _uiState = MutableStateFlow(SpecialFareModesUiState())
    val uiState: StateFlow<SpecialFareModesUiState> = _uiState.asStateFlow()

    fun onCreate() {
        screenModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val gateEquipments = equipmentConfigService.getAll()
                    .filter { it.eqTypeId == EquipmentType.AG.id }
                    .map { it to false } // Pair with selection state
                    .sortedBy { it.first.eqId }
                _uiState.update { it.copy(equipments = gateEquipments) }
            } catch (e: Exception) {
                _uiState.update { it.copy(dialogMessage = "Error loading equipment: ${e.message}") }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    /**
     * Sends the selected special fare modes to the selected equipment.
     */
    fun sendCommand() {
        screenModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val state = _uiState.value
            val selectedEquipments = state.equipments.filter { it.second }.map { it.first }
            val selectedModes = state.specialFareModes

            try {
                val (successes, failures) = equipmentCommService.updateFareSpecialModes(selectedEquipments, selectedModes)

                val message = buildString {
                    append("Command Results:\n\n")
                    if (successes.isNotEmpty()) {
                        append("Successfully updated:\n")
                        append(successes.joinToString(", ") { it.getName() })
                    } else {
                        append("No equipment was updated successfully.")
                    }

                    if (failures.isNotEmpty()) {
                        append("\n\nFailed to update (check connection):\n")
                        append(failures.joinToString(", ") { it.first.getName() })
                    }
                }
                _uiState.update { it.copy(dialogMessage = message) }
                clearAllToggles()
            } catch (e: Exception) {
                _uiState.update { it.copy(dialogMessage = "An error occurred: ${e.message}") }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    /**
     * Updates the state of a single special mode checkbox.
     */
    fun onSpecialModeSelected(modeName: String, isChecked: Boolean) {
        val currentModes = _uiState.value.specialFareModes
        val newModes = when (modeName) {
            "ENTRY EXIT OVERRIDE" -> currentModes.copy(entryExitOverride = isChecked)
            "EXCESS TIME OVERRIDE" -> currentModes.copy(excessTimeOverride = isChecked)
            "EXCESS FARE OVERRIDE" -> currentModes.copy(excessFareOverride = isChecked)
            "FARE OVERRIDE ONE" -> currentModes.copy(fareOverrideOne = isChecked)
            "FARE OVERRIDE TWO" -> currentModes.copy(fareOverrideTwo = isChecked)
            else -> currentModes
        }
        _uiState.update { it.copy(specialFareModes = newModes, selectAllModes = newModes.modesAsMap.all { entry -> entry.value }) }
    }

    /**
     * Updates the selection state of a single equipment checkbox.
     */
    fun onEquipmentSelected(equipment: EquipmentConfig, isChecked: Boolean) {
        _uiState.update { currentState ->
            val updatedEquipments = currentState.equipments.map { (eq, currentSelection) ->
                if (eq.eqId == equipment.eqId) {
                    eq to isChecked
                } else {
                    eq to currentSelection
                }
            }
            currentState.copy(
                equipments = updatedEquipments,
                selectAllEquipments = updatedEquipments.all { it.second }
            )
        }
    }

    /**
     * Toggles all special modes on or off.
     */
    fun onToggleAllModes(isChecked: Boolean) {
        _uiState.update {
            it.copy(
                selectAllModes = isChecked,
                specialFareModes = SpecialFareModes(
                    entryExitOverride = isChecked,
                    excessTimeOverride = isChecked,
                    excessFareOverride = isChecked,
                    fareOverrideOne = isChecked,
                    fareOverrideTwo = isChecked
                )
            )
        }
    }

    /**
     * Toggles all equipment selections on or off.
     */
    fun onToggleAllEquipments(isChecked: Boolean) {
        _uiState.update { currentState ->
            val updatedEquipments = currentState.equipments.map { it.first to isChecked }
            currentState.copy(selectAllEquipments = isChecked, equipments = updatedEquipments)
        }
    }

    /**
     * Resets all selections on the screen to their default (false) state.
     */
    fun clearAllToggles() {
        onToggleAllModes(false)
        onToggleAllEquipments(false)
    }

    /**
     * Clears the dialog message from the state.
     */
    fun onDialogDismissed() {
        _uiState.update { it.copy(dialogMessage = null) }
    }
}