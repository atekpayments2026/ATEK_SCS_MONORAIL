package com.atek.scs.feature.common.database

import com.atek.scs.feature.common.database.entity.AnalyticsConfig
import com.atek.scs.feature.common.database.entity.AuditConfig
import com.atek.scs.feature.common.database.entity.Config
import com.atek.scs.feature.common.database.entity.EquipmentConfig
import com.atek.scs.feature.common.database.entity.UserConfig
import com.atek.scs.utils.EquipmentType
import com.atek.scs.utils.ServiceMode
import database.*
import java.time.Instant
import java.time.LocalDate


fun AnalyticsConfig.toTable() = AnalyticsTable(
    id = this.id,
    equipmentId = this.equipmentId,
    passId = this.passId,
    productId = this.productId,
    mediaTypeId = this.mediaTypeId,
    entryCount = this.entryCount,
    exitCount = this.exitCount,
    totalCount = this.totalCount,
    createdAt = Instant.now(),
    updatedAt = Instant.now()
)

fun AnalyticsTable.toConfig() = AnalyticsConfig(
    id = this.id,
    equipmentId = this.equipmentId,
    passId = this.passId,
    productId = this.productId,
    mediaTypeId = this.mediaTypeId,
    entryCount = this.entryCount,
    exitCount = this.exitCount,
    totalCount = this.totalCount
)

fun Config.toTable() = ConfigTable(
    id = this.id,
    equipmentTypeId = this.eqTypeId,
    equipmentNumber = this.eqNum,
    equipmentId = this.eqId,
    stationId = this.stnId,
    stationName = this.stnName,
    loginUser = this.loginUser,
    status = this.status,
    version = this.version,
)

fun ConfigTable.toConfig() = Config(
    id = this.id,
    eqTypeId = this.equipmentTypeId,
    eqNum = this.equipmentNumber,
    status = this.status,
    eqId = this.equipmentId,
    stnId = this.stationId,
    stnName = this.stationName,
    loginUser = this.loginUser,
    version = this.version,
)

fun EquipmentConfig.toTable() = EquipmentTable(
    id = this.id,
    equipmentId = this.eqId,
    equipmentNumber = this.eqNum,
    equipmentTypeId = this.eqTypeId,
    equipmentModeId = this.eqModeId,
    currentModeId = this.currentModeId,
    equipmentRole = this.eqRole,
    locationId = this.eqLocationId,
    coordinateX = this.cordX,
    coordinateY = this.cordY,
    status = this.status,
    ipAddress = this.ipAddress,
    isConnected = this.isConnected,
    stationId = this.stnId,
    stationName = this.stnName,
    equipmentVersion = this.eqVersion,
    serviceMode = this.serviceMode ?: ServiceMode.IN_SERVICE,
    isEntryExitOverride = this.entryExitOverride,
    isExcessTimeOverride = this.excessTimeOverride,
    isExcessFareOverride = this.excessFareOverride,
    isFareOverrideOne = this.fareOverrideOne,
    isFareOverrideTwo = this.fareOverrideTwo
)

fun EquipmentTable.toConfig () = EquipmentConfig(
    id = this.id,
    eqId = this.equipmentId,
    eqNum = this.equipmentNumber,
    eqTypeId = this.equipmentTypeId,
    eqModeId = this.equipmentModeId,
    currentModeId = this.currentModeId,
    eqRole = this.equipmentRole,
    eqLocationId = this.locationId,
    cordX = this.coordinateX,
    cordY = this.coordinateY,
    status = this.status,
    ipAddress = this.ipAddress,
    isConnected = this.isConnected,
    stnId = this.stationId,
    stnName = this.stationName,
    eqVersion = this.equipmentVersion,
    serviceMode = this.serviceMode,
    entryExitOverride = this.isEntryExitOverride,
    excessTimeOverride = this.isExcessTimeOverride,
    excessFareOverride = this.isExcessFareOverride,
    fareOverrideOne = this.isFareOverrideOne,
    fareOverrideTwo = this.isFareOverrideTwo
)

fun UserConfig.toTable() = UserTable(
    id = this.id,
    userId = this.userId,
    firstName = this.firstName,
    middleName = this.middleName,
    lastName = this.lastName,
    mobileNumber = this.empMobile.toString(),
    email = this.empEmail,
    dateOfBirth = LocalDate.parse(this.empDob),
    username = this.userLogin,
    password = this.userPwd,
    isActive = this.status
)

fun UserTable.toConfig() = UserConfig(
    id = this.id,
    userId = this.userId,
    firstName = this.firstName,
    middleName = this.middleName,
    lastName = this.lastName,
    empMobile = this.mobileNumber.toLong(),
    empEmail = this.email,
    empDob = this.dateOfBirth.toString(),
    userLogin = this.username,
    userPwd = this.password,
    status = this.isActive,
)

fun AuditConfig.toTable() = AuditTable(
    id = this.id,
    operation = this.operation,
    state = if (this.state) 1 else 0,
    equipmentId = this.eqId,
    equipmentTypeId = this.eqTypeId.id,
    isSynced = this.isSync,
    userId = this.userId,
    createdAt = Instant.now(),
    updatedAt = Instant.now(),
)

fun AuditTable.toConfig() = AuditConfig(
    id = this.id,
    operation = this.operation,
    state = this.state == 1L,
    eqId = this.equipmentId,
    eqTypeId = EquipmentType.get(this.equipmentTypeId),
    isSync = this.isSynced,
    userId = this.userId
)