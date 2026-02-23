package com.atek.scs.feature.common.database

import android.content.Context
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.atek.scs.Database
import com.atek.scs.feature.common.database.DatabaseAdapters.instantAdapter
import com.atek.scs.feature.common.database.DatabaseAdapters.localDateAdapter
import com.atek.scs.feature.common.database.DatabaseAdapters.serviceModeAdapter
import com.atek.scs.feature.common.database.service.ConfigService
import com.atek.scs.feature.common.system.databaseFolder
import com.atek.scs.utils.DateFormats.FILE_DATE_FORMAT
import com.atek.scs.utils.addDays
import com.atek.scs.utils.date
import com.atek.scs.utils.toDateString
import database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.tinylog.Logger
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

/**
 * A singleton object responsible for managing the application's database instance,
 * its lifecycle, and maintenance operations like backups and cleanup.
 */
object AppDatabase {

    private const val DB_NAME = "SCS_DB.db"

    private lateinit var driver: AndroidSqliteDriver
    lateinit var db: Database
        private set

    val configurationQueries: ConfigTableQueries by lazy { db.configTableQueries }
    val equipmentQueries: EquipmentTableQueries by lazy { db.equipmentTableQueries }
    val userQueries: UserTableQueries by lazy { db.userTableQueries }
    val auditQueries: AuditTableQueries by lazy { db.auditTableQueries }
    val analyticsQueries: AnalyticsTableQueries by lazy { db.analyticsTableQueries }

    /**
     * Initializes the database driver and creates the database instance with all necessary adapters.
     * Thread-safe. Must be called once on app startup.
     */
    fun init(context: Context) {
        synchronized(this) {
            if (!::driver.isInitialized) {

                driver = AndroidSqliteDriver(
                    schema = Database.Schema,
                    context = context,
                    name = DB_NAME // e.g. "app.db"
                )

                db = Database(
                    driver = driver,
                    AnalyticsTableAdapter = AnalyticsTable.Adapter(
                        createdAtAdapter = instantAdapter,
                        updatedAtAdapter = instantAdapter
                    ),
                    AuditTableAdapter = AuditTable.Adapter(
                        createdAtAdapter = instantAdapter,
                        updatedAtAdapter = instantAdapter
                    ),
                    EquipmentTableAdapter = EquipmentTable.Adapter(
                        serviceModeAdapter = serviceModeAdapter
                    ),
                    UserTableAdapter = UserTable.Adapter(
                        dateOfBirthAdapter = localDateAdapter
                    )
                )
            }
        }
    }


    /**
     * Creates a backup of the current database file.
     * This operation is performed on an IO thread.
     *
     * @param isDailyMaintenance If true, the backup is for the previous day.
     */
    suspend fun backup(isDailyMaintenance: Boolean = false) = withContext(Dispatchers.IO) {
        Logger.debug{"Database backup initiated."}
        val config = ConfigService.getConfig() ?: run {
            Logger.error{"Configuration not found. Cannot create backup."}
            return@withContext
        }
        val databaseFolder = databaseFolder()
        val backupFileName = if (isDailyMaintenance) {
            val date = System.currentTimeMillis().addDays(-1).toDateString(FILE_DATE_FORMAT)
            "GATE_${config.eqId}_${date}.backup"
        } else {
            "GATE_${config.eqId}_${date(FILE_DATE_FORMAT)}.backup"
        }
        val backupFile = databaseFolder.resolve(backupFileName)
        val databaseFile = databaseFolder.resolve(DB_NAME)

        if (databaseFile.exists()) {
            databaseFile.copyTo(backupFile, overwrite = true)
            Logger.debug { "Database backed up successfully to: ${backupFile.name}" }
        } else {
            Logger.error { "Database file not found at ${databaseFile.path}. Backup failed." }
        }
    }

    /**
     * Deletes backup files older than a specified number of days to manage storage.
     * This operation is performed on an IO thread.
     */
    /**
     * Deletes backup files older than a specified number of days.
     * Compatible with all Android versions.
     */
    suspend fun deleteOlderBackupFiles(daysToKeep: Int = 3) = withContext(Dispatchers.IO) {
        Logger.debug { "Deleting backup files older than $daysToKeep days." }
        val databaseFolder = databaseFolder()

        // Use legacy Calendar and SimpleDateFormat
        val today = Calendar.getInstance()
        val sdf = SimpleDateFormat(FILE_DATE_FORMAT, Locale.getDefault())

        databaseFolder.listFiles()
            ?.filter { it.isFile && it.extension == "backup" && it.name != DB_NAME }
            ?.forEach { file ->
                try {
                    val dateString = file.name
                        .split("_")
                        .getOrNull(2)
                        ?.removeSuffix(".backup")

                    if (dateString != null) {
                        val fileDate: Date? = sdf.parse(dateString)
                        if (fileDate != null) {
                            val diffInMillies = today.timeInMillis - fileDate.time
                            val diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillies)

                            if (diffInDays > daysToKeep) {
                                if (file.delete()) {
                                    Logger.debug { "Deleted old backup file: ${file.name}" }
                                } else {
                                    Logger.warn { "Failed to delete backup file: ${file.name}" }
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    Logger.error(e) { "Error processing or deleting backup file: ${file.name}" }
                }
            }
    }
//    @RequiresApi(Build.VERSION_CODES.O)
//    suspend fun deleteOlderBackupFiles(daysToKeep: Int = 3) = withContext(Dispatchers.IO) {
//        Logger.debug{ "Deleting backup files older than $daysToKeep days." }
//        val databaseFolder = databaseFolder()
//        val today = JavaLocalDate.now()
//        val formatter = DateTimeFormatter.ofPattern(FILE_DATE_FORMAT)
//
//        databaseFolder.listFiles()
//            ?.filter { it.isFile && it.extension == "backup" && it.name != DB_NAME }
//            ?.forEach { file ->
//                try {
//                    val dateString = file.name
//                        .split("_")
//                        .getOrNull(2)
//                        ?.removeSuffix(".backup")
//                    if (dateString != null) {
//                        val fileDate = JavaLocalDate.parse(dateString, formatter)
//                        if (ChronoUnit.DAYS.between(fileDate, today) > daysToKeep)
//                            if (file.delete()) Logger.debug { "Deleted old backup file: ${file.name}" }
//                            else Logger.warn { "Failed to delete backup file: ${file.name}" }
//                    }
//                } catch (e: Exception) {
//                    Logger.error(e) { "Error processing or deleting backup file: ${file.name}" }
//                }
//            }
//    }

    /**
     * Performs database cleanup by vacuuming and reindexing to optimize performance.
     * This operation is performed on an IO thread.
     */
    suspend fun cleanUp() = withContext(Dispatchers.IO) {
        Logger.info { "Starting database cleanup." }
        try {
            driver.execute(null, "VACUUM;", 0)
            Logger.debug { "Database vacuumed successfully." }
        } catch (e: Exception) {
            Logger.error(e) { "Error during VACUUM operation." }
        }
        try {
            driver.execute(null, "REINDEX;", 0)
            Logger.debug { "Database reindexed successfully." }
        } catch (e: Exception) {
            Logger.error(e) { "Error during REINDEX operation." }
        }
    }

}