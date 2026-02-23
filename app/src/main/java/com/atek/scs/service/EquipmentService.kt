package com.atek.scs.service

import androidx.compose.runtime.*
import com.atek.scs.feature.common.database.entity.Config
import com.atek.scs.feature.common.database.entity.EquipmentConfig
import com.atek.scs.feature.common.database.service.AnalyticsConfigService
import com.atek.scs.feature.common.database.service.ConfigService
import com.atek.scs.feature.common.database.service.EquipmentConfigService
import com.atek.scs.feature.common.database.toConfig
import com.atek.scs.utils.EquipmentType
import com.atek.scs.utils.MediaType
import database.GetSummaryByMediaType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * A singleton service acting as a centralized, in-memory cache for application-wide data.
 *
 * This object is responsible for observing and holding real-time data for equipment,
 * system settings, and analytics. It exposes data using Jetpack Compose's state primitives
 * to drive reactive UI updates.
 *
 * ### Lifecycle Management
 * The service's lifecycle is managed externally. It does not own its own CoroutineScope,
 * a critical design choice to prevent memory leaks. The `start(scope)` method
 * must be called by a lifecycle-aware component (e.g., a ViewModel), which provides its
 * own `CoroutineScope`. This ties the service's jobs to the component's lifecycle.
 *
 * ### Key Principles
 * - **Single Source of Truth**: The service holds one primary list for all equipment.
 *   Filtered lists are derived reactively from this source, reducing memory and
 *   eliminating data duplication.
 * - **External Lifecycle Ownership**: Coroutine jobs are tied to an external,
 *   lifecycle-aware scope to ensure proper cancellation and prevent memory leaks.
 */
object EquipmentService {

    /**
     * Holds the primary application configuration. Null until fetched.
     */
    var config by mutableStateOf<Config?>(null)

    /**
     * The single source of truth for all equipment configurations.
     * This is the only list that is directly modified by the data collection flow.
     */
    val equipments = mutableStateListOf<EquipmentConfig>()

    /**
     * Derived, read-only state lists for specific equipment types.
     *
     * These are computed reactively from the main `equipments` list using `derivedStateOf`.
     * This is highly efficient because the filter operation is only re-executed when the
     * `equipments` list actually changes. This pattern avoids redundant data storage
     * and ensures the UI always shows a consistent, filtered view.
     */
    val gates by derivedStateOf { equipments.filter { it.eqTypeId == EquipmentType.AG.id } }
    val tom by derivedStateOf { equipments.filter { it.eqTypeId == EquipmentType.TOM.id } }
    val tvm by derivedStateOf { equipments.filter { it.eqTypeId == EquipmentType.TVM.id } }
    val tr by derivedStateOf { equipments.filter { it.eqTypeId == EquipmentType.TR.id } }

    /**
     * Observable state for analytics summaries, keyed by [MediaType].
     * An empty map represents the initial or "no data" state.
     */
    var analytics by mutableStateOf<Map<MediaType, GetSummaryByMediaType>>(emptyMap())

    /**
     * A reference to the main job supervising all data collection coroutines.
     * This allows for a clean shutdown of all observers via the `stop()` method.
     */
    private var serviceJob: Job? = null

    /**
     * Starts all data collection flows on a provided [CoroutineScope].
     * This function is idempotent; it safely handles being called multiple times.
     *
     * @param scope The CoroutineScope from a lifecycle-aware component (e.g., `viewModelScope`).
     */
    fun start(scope: CoroutineScope) {
        stop()
        serviceJob = scope.launch {

            // Observe application configuration.
            launch {
                ConfigService
                    .getConfigFlow()
                    .collectLatest { configResult ->
                        config = configResult.executeAsOneOrNull()?.toConfig()
                    }
            }

            // Observe the primary list of equipment configurations.
            launch {
                EquipmentConfigService
                    .getAllFlow()
                    .collectLatest { equipmentResult ->
                        val allEquipment = equipmentResult.executeAsList().map { it.toConfig() }
                        equipments.clear()
                        equipments.addAll(allEquipment)
                    }
            }

            // Observe and categorize analytics summaries.
            launch {
                AnalyticsConfigService
                    .getSummaryByMediaTypeFlow()
                    .collectLatest { analyticsResult ->
                        val summaries = analyticsResult.executeAsList()
                        val groupedByMediaType = summaries.groupBy { it.mediaTypeId }
                        analytics = MediaType.entries
                            .mapNotNull { mediaType ->
                                groupedByMediaType[mediaType.id]?.firstOrNull()
                                    ?.let { mediaType to it }
                            }
                            .toMap()
                    }
            }
        }
    }

    /**
     * Stops all data collection coroutines and resets the service's state to default.
     * This is critical for preventing memory leaks and must be called when the service
     * is no longer needed (e.g., in a ViewModel's `onCleared()`).
     */
    fun stop() {
        serviceJob?.cancel()
        serviceJob = null
        config = null
        equipments.clear()
        analytics = emptyMap()
    }

}