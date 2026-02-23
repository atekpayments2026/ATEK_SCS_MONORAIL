package com.atek.scs.feature.common.database.entity

data class AnalyticsConfig(
    val id: Long = 0,
    val equipmentId: String,
    val passId: Long,
    val productId: Long,
    val mediaTypeId: Long,
    val entryCount: Long,
    val exitCount: Long,
    val totalCount: Long,
)