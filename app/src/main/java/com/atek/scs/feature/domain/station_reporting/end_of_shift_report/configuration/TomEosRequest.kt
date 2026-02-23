package com.atek.scs.feature.domain.station_reporting.end_of_shift_report.configuration

import com.google.gson.annotations.SerializedName

data class TomEosRequest(

    @SerializedName("stn_id") val stnId: Long?,
    @SerializedName("eq_id") val eqId: String?,
    @SerializedName("user_id") val userId: String?,
    @SerializedName("shift_id") val shiftId: Int?,
    @SerializedName("shift_start") val shiftStart: String?,
    @SerializedName("shift_end") val shiftEnd: String?
)