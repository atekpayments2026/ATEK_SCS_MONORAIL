package com.atek.scs.feature.domain.station_reporting.end_of_shifts.configuration

import com.google.gson.annotations.SerializedName

data class EndOfShiftsRequest(

    val date: String,

    @SerializedName("eq_id")
    val eqId: String
)
