package com.atek.scs.feature.domain.configuration.model

import com.google.gson.annotations.SerializedName

data class ConfigRequest(
    val ip: String,
    @SerializedName("eq_type_id")
    val eqTypeId: Long = 4, // TOM
    @SerializedName("eq_version")
    val eqVersion: Long = 0,
    @SerializedName("fare_version")
    val fareVersion: Long = 0,
    @SerializedName("pass_version")
    val passVersion: Long = 0
)
