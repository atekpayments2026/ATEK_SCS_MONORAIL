package com.atek.scs.feature.domain.station_reporting.end_of_shifts.configuration

import com.google.gson.annotations.SerializedName

data class EndOfShiftsResponse(

    val data: List<Data>,
    val message: String,
    val status: Boolean

) {
    data class Data(

        @SerializedName("shift_id")
        val shiftId: Int,

        @SerializedName("stn_id")
        val stnId: Long,

        @SerializedName("eq_id")
        val eqId: String,

        @SerializedName("operator")
        val operator: String,

        @SerializedName("start_date")
        val startDate: String,

        @SerializedName("end_date")
        val endDate: String
    )
}
