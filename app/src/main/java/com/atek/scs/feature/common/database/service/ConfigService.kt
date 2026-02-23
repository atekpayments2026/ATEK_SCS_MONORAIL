package com.atek.scs.feature.common.database.service

import app.cash.sqldelight.coroutines.asFlow
import com.atek.scs.feature.common.database.AppDatabase
import com.atek.scs.feature.common.database.entity.Config
import com.atek.scs.feature.common.database.toConfig
import com.atek.scs.feature.common.database.toTable

object ConfigService {

    private val dao = AppDatabase.configurationQueries

    fun Config.saveOrUpdate() = dao.saveOrUpdate(toTable())

    fun deleteAll() = dao.deleteAll()

    fun getConfig() = dao.getFirstConfig().executeAsOneOrNull()?.toConfig()

    fun getConfigFlow() = dao.getFirstConfig().asFlow()

}