package com.atek.scs.feature.domain.station_reporting

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.atek.scs.feature.common.database.service.ConfigService
import com.atek.scs.feature.domain.station_reporting.end_of_shifts.configuration.EndOfShiftsRequest
import com.atek.scs.feature.domain.station_reporting.end_of_shift_report.configuration.TomEosRequest
import com.atek.scs.feature.domain.station_reporting.end_of_shift_report.configuration.TomEosResponse
import com.atek.scs.feature.domain.station_reporting.end_of_shift_report.configuration.TvmEosRequest
import com.atek.scs.feature.domain.station_reporting.end_of_shift_report.configuration.TvmEosResponse
import com.atek.scs.feature.domain.station_reporting.end_of_shifts.configuration.EndOfShiftsResponse
import com.atek.scs.feature.domain.station_reporting.repo.StationReportRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.tinylog.Logger


class StationReportingViewModel(
    val navigateToTomEosInputScreen: (TomEosResponse) -> Unit,
    val navigateToTvmEosInputScreen: (TvmEosResponse) -> Unit,
    /* val navigateToSaleReport: (SaleReportResponse) -> Unit*/
    val navigateToEosDataScreen: (EndOfShiftsResponse) -> Unit,
) : ScreenModel {

    var eosList by mutableStateOf<EndOfShiftsResponse?>(null)

    var equipmentId by mutableStateOf("")
    var equipmentIdError: String? by mutableStateOf(null)

    var userId by mutableStateOf("")
    var userIdError: String? by mutableStateOf(null)
    var shiftId: Int? by mutableStateOf(null)
    var shiftIdError: String? by mutableStateOf(null)

    var startDate by mutableStateOf("")
    var startDateError: String? by mutableStateOf(null)
    var endDate by mutableStateOf("")
    var endDateError: String? by mutableStateOf(null)

    var stationName: String? by mutableStateOf("")

    var stationId: Int? by mutableStateOf(null)

    var totalRiderShip: String? by mutableStateOf(null)

    fun onCreate() {
        val config = ConfigService.getConfig()
            ?: error("Device is not configured yet!")
        Logger.info{ "Station Id: $config" }
        stationName = config.stnName
        stationId = config.stnId.toInt()
        Logger.info{"Start: $stationId"}
    }
    // ------------------------------------------------ VALIDATE INPUT OF TOM AND TVM --------------------------------------------
    fun validateInputs(): Boolean {
        var isValid = true

        // Equipment ID
        if (equipmentId.isBlank() || equipmentId.any { !it.isDigit() }) {
            equipmentIdError = "Equipment ID must be entered"
            isValid = false
        } else {
            // Check station prefix safely
            val eqStationIdPrefix = equipmentId.getOrNull(1)?.digitToIntOrNull()
            if (eqStationIdPrefix != stationId) {
                equipmentIdError = "Wrong Station ID!"
                isValid = false
            } else {
                equipmentIdError = null
            }
        }

        // User ID
        if (userId.isBlank() || userId.any { !it.isDigit() }) {
            userIdError = "User ID must be entered"
            isValid = false
        } else {
            userIdError = null
        }

        // Shift ID
        if (shiftId == null) {
            shiftIdError = "Shift ID must be entered"
            isValid = false
        } else {
            shiftIdError = null
        }

        // Start Date
        if (startDate.isBlank()) {
            startDateError = "Date cannot be blank"
            isValid = false
        } else {
            startDateError = null
        }

        // End Date
        if (endDate.isBlank()) {
            endDateError = "Date cannot be blank"
            isValid = false
        } else {
            endDateError = null
        }

        return isValid
    }


    // --------------------------------------------------- FETCH TOM EOS REPORT ---------------------------------------------------
    fun fetchTOMReport() {
        if (!validateInputs()) return
      /*  if(!validatedEqId()) return*/

        screenModelScope.launch {

            val config = ConfigService.getConfig()
                ?: error("Device is not configured yet!")

            try {
                val request = TomEosRequest(
                    stnId = config.stnId,
                    eqId = equipmentId,
                    userId = userId,
                    shiftId = shiftId,
                    shiftStart = startDate,
                    shiftEnd = endDate
                )
                val response = StationReportRepository.getTomEosReport(request)
                withContext(Dispatchers.Main) {
                    navigateToTomEosInputScreen(response)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // ---------------------------------------------------------------FETCH TVM EOS REPORT ---------------------------------------------
    fun fetchTVMReport() {
        if (!validateInputs()) return
/*
        if (!validatedEqId()) return
*/

        screenModelScope.launch {

            val config = ConfigService.getConfig()
                ?: error("Device is not configured yet!")

            try {
                val request = TvmEosRequest(
                    stnId = config.stnId,
                    eqId = equipmentId,
                    userId = userId,
                    shiftId = shiftId,
                    shiftStart = startDate,
                    shiftEnd = endDate
                )
                val response = StationReportRepository.getTvmEosReport(request)
                withContext(Dispatchers.Main) {
                    navigateToTvmEosInputScreen(response)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // -------------------------------------------------- END OF SHIFTS LIST REPORT --------------------------------------------------------
    fun fetchEqsEoslist() {
/*
        if(!validatedEqId()) return
*/

        if (startDate.isBlank()) {
           startDateError = "Date cannot be blank"
        }
        if (equipmentId.isBlank() || equipmentId.any { !it.isDigit() }) {
            equipmentIdError = "Equipment ID must be entered"
            return
        } else {
            equipmentIdError = null
        }
        screenModelScope.launch {
            try {
                val request = EndOfShiftsRequest(
                    date = startDate,
                    eqId = equipmentId
                )
                val response = StationReportRepository.getEquipmentsEOS(request)
                eosList = response
                withContext(Dispatchers.Main) {
                 navigateToEosDataScreen(response)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}


