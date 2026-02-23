package com.atek.scs.feature.common.database.entity

import com.google.gson.annotations.SerializedName

data class Config(
    var id: Long = 0,
    @SerializedName("eq_type_id") val eqTypeId: Long,
    @SerializedName("eq_num") val eqNum: Long,
    @SerializedName("status") val status: Boolean,
    @SerializedName("eq_id") val eqId: String,
    @SerializedName("stn_id") val stnId: Long,
    @SerializedName("stn_name") val stnName: String,
    @SerializedName("login_user") var loginUser: String? = null,
    @SerializedName("login_time") var loginTime: Long = 0,
    @SerializedName("eq_version") var version: Long,
)