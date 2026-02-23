package com.atek.scs.feature.domain.station_reporting.repo/*
package com.atek.scs.features.domain.station_reporting.repo

import com.google.gson.annotations.SerializedName

sealed class StationId {

    data class Number(val value: Int) : StationId()

    data class Operator(
        @SerializedName("operator_id") val operatorId: Int,
        @SerializedName("operator_name") val operatorName: String
    ) : StationId()
}
*/
