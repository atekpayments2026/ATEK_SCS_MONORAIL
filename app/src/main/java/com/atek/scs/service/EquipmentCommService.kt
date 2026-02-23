package com.atek.scs.service

import com.atek.scs.feature.common.database.entity.AnalyticsConfig
import com.atek.scs.feature.common.database.entity.EquipmentConfig
import com.atek.scs.feature.common.database.service.AnalyticsConfigService.saveOrUpdate
import com.atek.scs.feature.common.database.service.EquipmentConfigService
import com.atek.scs.feature.common.database.service.EquipmentConfigService.saveOrUpdate
import com.atek.scs.feature.common.network.ApiManager
import com.atek.scs.feature.common.network.model.GateAnalyticsResponse
import com.atek.scs.feature.common.network.model.SpecialFareModes
import com.atek.scs.utils.GateMode
import com.atek.scs.utils.ServiceMode
import kotlinx.coroutines.*
import org.tinylog.Logger
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

/**
 * A singleton object responsible for handling all communication with equipment.
 * This service manages fetching equipment status, updating configurations, and handling concurrent network operations.
 */
object EquipmentCommService {

    private val PULSE_INTERVAL_MS = TimeUnit.MINUTES.toMillis(1)
    private val PULSE_FAILURE_RETRY_DELAY_MS = TimeUnit.MINUTES.toMillis(5)

    /**
     * Executes a given Retrofit API call and handles the response.
     * It returns the response body on success or throws a detailed exception on failure.
     *
     * @param T The type of the successful response body.
     * @param apiCall A suspend lambda function representing the Retrofit API call.
     * @return The response body of type [T].
     * @throws Exception if the API call fails, with a message indicating the reason.
     */
    private suspend fun <T> executeOrThrow(apiCall: suspend () -> Response<T>): T {
        try {
            val response = apiCall()
            if (response.isSuccessful) {
                return response.body() ?: throw Exception("Server returned a successful response but with no data.")
            } else {
                val errorBody = response.errorBody()?.string() ?: "No additional details from server."
                val errorMessage = when (response.code()) {
                    400 -> "Bad Request: The data sent to the server was incorrect. ($errorBody)"
                    401 -> "Unauthorized: You are not authenticated. Please log in again."
                    403 -> "Forbidden: You don't have permission to perform this action."
                    404 -> "Not Found: The requested item could not be found on the server."
                    500 -> "Internal Server Error: A problem occurred on the server. Please try again later."
                    else -> "Request Failed: The server responded with an error (Code: ${response.code()})."
                }
                throw Exception(errorMessage)
            }
        } catch (e: Exception) {
            if (e.cause == null && e !is SocketTimeoutException && e !is UnknownHostException && e !is ConnectException)
                throw e
            val friendlyMessage = when (e) {
                is SocketTimeoutException -> "Connection Timeout: The server took too long to respond."
                is UnknownHostException -> "Network Error: Could not connect to the server. Check the IP address and network connection."
                is ConnectException -> "Connection Failed: Could not establish a connection. The server may be offline."
                else -> e.message ?: "An unexpected network error occurred."
            }
            throw Exception(friendlyMessage, e)
        }
    }

    /**
     * Updates the local database with the latest status and transaction data from a piece of equipment.
     *
     * @param data The [GateAnalyticsResponse] containing the latest configuration and transaction data.
     * @return `true` if the equipment was found and updated, `false` otherwise.
     */
    private fun updateEquipmentStatus(data: GateAnalyticsResponse): Boolean {
        val equipment = EquipmentConfigService.findFirstByEqId(data.config.eqId)
            ?: return false
        val updatedEquipment = equipment.copy(
            serviceMode = data.config.serviceMode,
            entryExitOverride = data.config.entryExitOverride,
            excessTimeOverride = data.config.excessTimeOverride,
            excessFareOverride = data.config.excessFareOverride,
            fareOverrideOne = data.config.fareOverrideOne,
            fareOverrideTwo = data.config.fareOverrideTwo
        )
        if (equipment != updatedEquipment)
            updatedEquipment.saveOrUpdate()
        data.transactions.forEach {
            AnalyticsConfig(
                equipmentId = equipment.eqId,
                passId = it.passId,
                productId = it.productId,
                mediaTypeId = it.mediaTypeId,
                entryCount = it.entryCount,
                exitCount = it.exitCount,
                totalCount = it.totalCount
            ).saveOrUpdate()
        }
        return true
    }

    /**
     * Starts a long-running coroutine that periodically fetches the status of all registered equipment.
     * This function will continue to run as long as its coroutine scope is active.
     */

    fun CoroutineScope.startPulse(): Job = launch(Dispatchers.IO) {
        while (isActive) {
            try {
                val equipments = EquipmentConfigService.getAll()

                val deferredResults = equipments.map { equipment ->
                    async {
                        try {
                            val response = executeOrThrow {
                                ApiManager
                                    .equipmentApiService(equipment.ipAddress)
                                    .status()
                            }

                            if (response.data != null && updateEquipmentStatus(response.data)) {
                                Logger.trace { "Equipment ${equipment.eqId} status updated successfully." }
                            } else {
                                Logger.warn { "Equipment ${equipment.eqId} not found in local database or response data was null." }
                            }
                        } catch (e: Exception) {
                            Logger.error(e) { "Failed to fetch status from equipment ${equipment.eqId} at ${equipment.ipAddress}" }
                        }
                    }
                }

                deferredResults.awaitAll()
                delay(PULSE_INTERVAL_MS)
            } catch (e: Exception) {
                Logger.error(e) { "Error during equipment pulse" }
                delay(PULSE_FAILURE_RETRY_DELAY_MS)
            }
        }
    }
//    suspend fun startPulse() = withContext(Dispatchers.IO) {
//        while (isActive) {
//            try {
//                val equipments = EquipmentConfigService.getAll()
//                val deferredResults = equipments.map { equipment ->
//                    async {
//                        try {
//                            val response = executeOrThrow {
//                                ApiManager
//                                    .equipmentApiService(equipment.ipAddress)
//                                    .status()
//                            }
//                            if (response.data != null && updateEquipmentStatus(response.data)) {
//                                Logger.trace("Equipment ${equipment.eqId} status updated successfully.")
//                            } else {
//                                Logger.warn("Equipment ${equipment.eqId} not found in local database or response data was null.")
//                            }
//                        } catch (e: Exception) {
//                            Logger.error("Failed to fetch status from equipment ${equipment.eqId} at ${equipment.ipAddress}: ${e.message}")
//                        }
//                    }
//                }
//                deferredResults.awaitAll()
//                delay(PULSE_INTERVAL_MS)
//            } catch (e: Exception) {
//                Logger.error("Error during equipment pulse: ${e.message}")
//                delay(PULSE_FAILURE_RETRY_DELAY_MS)
//            }
//        }
//    }

    /**
     * Executes a given suspend operation concurrently for a list of items.
     *
     * @param Input The type of the items in the input list.
     * @param Success The type of the successful result of the operation.
     * @param items The list of items to process.
     * @param operation The suspend function to be executed for each item.
     * @return A [Pair] containing a list of successful results and a list of failures,
     *         where each failure is a pair of the input item and the corresponding [Throwable].
     */
    private suspend fun <Input, Success> executeConcurrently(
        items: List<Input>,
        operation: suspend (Input) -> Success
    ): Pair<List<Success>, List<Pair<Input, Throwable>>> = coroutineScope {
        val deferredResults = items.map { item ->
            async {
                try {
                    Result.success(operation(item))
                } catch (e: Throwable) {
                    Result.failure(e)
                }
            }
        }
        val successes = mutableListOf<Success>()
        val failures = mutableListOf<Pair<Input, Throwable>>()
        deferredResults.awaitAll().forEachIndexed { index, result ->
            result.onSuccess { successData ->
                successes.add(successData)
            }.onFailure { error ->
                failures.add(items[index] to error)
            }
        }
        successes to failures
    }

    /**
     * Updates the special fare modes for a single piece of equipment.
     *
     * @param equipment The [EquipmentConfig] of the equipment to update.
     * @param specialFareModes The [SpecialFareModes] to apply.
     * @return The updated [EquipmentConfig].
     * @throws Exception if the API call fails or the server returns a non-success status.
     */
    suspend fun updateFareSpecialModes(
        equipment: EquipmentConfig,
        specialFareModes: SpecialFareModes
    ): EquipmentConfig {
        val response = executeOrThrow {
            ApiManager
                .equipmentApiService(equipment.ipAddress)
                .updateSpecialFareModes(specialFareModes)
        }
        if (!response.status) throw Exception(response.message)
        val updatedEquipment = equipment.copy(
            entryExitOverride = specialFareModes.entryExitOverride,
            excessTimeOverride = specialFareModes.excessTimeOverride,
            excessFareOverride = specialFareModes.excessFareOverride,
            fareOverrideOne = specialFareModes.fareOverrideOne,
            fareOverrideTwo = specialFareModes.fareOverrideTwo,
        )
        updatedEquipment.saveOrUpdate()
        return updatedEquipment
    }

    /**
     * Concurrently updates the special fare modes for a list of equipment.
     *
     * @param equipments The list of [EquipmentConfig] to update.
     * @param specialFareModes The [SpecialFareModes] to apply to all equipment.
     * @return A [Pair] of lists: one for successful updates and one for failures.
     */
    suspend fun updateFareSpecialModes(
        equipments: List<EquipmentConfig>,
        specialFareModes: SpecialFareModes
    ) = executeConcurrently(equipments) { equipment ->
        updateFareSpecialModes(equipment, specialFareModes)
    }

    /**
     * Changes the operating mode of a single piece of equipment.
     *
     * @param equipment The [EquipmentConfig] of the equipment to change.
     * @param gateMode The new [GateMode] to set.
     * @return The updated [EquipmentConfig].
     * @throws Exception if the API call fails or the server returns a non-success status.
     */
    suspend fun changeMode(
        equipment: EquipmentConfig,
        gateMode: GateMode
    ): EquipmentConfig {
        val response = executeOrThrow {
            ApiManager
                .equipmentApiService(equipment.ipAddress)
                .changeMode(gateMode.id)
        }
        if (!response.status) throw Exception(response.message)
        val updatedEquipment = equipment.copy(currentModeId = gateMode.id)
        updatedEquipment.saveOrUpdate()
        return updatedEquipment
    }

    /**
     * Concurrently changes the operating mode for a list of equipment.
     *
     * @param equipments The list of [EquipmentConfig] to change.
     * @param gateMode The new [GateMode] to set for all equipment.
     * @return A [Pair] of lists: one for successful changes and one for failures.
     */
    suspend fun changeMode(
        equipments: List<EquipmentConfig>,
        gateMode: GateMode
    ) = executeConcurrently(equipments) { equipment ->
        changeMode(equipment, gateMode)
    }

    /**
     * Updates the service mode for a single piece of equipment.
     *
     * @param equipment The [EquipmentConfig] of the equipment to update.
     * @param serviceMode The new [ServiceMode] to set.
     * @return The updated [EquipmentConfig].
     * @throws Exception if the API call fails or the server returns a non-success status.
     */
    suspend fun updateServiceModes(
        equipment: EquipmentConfig,
        serviceMode: ServiceMode
    ): EquipmentConfig {
        val response = executeOrThrow {
            ApiManager
                .equipmentApiService(equipment.ipAddress)
                .changeServiceMode(serviceMode.id)
        }
        if (!response.status) throw Exception(response.message)
        val updatedEquipment = equipment.copy(
            serviceMode = serviceMode
        )
        updatedEquipment.saveOrUpdate()
        return updatedEquipment
    }

    /**
     * Concurrently updates the service modes for a list of equipment.
     *
     * @param equipments The list of [EquipmentConfig] to update.
     * @param serviceMode The new [ServiceMode] to set for all equipment.
     * @return A [Pair] of lists: one for successful updates and one for failures.
     */
    suspend fun updateServiceModes(
        equipments: List<EquipmentConfig>,
        serviceMode: ServiceMode
    ) = executeConcurrently(equipments) { equipment ->
        updateServiceModes(equipment, serviceMode)
    }

}