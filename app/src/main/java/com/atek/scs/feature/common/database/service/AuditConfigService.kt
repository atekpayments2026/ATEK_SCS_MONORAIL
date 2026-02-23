package com.atek.scs.feature.common.database.service

import com.atek.scs.feature.common.database.AppDatabase
import com.atek.scs.feature.common.database.entity.AuditConfig
import com.atek.scs.feature.common.database.toTable

object AuditConfigService {

    private val dao = AppDatabase.auditQueries

    fun AuditConfig.saveOrUpdate() = dao.saveOrUpdate(toTable())

}