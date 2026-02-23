package com.atek.scs.feature.domain.configuration.model

import com.atek.scs.feature.common.database.entity.EquipmentConfig
import com.atek.scs.feature.common.database.entity.Config
import com.atek.scs.feature.common.database.entity.UserConfig
import com.google.gson.annotations.SerializedName

data class ConfigResponse(
    var status: Boolean,
    val error: String? = null,
    val data: ConfigData? = null,
) {
    data class ConfigData(
        @SerializedName("activation_time")
        val activationTime: Long? = null,
        val config: Config,
        val equipments: List<EquipmentConfig>? = null,
        val users: List<UserConfig>? = null,
    )
}
