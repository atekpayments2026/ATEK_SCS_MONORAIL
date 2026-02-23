package com.atek.scs.feature.domain.special_fare_modes

import com.atek.scs.feature.common.database.entity.EquipmentConfig
import com.atek.scs.feature.common.network.model.SpecialFareModes

/**
 * Represents the complete, immutable state for the Special Fare Modes screen.
 *
 * @property equipments The list of all available gate equipments, paired with a boolean indicating if they are selected.
 * @property specialFareModes An object holding the state of all selectable fare modes.
 * @property selectAllEquipments The checked state of the "Select All Equipments" checkbox.
 * @property selectAllModes The checked state of the "Select All Modes" checkbox.
 * @property isLoading True if a background operation (like a network call) is in progress.
 * @property dialogMessage An optional message to be displayed in a dialog, typically for showing command results or errors.
 */
data class SpecialFareModesUiState(
    val equipments: List<Pair<EquipmentConfig, Boolean>> = emptyList(),
    val specialFareModes: SpecialFareModes = SpecialFareModes(),
    val selectAllEquipments: Boolean = false,
    val selectAllModes: Boolean = false,
    val isLoading: Boolean = false,
    val dialogMessage: String? = null
)