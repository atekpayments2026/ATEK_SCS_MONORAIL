package com.atek.scs.feature.common.database.service

import com.atek.scs.feature.common.database.AppDatabase
import com.atek.scs.feature.common.database.entity.Config
import com.atek.scs.feature.common.database.entity.EquipmentConfig
import com.atek.scs.feature.common.database.entity.UserConfig
import com.atek.scs.feature.common.database.service.ConfigService.saveOrUpdate
import com.atek.scs.feature.common.database.service.EquipmentConfigService.saveOrUpdate
import com.atek.scs.feature.common.database.service.UserConfigService.saveOrUpdate

object CommonDatabaseService {

    fun saveAllConfig(
        config: Config,
        equipments: List<EquipmentConfig>?,
        users: List<UserConfig>?
    ) = AppDatabase.configurationQueries.transaction {

        ConfigService.deleteAll()
        config.saveOrUpdate()

        EquipmentConfigService.deleteAll()
        equipments?.forEach { it.saveOrUpdate() }

        UserConfigService.deleteAll()
        users?.forEach { it.saveOrUpdate() }

    }

}