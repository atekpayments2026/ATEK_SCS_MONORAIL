package com.atek.scs.feature.common.activity

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.atek.scs.feature.common.database.service.ConfigService
import com.atek.scs.feature.common.database.entity.Config
import com.atek.scs.feature.common.database.toConfig
import com.atek.scs.service.MaintenanceService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ScreenModel {

    var config: Config? by mutableStateOf(null)
    var isFirmwareUpdateAvailable: Boolean by mutableStateOf(false)
    var ccsStatus: Boolean by mutableStateOf(false)
    var isLoading = mutableStateOf(false)

    companion object {
        var instance: MainViewModel? = null
            private set
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onCreate() {
        // SET INSTANCE
        instance = this

        screenModelScope.launch {
            // MONITOR FOR CONFIG CHANGES
            launch(Dispatchers.IO) {
                ConfigService.getConfigFlow()
                    .collectLatest {
                        config = it
                            .executeAsOneOrNull()
                            ?.toConfig()
                    }
            }

            // MONITORING SERVICES
            launch(Dispatchers.IO) {
                MaintenanceService.getInstance().start()
            }

            launch(Dispatchers.Main) {
                MaintenanceService.getInstance().connectivityFlow.collectLatest {
                    ccsStatus = it
                }
            }
        }
    }

    override fun onDispose() {
        super.onDispose()
    }

}