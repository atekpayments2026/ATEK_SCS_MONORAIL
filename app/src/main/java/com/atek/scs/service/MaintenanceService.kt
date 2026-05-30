package com.atek.scs.service

import android.os.Build
import androidx.annotation.RequiresApi
import com.atek.scs.feature.common.database.AppDatabase
import com.atek.scs.feature.common.database.service.CommonDatabaseService
import com.atek.scs.feature.common.database.service.ConfigService
import com.atek.scs.feature.common.network.ApiManager
import com.atek.scs.feature.common.system.logFolder
import com.atek.scs.feature.common.system.tempFolder
import com.atek.scs.feature.domain.configuration.ConfigRepository
import com.atek.scs.feature.domain.configuration.model.ConfigRequest
import com.atek.scs.feature.domain.configuration.model.ConfigResponse
import com.atek.scs.utils.*
import com.google.gson.internal.GsonBuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import org.tinylog.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.security.SecureRandom

class MaintenanceService private constructor() {

    private val _connectivityFlow = MutableStateFlow(false)
    val connectivityFlow: StateFlow<Boolean> = _connectivityFlow.asStateFlow()

    companion object {

        private val DELAY_PERIOD = 10 * 1000L
        private var instance: MaintenanceService? = null

        fun getInstance(): MaintenanceService {
            if (instance == null) instance = MaintenanceService()
            return instance!!
        }

    }

    /**
     * Starts the maintenance service.
     * Continuously checks connectivity and performs maintenance tasks.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun start() = withContext(Dispatchers.Default) {
        while (isActive) {
            try {
                doWork()
            } catch (e: Exception) {
                Logger.error(e)
            } finally {
                delay(DELAY_PERIOD)
            }
        }
    }

    /**
     * Runs a task in the background.
     */
    private suspend fun runTask(message: String, task: suspend () -> Unit) {
        withContext(Dispatchers.IO) {
            Logger.trace{"\n\n-------------------------------------- $message --------------------------------------"}
            try {
                task()
            } catch (e: Exception) {
                Logger.error{e.message}
            }
            Logger.trace{ "-------------------------------------------------- END --------------------------------------------------\n\n" }
        }
    }

    /**
     * Performs various maintenance tasks.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun doWork() {
        runTask("CHECKING CONNECTIVITY WITH CCS") { connectivity() }
        runTask("CHECKING FOR CONFIG UPDATE") { configUpdate() }
        runTask("APPLY AVAILABLE CONFIG UPDATE") { implConfigUpdate() }
        runTask("CHECKING FOR FIRMWARE UPDATE") { firmwareUpdate() }
        runTask("PERFORMING MONITORING DEVICE HEALTH") { deviceMonitoring() }
        runTask("PERFORMING DEVICE CLEANUP") { cleanup() }
        runTask("PERFORMING DAILY MAINTENANCE TASK") { dailyTask() }
    }

    /**
     * Checks internet connectivity and updates status.
     */
    private suspend fun connectivity() {
        val isConnected = isUrlReachable()
        _connectivityFlow.value = isConnected
        Logger.debug { "Internet connectivity is ${if (isConnected) "available" else "not available"}" }
    }

    /**
     * Checks for configuration updates from the server.
     */
    private suspend fun configUpdate() {
        val config = ConfigService.getConfig()!!
        val response = ConfigRepository.getConfig(
            ConfigRequest(
                ip = getIPAddress(),
                eqTypeId = config.eqTypeId,
                eqVersion = config.version,
            )
        )

        if (!response.status || response.data == null)
            error("Failed to get config update from server, error: ${response.error}")

        // DELETE OLD CONFIG FILES
        val tempFolder = tempFolder()
        tempFolder.listFiles()?.forEach {
            if (it.name.startsWith("config_update_")) {
                it.delete()
            }
        }

        // WRITE NEW CONFIG FILE
        val file = tempFolder.resolve("config_update_${response.data.activationTime}.json")
        if (!file.exists()) withContext(Dispatchers.IO) {
            file.createNewFile()
        }
        file.writeText(response.toJson())

    }

    /**
     * Applies configuration updates.
     */
    private suspend fun implConfigUpdate() {

        val tempFolder = tempFolder()
        val files = tempFolder.listFiles()?.filter {
            it.name.startsWith("config_update_")
        } ?: emptyList()

        // APPLY CONFIG UPDATES
        files.forEach { file ->

            val response = file.readText().fromJson<ConfigResponse>()
            if (!response.status || response.data == null)
                error("Failed to read config update from file: ${file.name}")

            // CHECK FOR ACTIVATION TIME
            if (response.data.activationTime == null)
                error("Failed to apply config update from file: ${file.name}, activation time is missing")

            // CHECK ACTIVATION TIME IS LESS THAN CURRENT TIME
            if (response.data.activationTime > System.currentTimeMillis())
                error("Failed to apply config update from file: ${file.name}, activation time is in future")

            // UPDATE CONFIGS
            CommonDatabaseService
                .saveAllConfig(
                    config = response.data.config,
                    equipments = response.data.equipments,
                    users = response.data.users
                )

            // CLEAN UP DATABASE
            AppDatabase.cleanUp()

            // DELETE CONFIG UPDATE FILE
            file.delete()

        }

    }

    /**
     * Checks for firmware updates from the server.
     */
    private suspend fun firmwareUpdate() {


    }

    /**
     * Installs firmware updates.
     */
    fun implFirmwareUpdate() {
        val tempFolder = tempFolder()
        val files = tempFolder.listFiles()?.filter {
            it.name.startsWith("firmware_update_")
        } ?: emptyList()
        files.forEach { file ->
            //
        }
    }

    fun hasFirmwareUpdate(): Boolean {
        val tempFolder = tempFolder()
        val files = tempFolder.listFiles()?.filter {
            it.name.startsWith("firmware_update_")
            val version = it.name.split("_")[2].replace(".apk", "")
            version != GsonBuildConfig.VERSION
        } ?: emptyList()
        return files.isNotEmpty()
    }

    fun deleteFirmwareUpdateFiles() {
        tempFolder().listFiles()?.forEach {
            if (it.name.startsWith("firmware_update_")) {
                val version = it.name.split("_")[2].replace(".apk", "")
                if (version == GsonBuildConfig.VERSION) {
                    it.delete()
                }
            }
        }
    }

    fun deleteAllTempFilesOlderThen24Hours() {
        tempFolder().listFiles()
            ?.forEach {
                val createdAt = it.lastModified()
                val currentTime = System.currentTimeMillis()
                val diff = currentTime - createdAt
                val hours = diff / (60 * 60 * 1000)
                if (hours > 24) {
                    it.delete()
                }
            }
    }

    /**
     * Monitors device health by checking storage and date-time settings.
     */
    private fun deviceMonitoring() {
        /*val availableStorage = StatFs(Environment.getExternalStorageDirectory().path)
            .availableBytes / 1024 / 1024
        if (availableStorage < 100) {
            AlertDialog.showNotThreadSafe(
                type = AlertDialog.Type.ERROR,
                message = "Low storage space available, please contact AFC!"
            )
        }
        if (date().contains("1970")) {
            AlertDialog.showNotThreadSafe(
                type = AlertDialog.Type.ERROR,
                message = "Device date time is reset, please contact AFC!"
            )
        }*/
    }

    /**
     * Cleans up old files and backups.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun cleanup() {
        AppDatabase
            .deleteOlderBackupFiles()
        val logFolder = logFolder()
        deleteFirmwareUpdateFiles()
        deleteAllTempFilesOlderThen24Hours()
        logFolder.listFiles()?.forEach {
            try {
                val name = it.name
                if (name == "latest.txt") return@forEach
                val date = name.split("_")[1].split(".")[0]
                if (date.toInt() < date(DateFormats.FILE_DATE_FORMAT).toInt() - 7) {
                    it.delete()
                }
            } catch (e: Exception) {
                Logger.error(e) { it.name }
                it.delete()
            }
        }
    }

    /**
     * Performs daily maintenance tasks.
     */
    private suspend fun dailyTask() {
        if (date("HH:mm") == "02:00") doDailyMaintenance()
    }

    /**
     * Executes daily maintenance tasks such as cleaning and backing up the database.
     */
    private suspend fun doDailyMaintenance() {
        runTask("Manual end of shift!") { processManualEndOfShift() }
        AppDatabase.apply {
            runTask("Backing up database ...") { backup() }
            runTask("Cleaning up database ...") { cleanUp() }
        }
    }

    /**
     * Processes manual end of shift.
     */
    private fun processManualEndOfShift() {
        /*if (MainViewModel.instance
                ?.config
                ?.loginUser != null
        ) {
            Logger.trace("Manual end of shift initiated.")
            MainViewModel.instance
                ?.manualLogout()
        }*/
    }

    /**
     * Checks if a given URL is reachable.
     */
    private suspend fun isUrlReachable(): Boolean {
        return try {
            val response = ApiManager
                .getFastCCService()
                .checkStatus()
            
            val isSuccess = response.isSuccessful
            Logger.debug { "CCS Status Check: ${response.code()} - isSuccess: $isSuccess" }
            isSuccess
        } catch (e: Exception) {
            Logger.error { "CCS Connectivity Error: ${e.message}" }
            false
        }
    }


}
