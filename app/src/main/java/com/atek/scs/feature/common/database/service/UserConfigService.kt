package com.atek.scs.feature.common.database.service

import com.atek.scs.feature.common.database.AppDatabase
import com.atek.scs.feature.common.database.entity.UserConfig
import com.atek.scs.feature.common.database.toTable

object UserConfigService {

    private val dao = AppDatabase.userQueries

    fun UserConfig.saveOrUpdate() = dao.saveOrUpdate(toTable())

    fun deleteAll() = dao.deleteAll()

    fun findByUsername(username: String) = dao
        .getByUsername(username)
        .executeAsOneOrNull()

}