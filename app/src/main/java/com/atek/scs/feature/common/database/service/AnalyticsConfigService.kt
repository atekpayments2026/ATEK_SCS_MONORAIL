package com.atek.scs.feature.common.database.service

import app.cash.sqldelight.coroutines.asFlow
import com.atek.scs.feature.common.database.AppDatabase
import com.atek.scs.feature.common.database.entity.AnalyticsConfig
import com.atek.scs.feature.common.database.toTable

object AnalyticsConfigService {

    private val dao = AppDatabase.analyticsQueries

    fun AnalyticsConfig.saveOrUpdate() = dao.saveOrUpdate(toTable())

    fun getSummaryByMediaTypeFlow() = dao.getSummaryByMediaType().asFlow()

    fun deleteAll() = dao.deleteAll()

}