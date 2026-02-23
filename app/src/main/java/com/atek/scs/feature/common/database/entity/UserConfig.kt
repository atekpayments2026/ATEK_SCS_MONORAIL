package com.atek.scs.feature.common.database.entity

import com.google.gson.annotations.SerializedName

data class UserConfig(
    var id: Long = 0,
    @SerializedName("user_id") val userId: Long,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("middle_name") val middleName: String?,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("emp_mobile") val empMobile: Long,
    @SerializedName("emp_email") val empEmail: String,
    @SerializedName("emp_dob") val empDob: String,
    @SerializedName("user_login") val userLogin: String,
    @SerializedName("user_pwd") val userPwd: String,
    val status: Boolean
)