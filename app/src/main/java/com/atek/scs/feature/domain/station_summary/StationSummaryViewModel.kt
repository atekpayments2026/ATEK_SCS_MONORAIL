package com.atek.scs.feature.domain.station_summary

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.atek.scs.feature.common.database.entity.EquipmentConfig
import com.atek.scs.feature.common.database.service.EquipmentConfigService
import com.atek.scs.feature.domain.authentication.AuthService
import com.atek.scs.service.EquipmentCommService
import com.atek.scs.service.EquipmentService // Assuming this provides analytics
import com.atek.scs.utils.AuditOperations
import com.atek.scs.utils.GateMode
import com.atek.scs.utils.ServiceMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.tinylog.Logger

/**
 * Manages the UI state and business logic for the Station Summary screen.
 *
 * @param equipmentConfigService Service for interacting with the local equipment configuration database.
 * @param equipmentCommService Service for handling network communication with equipment.
 * @param authService Service for managing authentication and auditing.
 */
class StationSummaryViewModel(
    private val equipmentConfigService: EquipmentConfigService,
    private val equipmentCommService: EquipmentCommService,
    private val authService: AuthService
) : ScreenModel {

    private val _uiState = MutableStateFlow(StationSummaryUiState())
    val uiState: StateFlow<StationSummaryUiState> = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    /**
     * Fetches all necessary data from services and updates the UI state.
     */
    private fun loadInitialData() {
        screenModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val equipments = equipmentConfigService.getAll()
                // Assuming EquipmentService holds the latest analytics data
                val analytics = EquipmentService.analytics
                _uiState.update {
                    it.copy(
                        equipments = equipments,
                        analytics = analytics,
                        isLoggedIn = authService.isUserLoggedIn() // Assuming this method exists
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(dialogMessage = "Failed to load station data: ${e.message}") }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    /**
     * Updates the local coordinates of an equipment configuration in the UI state.
     * This does not persist changes until [saveLayout] is called.
     */
    fun setCoordinates(equipment: EquipmentConfig, x: Float, y: Float) {
        _uiState.update { currentState ->
            val updatedList = currentState.equipments.map {
                if (it.eqId == equipment.eqId) it.copy(cordX = x.toDouble(), cordY = y.toDouble()) else it
            }
            currentState.copy(equipments = updatedList)
        }
    }

    /**
     * Changes the operating mode for a single piece of equipment.
     */
    fun changeMode(equipment: EquipmentConfig, mode: GateMode) {
        screenModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                equipmentCommService.changeMode(equipment, mode)
                loadInitialData() // Refresh data to ensure UI consistency
            } catch (e: Exception) {
                _uiState.update { it.copy(dialogMessage = "Error changing mode: ${e.message}") }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    /**
     * Updates the service mode for all station equipment, typically for emergencies.
     */
    fun updateStationEmergency(isEmergency: Boolean) {
        screenModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val targetMode = if (isEmergency) ServiceMode.EMERGENCY_START else ServiceMode.EMERGENCY_STOP
            try {
                val (_, failures) = equipmentCommService.updateServiceModes(_uiState.value.equipments, targetMode)
                val message = buildString {
                    append(if (isEmergency) "Station emergency activated." else "Station returned to service.")
                    if (failures.isNotEmpty()) {
                        val failedNames = failures.joinToString(", ") { it.first.getName() }
                        append("\n\nCould not update: $failedNames")
                    }
                }
                _uiState.update { it.copy(dialogMessage = message) }
                loadInitialData() // Refresh state
            } catch (e: Exception) {
                _uiState.update { it.copy(dialogMessage = "An error occurred: ${e.message}") }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    /**
     * Persists the current equipment layout (including coordinates) to the database.
     */
    fun saveLayout() {
        screenModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                equipmentConfigService.saveAll(_uiState.value.equipments)
                authService.updateAuditConfig(AuditOperations.CHANGE_LAYOUT)
                _uiState.update { it.copy(dialogMessage = "Layout saved successfully.") }
            } catch (e: Exception) {
                Logger.error(e, "Failed to save layout")
                _uiState.update { it.copy(dialogMessage = "Failed to save layout: ${e.message}") }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    /**
     * Updates the service mode for a single piece of equipment.
     */
    fun updateEquipmentServiceMode(eqId: String, serviceMode: ServiceMode) {
        screenModelScope.launch {
            _uiState.value.equipments.find { it.eqId == eqId }?.let { equipment ->
                _uiState.update { it.copy(isLoading = true) }
                try {
                    equipmentCommService.updateServiceModes(equipment, serviceMode)
                    loadInitialData() // Refresh state from the source of truth
                } catch (e: Exception) {
                    _uiState.update { it.copy(dialogMessage = "${equipment.getName()}: ${e.message}") }
                } finally {
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    /**
     * Logs the current user out.
     */
    fun logout() {
        screenModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                authService.logout()
                _uiState.update { it.copy(isLoggedIn = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(dialogMessage = "Logout failed: ${e.message}") }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    /**
     * Clears the dialog message from the state, to be called by the UI when the dialog is dismissed.
     */
    fun onDialogDismissed() {
        _uiState.update { it.copy(dialogMessage = null) }
    }
}