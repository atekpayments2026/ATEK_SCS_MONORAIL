package com.atek.scs.feature.domain.station_reporting.sale_report.configuration/*
package com.atek.scs.features.domain.station_reporting.sale_report.configuration

import com.atek.scs.features.common.database.service.ConfigService
import com.atek.scs.features.domain.station_reporting.repo.StationId
import com.google.gson.annotations.SerializedName

@Suppress("CAST_NEVER_SUCCEEDS")
data class SaleReportResponse(

    val status: Boolean,
    val message: String,
    @SerializedName("start_date") val startDate: String,
    @SerializedName("end_date") val endDate: String,
    val data: List<Data>,
) {

    data class Data(

        @SerializedName("station_name")
        val stationName: String,
        val totalRidership: String,
        val sjt: Sjt,
        val rjt: Rjt,
        @SerializedName("mqr_tp") val mqrTp: MqrTp,
        @SerializedName("ncmc_card") val ncmcCard: NcmcCard,
        @SerializedName("ncmc_sv") val ncmcSv: NcmcSv,
        @SerializedName("ncmc_tp") val ncmcTp: NcmcTp,
        @SerializedName("station_id") val stationId: StationId,

        @SerializedName("extra_object")
        val extraObject: ExtraObject? = null
    )

    data class ExtraObject(
        @SerializedName("some_key")
        val someKey: String,
        @SerializedName("another_key")
        val anotherKey: String
    )

    data class Sjt(
        @SerializedName("issue_count") val issueCount: String,
        @SerializedName("refund_count") val refundCount: String,
        @SerializedName("penalty_count") val penaltyCount: String,
    )

    data class Rjt(
        @SerializedName("issue_count") val issueCount: String,
        @SerializedName("refund_count") val refundCount: String,
        @SerializedName("penalty_count") val penaltyCount: String,
    )

    data class MqrTp(
        @SerializedName("issue_count") val issueCount: String,
        @SerializedName("cancel_count") val cancelCount: String,
        @SerializedName("top_up_count") val topUpCount: String,
        @SerializedName("penalty_count") val penaltyCount: String,
        @SerializedName("exit_count") val exitCount: String,
    )

    data class NcmcCard(
        @SerializedName("sale_count") val saleCount: String,
        @SerializedName("replacement_count") val replacementCount: String,
    )

    data class NcmcSv(
        @SerializedName("top_up_count") val topUpCount: String,
        @SerializedName("penalty_count") val penaltyCount: String,
        @SerializedName("exit_count") val exitCount: String,
    )

    data class NcmcTp(
        @SerializedName("issue_count") val issueCount: String,
        @SerializedName("cancel_count") val cancelCount: String,
        @SerializedName("top_up_count") val topUpCount: String,
        @SerializedName("penalty_count") val penaltyCount: String,
        @SerializedName("exit_count") val exitCount: String,
    )

    fun getStation(): Data? {
        val config = ConfigService.getConfig()
        val stnId = config?.stnId ?: return null

        return data.find { station ->
            when (val id = station.stationId) {
                is StationId.Number -> id.value.toLong() == stnId
                is StationId.Operator -> false
            }
        }
    }

    fun getSJTData() = getStation()?.sjt?.let {
        mapOf(
            "Issue Count" to it.issueCount,
            "Refund Count" to it.refundCount,
            "Penalty Count" to it.penaltyCount
        )
    } ?: emptyMap()

    fun getRjtData() = getStation()?.rjt?.let {
        mapOf(
            "Issue Count" to it.issueCount,
            "Refund Count" to it.refundCount,
            "Penalty Count" to it.penaltyCount
        )
    }

    fun getMqrTpData() = getStation()?.mqrTp?.let {
        mapOf(
            "Issue Count" to it.issueCount,
            "Refund Count" to it.cancelCount,
            "Top-Up Count" to it.topUpCount,
            "Penalty Count" to it.penaltyCount,
            "Exit Count" to it.exitCount
        )
    }

    fun getNcmcData() = getStation()?.ncmcCard?.let {
        mapOf(
            "Sale Count" to it.saleCount,
            "Replacement Count" to it.replacementCount,
        )
    }

    fun getNcmcSvData() = getStation()?.ncmcSv?.let {
        mapOf(
            "Top Up Count" to it.topUpCount,
            "Penalty Count" to it.penaltyCount,
            "Exit Count" to it.exitCount,
        )
    }

    fun getNcmcTpData() = getStation()?.ncmcTp?.let {
        mapOf(
            "Issue Count" to it.issueCount,
            "Cancel Count" to it.cancelCount,
            "Top Up Count" to it.topUpCount,
            "Penalty Count" to it.penaltyCount,
            "Exit Count" to it.exitCount,
        )
    }

    fun getExtraObject() = getStation()?.extraObject

}
*/
