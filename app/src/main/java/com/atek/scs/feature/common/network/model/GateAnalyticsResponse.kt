package com.atek.scs.feature.common.network.model

import com.atek.scs.utils.GateMode
import com.atek.scs.utils.ServiceMode
import com.atek.scs.utils.fromJson
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class GateAnalyticsResponse(
    val config: GateConfig,
    val systemTime: String,
    val transactions: List<Transaction>
) {
    @Serializable
    data class Transaction(
        val passId: Long,
        val productId: Long,
        val mediaTypeId: Long,
        val entryCount: Long,
        val exitCount: Long,
        val totalCount: Long,
    )
    @Serializable
    data class GateConfig(
        @SerializedName("eq_id") val eqId: String,
        @SerializedName("eq_mode_id") var eqModeId: Long,
        @SerializedName("current_eq_mode") var currentEqMode: GateMode?,
        @SerializedName("status") val status: Boolean,
        @SerializedName("stn_id") val stnId: Long,
        @SerializedName("stn_name") val stnName: String,
        @SerializedName("merchant_id") var merchantId: String,
        @SerializedName("client_id") var clientId: String,
        @SerializedName("acquirer_id") var acquirerId: Long,
        @SerializedName("acquirer_name") var acquirerName: String,
        @SerializedName("operator_id") var operatorId: Long,
        @SerializedName("service_mode") var serviceMode: ServiceMode? = null,
        @SerializedName("eq_version") val eqVersion: Long,
        @SerializedName("fare_version") val fareVersion: Long,
        @SerializedName("pass_version") val passVersion: Long,
        @SerializedName("cl_blacklist_version") val revokedVersion: Long,
        @SerializedName("entry_exit_override") val entryExitOverride: Boolean = false,
        @SerializedName("excess_time_override") val excessTimeOverride: Boolean = false,
        @SerializedName("excess_fare_override") val excessFareOverride: Boolean = false,
        @SerializedName("fare_override_one") val fareOverrideOne: Boolean = false,
        @SerializedName("fare_override_two") val fareOverrideTwo: Boolean = false
    )
}

fun main() {
    val data = "{\"status\":true,\"message\":\"Operation successful\",\"data\":{\"config\":{\"id\":1,\"eq_id\":\"040102\",\"eq_mode_id\":3,\"current_eq_mode\":\"BI_DI\",\"status\":true,\"stn_id\":4,\"stn_name\":\"Andheri\",\"merchant_id\":\"Mumbai80360927499450\",\"client_id\":\"FIEG0N001\",\"acquirer_id\":1,\"acquirer_name\":\"PPBL_FEIG\",\"operator_id\":1,\"eq_version\":3,\"fare_version\":34,\"pass_version\":74,\"cl_blacklist_version\":150,\"service_mode\":\"IN_SERVICE\",\"entryExitOverride\":false,\"excessTimeOverride\":false,\"excessFareOverride\":false,\"fareOverrideOne\":false,\"fareOverrideTwo\":false},\"systemTime\":\"2025-09-05 18:59:36\",\"transactions\":[{\"passId\":15,\"productId\":1,\"mediaTypeId\":4,\"entryCount\":0,\"exitCount\":3,\"totalCount\":3}]}}"
    val response = data.fromJson<GateResponse<GateAnalyticsResponse>>()
    println(response)
}