package com.atek.scs.feature.common.system

import android.content.Context
import com.atek.scs.feature.common.database.AppDatabase
import org.tinylog.Logger
import java.io.FileNotFoundException

private val envData: HashMap<String, String> = HashMap()

/**
 * Enum class defining possible environment variables.
 */
enum class Environment {
    ENV,
    LOG_LEVEL,
    PRINTER_NAME,
    SCANNER_PORT,
    ATEK_CCS_BASE_URL,
    ATEK_QR_BASE_URL,
    SBI_UPI_BASE_URL
}

/**
 * Custom exception for missing env.properties file.
 */
class EnvironmentFileNotFoundException(filePath: String) : FileNotFoundException(
    "Environment file not found at: $filePath. Please create a env.properties file in the project root."
)

/**
 * Loads the environment variables from a env.properties file into memory.
 * Throws an exception if the env.properties file is missing.
 */
fun loadEnv(context: Context) {
    initFolders()
    envData.clear()
    AppDatabase.init(context)


    val lines = try {
        context.assets.open("env.properties").bufferedReader().readLines()
    } catch (e: Exception) {
        throw EnvironmentFileNotFoundException("env.properties")
    }

    lines
        .asSequence()
        .map { it.trim() }
        .filter { it.isNotEmpty() && !it.startsWith("#") }
        .map { it.split("=", limit = 2) }
        .filter { it.size == 2 }
        .forEach { (key, value) ->
            val cleanedValue = value
                .trim()
                .removeSurrounding("'")
                .removeSurrounding("\"")
            envData[key] = cleanedValue
        }

    // Set logging level
    System.setProperty("tinylog.level", env(Environment.LOG_LEVEL))

    // Log environment variables
    Logger.trace {
        """
        ---------------------- ENVIRONMENT ----------------------
        ENV ......................... ${env(Environment.ENV)}
        LOG LEVEL ................... ${env(Environment.LOG_LEVEL)}
        PRINTER NAME ................ ${env(Environment.PRINTER_NAME)}
        SCANNER PORT ................ ${env(Environment.SCANNER_PORT)}
        CCS BASE URL ................ ${env(Environment.ATEK_CCS_BASE_URL)}
        QR BASE URL ................. ${env(Environment.ATEK_QR_BASE_URL)}
        SBI UPI BASE URL ............ ${env(Environment.SBI_UPI_BASE_URL)}
        ---------------------------------------------------------
        """.trimIndent()
    }
}

//fun loadEnv(context: Context) {
//    initFolders()
//    envData.clear()
//    AppDatabase.init(context)
//
//    val envFile = findEnvFile() ?: throw EnvironmentFileNotFoundException(".")
//
//    envFile.readLines()
//        .asSequence()
//        .map { it.trim() }
//        .filter { it.isNotEmpty() && !it.startsWith("#") }
//        .map { it.split("=", limit = 2) }
//        .filter { it.size == 2 }
//        .forEach { (key, value) ->
//            val cleanedValue = value.trim().removeSurrounding("'").removeSurrounding("\"")
//            envData[key] = cleanedValue
//        }
//
//    // Set logging level
//    System.setProperty("tinylog.level", env(Environment.LOG_LEVEL))
//
//    // Log environment variables
//    Logger.trace {
//        (
//                """
//        ---------------------- ENVIRONMENT ----------------------
//        ENV ......................... ${env(Environment.ENV)}
//        LOG LEVEL ................... ${env(Environment.LOG_LEVEL)}
//        PRINTER NAME ................ ${env(Environment.PRINTER_NAME)}
//        SCANNER PORT ................ ${env(Environment.SCANNER_PORT)}
//        CCS BASE URL ................ ${env(Environment.ATEK_CCS_BASE_URL)}
//        QR BASE URL ................. ${env(Environment.ATEK_QR_BASE_URL)}
//        SBI UPI BASE URL ............ ${env(Environment.SBI_UPI_BASE_URL)}
//        ---------------------------------------------------------
//        """.trimIndent()
//                )
//    }
//}

/**
 * Retrieves the value of an environment variable.
 * If not found, returns the specified default value.
 * Higher priority values are taken first.
 */
fun env(key: Environment, default: String = ""): String {
    return System.getenv(key.name) ?: envData[key.name] ?: default
}

/**
 * Checks if the current environment is set to "DEBUG".
 */
fun isDebugEnabled(): Boolean = env(Environment.ENV).equals("DEBUG", ignoreCase = true)

/**
 * Parses a "hostname:port" string into a Pair<String, Int>.
 */
fun String.toHostname(): Pair<String, Int> {
    val parts = this.split(":")
    require(parts.size == 2) { "Invalid format for hostname and port: $this" }
    return parts[0] to parts[1].toInt()
}