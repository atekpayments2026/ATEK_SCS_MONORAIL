package com.atek.scs.feature.common.network.model

data class GateResponse<T>(
    val status: Boolean,
    val message: String,
    val data: T? = null
)