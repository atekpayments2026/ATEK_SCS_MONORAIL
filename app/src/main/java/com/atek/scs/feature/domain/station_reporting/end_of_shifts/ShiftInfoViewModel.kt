package com.atek.scs.feature.domain.station_reporting.end_of_shifts

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.atek.scs.feature.domain.station_reporting.StationReportingViewModel
import com.atek.scs.feature.domain.station_reporting.end_of_shift_report.configuration.TomEosRequest
import com.atek.scs.feature.domain.station_reporting.end_of_shift_report.configuration.TomEosResponse
import com.atek.scs.feature.domain.station_reporting.end_of_shift_report.configuration.TvmEosRequest
import com.atek.scs.feature.domain.station_reporting.end_of_shift_report.configuration.TvmEosResponse
import com.atek.scs.feature.domain.station_reporting.end_of_shifts.configuration.EndOfShiftsRequest
import com.atek.scs.feature.domain.station_reporting.end_of_shifts.configuration.EndOfShiftsResponse
import com.atek.scs.feature.domain.station_reporting.repo.StationReportRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.tinylog.Logger

class ShiftInfoViewModel(
    val navigateToTomEosInputScreen: (TomEosResponse) -> Unit,
    val navigateToTvmEosInputScreen: (TvmEosResponse) -> Unit,
    /* val navigateToSaleReport: (SaleReportResponse) -> Unit*/
    val navigateToEosDataScreen: (EndOfShiftsResponse) -> Unit,
) : ScreenModel {

    var eosList by mutableStateOf<EndOfShiftsResponse?>(null)

    var tomData by mutableStateOf<TomEosResponse?>(null)
    var tvmData by mutableStateOf<TvmEosResponse?>(null)

    var eq by mutableStateOf("")

    fun eqTpeTransfer(viewModel: StationReportingViewModel){

        eq = viewModel.equipmentId.substring(2,4)

    }

    fun validateEqId(viewModel: StationReportingViewModel): Boolean {
        var isValid = true

        if (viewModel.equipmentId.isBlank() || viewModel.equipmentId.any { !it.isDigit() }) {
            viewModel.equipmentIdError = "Equipment ID must be entered"
            isValid = false
        } else {
            val eqStationIdPrefix = viewModel.equipmentId.getOrNull(1)?.digitToIntOrNull()
            if (eqStationIdPrefix != viewModel.stationId) {
                viewModel.equipmentIdError = "Wrong Station ID!"
                isValid = false
            } else {
                viewModel.equipmentIdError = null
            }
        }

        return isValid
    }

    fun fetchSpecificReport(viewModel: StationReportingViewModel) {
        screenModelScope.launch {
            try {

                if (!validateEqId(viewModel)) {
                    return@launch
                }

                val request = EndOfShiftsRequest(
                    date = viewModel.startDate,
                    eqId = viewModel.equipmentId
                )

                val response = StationReportRepository.getEquipmentsEOS(request)
                eosList = response

                val data = eosList?.data?.firstOrNull()
                if (data == null) {
                    return@launch
                }

                Logger.info{ "Data found. EQ_ID In Specific Eos Report: ${data.eqId}" }

                val parts = data.eqId.chunked(2)
                val secondPart = parts.getOrNull(1)

                when (secondPart) {
                    "02" -> {
                        val tomRequest = TomEosRequest(
                            stnId = data.stnId,
                            eqId = data.eqId,
                            userId = data.operator,
                            shiftId = data.shiftId,
                            shiftStart = data.startDate,
                            shiftEnd = data.endDate
                        )
                        val response = StationReportRepository.getTomEosReport(tomRequest)

                        withContext(Dispatchers.Main) {
                            navigateToTomEosInputScreen(response)
                        }
                    }

                    "03" -> {
                        val tvmRequest = TvmEosRequest(
                            stnId = data.stnId,
                            eqId = data.eqId,
                            userId = data.operator,
                            shiftId = data.shiftId,
                            shiftStart = data.startDate,
                            shiftEnd = data.endDate
                        )
                        val response = StationReportRepository.getTvmEosReport(tvmRequest)

                        withContext(Dispatchers.Main) {
                            navigateToTvmEosInputScreen(response)
                        }
                    }
                    else -> {
                        Logger.warn{"Unknown equipment type: '$secondPart'. Navigation will not happen."}
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Logger.error{ "EXCEPTION CAUGHT: ${e.message}" }
            }
        }
    }
}
