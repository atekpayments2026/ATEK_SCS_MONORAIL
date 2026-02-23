package com.atek.scs.feature.domain.configuration.model

import com.google.gson.annotations.SerializedName

data class FirmwareUpdateRequest(
    @SerializedName("current_version")
    val currentVersion: String,
    @SerializedName("eq_id")
    val eqId: String,
    @SerializedName("eq_type_id")
    val eqTypeId: Int
)
