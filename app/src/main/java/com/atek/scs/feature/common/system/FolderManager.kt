package com.atek.scs.feature.common.system

import java.io.File

/**
 * System Folder Initialization Utility
 *
 * This file provides a utility for initializing and managing the folder structure
 * required by the application. It ensures that essential directories such as those
 * for logs, databases, temporary files, and configuration are created and accessible.
 *
 * ## Folder Structure:
 * - **BASE_FOLDER**: Root directory for all configuration files and resources.
 * - **DATABASE_FOLDER**: Subdirectory under BASE_FOLDER for storing database files.
 * - **LOG_FOLDER**: Subdirectory under BASE_FOLDER for storing log files.
 * - **READER_UPDATE_ENTRY_FOLDER**: Subdirectory for storing entry reader updates.
 * - **READER_UPDATE_EXIT_FOLDER**: Subdirectory for storing exit reader updates.
 * - **TEMP_FOLDER**: Subdirectory under BASE_FOLDER for storing temporary files.
 *
 * ## System Properties:
 * - **tinylog.directory**: Set to the log folder path for configuring Tiny Logger's output.
 */

private const val BASE_FOLDER = "configuration"
private const val DATABASE_FOLDER = "$BASE_FOLDER/database"
private const val LOG_FOLDER = "$BASE_FOLDER/logs"
private const val TEMP_FOLDER = "$BASE_FOLDER/temp"

/**
 * Initializes the application's folder structure and configures system properties.
 *
 * Ensures that all necessary directories are created under the BASE_FOLDER. This includes
 * directories for logs, databases, temporary files, and reader updates. Additionally, the
 * function sets the "tinylog.directory" system property to direct log outputs to the specified
 * log folder.
 */
fun initFolders() {

    // Ensure the base directory exists
    File(BASE_FOLDER).apply { if (!exists()) mkdirs() }

    // Ensure the log directory exists and configure logging
    File(LOG_FOLDER).apply {
        if (!exists()) mkdirs()
        System.setProperty("tinylog.directory", absolutePath)
    }

    // Ensure the temporary files directory exists
    File(TEMP_FOLDER).apply { if (!exists()) mkdirs() }

    // Ensure the database directory exists
    File(DATABASE_FOLDER).apply { if (!exists()) mkdirs() }

}

/**
 * Retrieves the application's base folder.
 *
 * @return File object pointing to the BASE_FOLDER directory.
 */
fun appFolder() = File(BASE_FOLDER)

/**
 * Retrieves the application's log folder.
 *
 * @return File object pointing to the LOG_FOLDER directory.
 */
fun logFolder() = File(LOG_FOLDER)

/**
 * Retrieves the application's temporary folder.
 *
 * @return File object pointing to the TEMP_FOLDER directory.
 */
fun tempFolder() = File(TEMP_FOLDER)

/**
 * Retrieves the application's database folder.
 *
 * @return File object pointing to the DATABASE_FOLDER directory.
 */
fun databaseFolder() = File(DATABASE_FOLDER)
