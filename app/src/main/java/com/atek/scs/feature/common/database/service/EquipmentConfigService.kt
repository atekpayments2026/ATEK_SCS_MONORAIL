package com.atek.scs.feature.common.database.service

import app.cash.sqldelight.coroutines.asFlow
import com.atek.scs.feature.common.database.AppDatabase
import com.atek.scs.feature.common.database.entity.EquipmentConfig
import com.atek.scs.feature.common.database.toConfig
import com.atek.scs.feature.common.database.toTable

object EquipmentConfigService {

    private val dao = AppDatabase.equipmentQueries

    fun EquipmentConfig.saveOrUpdate() = dao.saveOrUpdate(toTable())
    fun saveAll(equipments: List<EquipmentConfig>) {
        dao.transaction {
            equipments.forEach { it.saveOrUpdate() }
        }
    }

    fun getAllFlow() = dao.getAll().asFlow()

    fun findFirstByEqId(eqId: String) = dao.getById(eqId).executeAsOneOrNull()?.toConfig()

    fun getAll() = dao.getAll().executeAsList().map { it.toConfig() }

    fun deleteAll() = dao.deleteAll()

}