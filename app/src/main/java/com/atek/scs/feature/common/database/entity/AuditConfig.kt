package com.atek.scs.feature.common.database.entity

import com.atek.scs.utils.EquipmentType

data class AuditConfig(
    val id: Long,
    val operation: String,
    val state: Boolean,
    val eqId: String,
    val eqTypeId: EquipmentType,
    val isSync: Boolean,
    val userId: String
)