package com.atek.scs.feature.common.activity

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

    fun onCreate() {
        screenModelScope.launch {

            // SET INSTANCE
            instance = this@MainViewModel

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
                MaintenanceService.getInstance {
                    withContext(Dispatchers.Main) {
                        ccsStatus = it
                    }
                }.start()
            }
        }
    }

    override fun onDispose() {
        super.onDispose()
    }

}