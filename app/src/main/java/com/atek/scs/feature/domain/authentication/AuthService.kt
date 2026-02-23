package com.atek.scs.feature.domain.authentication

import com.atek.scs.feature.common.database.AppDatabase
import com.atek.scs.feature.common.database.entity.Config
import com.atek.scs.feature.common.database.service.ConfigService
import com.atek.scs.feature.common.database.service.ConfigService.saveOrUpdate
import com.atek.scs.utils.AuditOperations

object AuthService {

    fun login(userName: String) = AppDatabase.configurationQueries.transaction {

        // SAVE USER DETAILS
        val config = ConfigService.getConfig()!!
        config.loginUser = userName
        config.loginTime = System.currentTimeMillis()
        config.saveOrUpdate()

        // UPDATE THE AUDIT LOG
        updateAuditConfig(
            config,
            AuditOperations.LOGIN,
        )

    }

    fun isUserLoggedIn() = ConfigService.getConfig()?.loginUser != null

    fun getCurrentUser(): String {
        val config = ConfigService.getConfig()
            ?: error("SCS not configured!")
        return config.loginUser ?: error("Please login first!")
    }

    fun logout() = AppDatabase.configurationQueries.transaction {

        // GET CURRENT CONFIG
        val config = ConfigService.getConfig()
            ?: error("SCS not configured!")

        // UPDATE THE AUDIT LOG
        updateAuditConfig(
            config,
            AuditOperations.LOGOUT,
        )

        // UPDATE LOGIN INFO
        config.loginUser = null
        config.loginTime = System.currentTimeMillis()
        config.saveOrUpdate()

    }

    fun updateAuditConfig(
        config: Config,
        operations: AuditOperations
    ) {
        /*AuditConfig(
            operation = operations,
            userId = config.loginUser ?: error("Please login first!"),
            timestamp = date()
        ).saveOrUpdate()*/
    }

    fun updateAuditConfig(
        operations: AuditOperations
    ) {

        // GET CURRENT CONFIG
        val config = ConfigService.getConfig()
            ?: error("SCS not configured!")

        // UPDATE THE AUDIT LOG
        /*AuditConfig(
            operation = operations,
            userId = config.loginUser ?: error("Please login first!"),
            timestamp = date()
        ).saveOrUpdate()*/

    }

}