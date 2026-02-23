package com.atek.scs.feature.common.network

import com.atek.scs.feature.common.network.model.GateAnalyticsResponse
import com.atek.scs.feature.common.network.model.GateResponse
import com.atek.scs.feature.common.network.model.SpecialFareModes
import com.atek.scs.feature.domain.configuration.model.ConfigRequest
import com.atek.scs.feature.domain.configuration.model.ConfigResponse
import com.atek.scs.feature.domain.configuration.model.FirmwareUpdateRequest
import com.atek.scs.feature.domain.configuration.model.FirmwareUpdateResponse
import com.atek.scs.feature.domain.station_reporting.end_of_shifts.configuration.EndOfShiftsRequest
import com.atek.scs.feature.domain.station_reporting.end_of_shifts.configuration.EndOfShiftsResponse
import com.atek.scs.feature.domain.station_reporting.end_of_shift_report.configuration.TomEosRequest
import com.atek.scs.feature.domain.station_reporting.end_of_shift_report.configuration.TvmEosRequest
import com.atek.scs.feature.domain.station_reporting.end_of_shift_report.configuration.TomEosResponse
import com.atek.scs.feature.domain.station_reporting.end_of_shift_report.configuration.TvmEosResponse
/*import com.atek.scs.features.domain.station_reporting.sale_report.configuration.SaleReportRequest
import com.atek.scs.features.domain.station_reporting.sale_report.configuration.SaleReportResponse*/
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("v2/config")
    suspend fun getConfig(
        @Body request: ConfigRequest
    ): Response<ConfigResponse>

    @POST("checkUpdate")
    suspend fun checkFirmwareUpdate(
        @Body request: FirmwareUpdateRequest
    ): Response<FirmwareUpdateResponse>

    @GET("secure/v1/admin/status")
    @Headers("Authorization: Basic MTgwMjoxODAy")
    suspend fun checkStatus(): Response<Boolean>

    // ----------------------------------------- EQUIPMENT APIS -----------------------------------------
    @GET("status")
    suspend fun status(): Response<GateResponse<GateAnalyticsResponse>>

    @GET("changeMode/{modeId}")
    suspend fun changeMode(
        @Path("modeId") modeId: Long
    ): Response<GateResponse<String>>

    @GET("changeServiceMode/{serviceModeId}")
    suspend fun changeServiceMode(
        @Path("serviceModeId") serviceModeId: Long
    ): Response<GateResponse<String>>

    @POST("changeSpecialModes")
    suspend fun updateSpecialFareModes(
        @Body request: SpecialFareModes
    ): Response<GateResponse<String>>

    // ----------------------------------------- EQUIPMENT REPORTS APIS -----------------------------------------

    @POST("report/endOfShift")
    suspend fun getTomEosReport(@Body tomEosRequest: TomEosRequest): Response<TomEosResponse>

    @POST("report/tvm/endOfShift")
    suspend fun getTvmEosReport(@Body tvmEosRequest: TvmEosRequest): Response<TvmEosResponse>

/*    @POST("report/daily/station/wise/sale/report")
    suspend fun getSaleRport(@Body saleReportRequest: SaleReportRequest): Response<SaleReportResponse>*/

    @POST("report/equipment/endOfShifts")
    suspend fun getEquipmentsEOS(@Body request: EndOfShiftsRequest): Response<EndOfShiftsResponse>


}