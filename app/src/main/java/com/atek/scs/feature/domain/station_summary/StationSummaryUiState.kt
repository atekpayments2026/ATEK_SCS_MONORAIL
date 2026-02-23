package com.atek.scs.feature.domain.station_summary

import com.atek.scs.feature.common.database.entity.EquipmentConfig
import com.atek.scs.utils.MediaType
import database.GetSummaryByMediaType

/**
 * Represents the complete, immutable state for the Station Summary screen.
 *
 * Using a single state object is a best practice for modern Android development
 * with Jetpack Compose and ViewModels. It makes state management predictable and robust.
 *
 * @property equipments The current list of all equipment configurations to be displayed.
 * @property analytics A map containing transaction summaries keyed by media type.
 * @property isLoading True if a background operation (like a network call) is in progress.
 * @property dialogMessage An optional message to be displayed in a dialog. If null, no dialog is shown.
 * @property isLoggedIn Represents the current authentication status.
 */
data class StationSummaryUiState(
    val equipments: List<EquipmentConfig> = emptyList(),
    val analytics: Map<MediaType, GetSummaryByMediaType?> = emptyMap(),
    val isLoading: Boolean = false,
    val dialogMessage: String? = null,
    val isLoggedIn: Boolean = false
)