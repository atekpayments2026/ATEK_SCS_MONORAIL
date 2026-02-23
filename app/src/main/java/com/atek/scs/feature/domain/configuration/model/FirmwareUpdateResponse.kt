package com.atek.scs.feature.domain.configuration.model

import com.google.gson.annotations.SerializedName

data class FirmwareUpdateResponse(
    val code: Int,
    val message: String,
    val status: Boolean,
    @SerializedName("uploadID")
    val uploadId: Int? = null,
    val updatedVersion: String? = null,
    val error: String? = null
)