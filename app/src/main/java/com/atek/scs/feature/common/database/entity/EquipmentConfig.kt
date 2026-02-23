package com.atek.scs.feature.common.database.entity

import com.atek.scs.utils.EquipmentType
import com.atek.scs.utils.ServiceMode
import com.google.gson.annotations.SerializedName

data class EquipmentConfig(
    val id: Long = 0,
    @SerializedName("eq_type_id") val eqTypeId: Long,
    @SerializedName("eq_mode_id") val eqModeId: Long,
    @SerializedName("eq_current_mode_id") val currentModeId: Long,
    @SerializedName("eq_role_id") val eqRole: Long,
    @SerializedName("eq_num") val eqNum: Long,
    @SerializedName("eq_id") val eqId: String,
    @SerializedName("eq_location_id") val eqLocationId: Long,
    @SerializedName("cord_x") val cordX: Double,
    @SerializedName("cord_y") val cordY: Double,
    @SerializedName("status") val status: Boolean,
    @SerializedName("stn_id") val stnId: Long,
    @SerializedName("stn_name") val stnName: String,
    @SerializedName("is_connected") val isConnected: Boolean,
    @SerializedName("ip_address") val ipAddress: String,
    @SerializedName("eq_version") val eqVersion: Long,
    @SerializedName("service_mode") var serviceMode: ServiceMode? = null,
    @SerializedName("entry_exit_override") var entryExitOverride: Boolean = false,
    @SerializedName("excess_time_override") var excessTimeOverride: Boolean = false,
    @SerializedName("excess_fare_override") var excessFareOverride: Boolean = false,
    @SerializedName("fare_override_one") var fareOverrideOne: Boolean = false,
    @SerializedName("fare_override_two") var fareOverrideTwo: Boolean = false
) {

   fun getName(): String {
       val id = eqId.substring(4, 6)
       return when(EquipmentType.get(eqTypeId)) {
           EquipmentType.AG -> "AG $id"
           EquipmentType.TOM -> "TOM $id"
           EquipmentType.TVM -> "TVM $id"
           EquipmentType.SCS -> "SCS $id"
           EquipmentType.TR -> "TR $id"
       }
   }


}