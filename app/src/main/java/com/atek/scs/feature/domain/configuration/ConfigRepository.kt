package com.atek.scs.feature.domain.configuration

import com.atek.scs.feature.common.network.ApiManager
import com.atek.scs.feature.domain.configuration.model.ConfigRequest
import com.atek.scs.feature.domain.configuration.model.ConfigResponse
import com.atek.scs.feature.domain.configuration.model.FirmwareUpdateRequest
import com.atek.scs.feature.domain.configuration.model.FirmwareUpdateResponse

object ConfigRepository {

    suspend fun getConfig(request: ConfigRequest): ConfigResponse {
        //if (BuildConfig.DEBUG) return readJsonFromAsset("debug/config_response.json")
        val response = ApiManager.getCCService().getConfig(request)
        if (!response.isSuccessful)
            throw Exception(response.errorBody()?.string() ?: "Failed to get config from server")
        if (response.body() == null)
            throw Exception("Response body is null")
        return response.body()!!
        /*return """
            {
              "status": true,
              "error": null,
              "data": {
                "activation_time": 1707052800000,
                "config": {
                  "id": 1,
                  "eq_type_id": 101,
                  "eq_mode_id": 202,
                  "eq_role_id": 303,
                  "eq_num": 1,
                  "eq_id": "EQ-12345",
                  "stn_id": 55,
                  "stn_name": "Central Station",
                  "login_user": "admin",
                  "login_time": 1707052900000,
                  "shift_id": 12,
                  "config_version": 3
                },
                "equipments": [
                  {
                    "id": 1,
                    "eq_id": "EQ-1001",
                    "eq_num": 2,
                    "eq_type_id": 1,
                    "eq_mode_id": 202,
                    "eq_current_mode_id": 203,
                    "eq_role": 1,
                    "eq_location_id": 5,
                    "cord_x": 100,
                    "cord_y": 200,
                    "is_active": true,
                    "ip_address": 3232235777,
                    "status": "IN_SERVICE",
                    "is_connected": true,
                    "stn_id": 55,
                    "stn_name": "Central Station",
                    "eq_version": 2
                  },
                  {
                    "id": 2,
                    "eq_id": "EQ-1002",
                    "eq_num": 3,
                    "eq_type_id": 2,
                    "eq_mode_id": 203,
                    "eq_current_mode_id": 204,
                    "eq_role": 2,
                    "eq_location_id": 6,
                    "cord_x": 150,
                    "cord_y": 250,
                    "is_active": true,
                    "ip_address": 3232235778,
                    "status": "OUT_OF_SERVICE",
                    "is_connected": false,
                    "stn_id": 55,
                    "stn_name": "Central Station",
                    "eq_version": 1
                  }
                ],
                "users": [
                  {
                    "id": 1,
                    "user_id": 1001,
                    "first_name": "John",
                    "middle_name": "A",
                    "last_name": "Doe",
                    "emp_mobile": 9876543210,
                    "emp_email": "john.doe@example.com",
                    "emp_dob": "1990-01-01",
                    "user_login": "1802",
                    "user_pwd": "1802",
                    "status": 1
                  },
                  {
                    "id": 2,
                    "user_id": 1002,
                    "first_name": "Jane",
                    "middle_name": null,
                    "last_name": "Smith",
                    "emp_mobile": 9123456789,
                    "emp_email": "jane.smith@example.com",
                    "emp_dob": "1985-05-10",
                    "user_login": "janesmith",
                    "user_pwd": "securepassword2",
                    "status": 1
                  }
                ]
              }
            }
        """.fromJson<ConfigResponse>()*/
    }

    suspend fun checkFirmwareUpdate(request: FirmwareUpdateRequest): FirmwareUpdateResponse {
        //if (BuildConfig.DEBUG) return readJsonFromAsset("debug/config_response.json")
        val response = ApiManager.getCCService().checkFirmwareUpdate(request)
        if (!response.isSuccessful)
            throw Exception(response.errorBody()?.string() ?: "Failed to check update from server")
        if (response.body() == null)
            throw Exception("Response body is null")
        return response.body()!!
    }

}