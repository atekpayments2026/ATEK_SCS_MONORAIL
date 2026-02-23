package com.atek.scs.feature.common.database

import android.os.Build
import androidx.annotation.RequiresApi
import app.cash.sqldelight.ColumnAdapter
import com.atek.scs.utils.ServiceMode
import java.time.Instant
import java.time.LocalDate

/**
 * A collection of all custom ColumnAdapters required by the application's database.
 * These adapters handle the encoding and decoding between custom Kotlin types and
 * the primitive types that can be stored in a SQL database.
 */
object DatabaseAdapters {

    /**
     * Adapter for the [ServiceMode] enum.
     * Encodes the enum to its string 'name' for storage in a TEXT column.
     * Decodes the string from the database back into the corresponding [ServiceMode] enum constant.
     */
    val serviceModeAdapter = object : ColumnAdapter<ServiceMode, String> {
        override fun decode(databaseValue: String): ServiceMode {
            // Converts the TEXT from the DB back to the ServiceMode enum.
            // This will throw an IllegalArgumentException if the name is not found,
            // which helps enforce data integrity.
            return ServiceMode.valueOf(databaseValue)
        }

        override fun encode(value: ServiceMode): String {
            // Converts the ServiceMode enum to its name (e.g., "IN_SERVICE") for storage.
            return value.name
        }
    }

    /**
     * Adapter for the [kotlinx.datetime.Instant] type.
     * Encodes an Instant to its standard ISO 8601 string representation for storage in a TEXT column.
     * Decodes the string from the database back into an [Instant] object.
     */
    val instantAdapter = object : ColumnAdapter<Instant, String> {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun decode(databaseValue: String): Instant {
            return Instant.parse(databaseValue)
        }
        override fun encode(value: Instant): String {
            return value.toString()
        }
    }

    /**
     * Adapter for the [kotlinx.datetime.LocalDate] type.
     * Encodes a LocalDate to its standard YYYY-MM-DD string representation for storage in a TEXT column.
     * Decodes the string from the database back into a [LocalDate] object.
     */
    val localDateAdapter = object : ColumnAdapter<LocalDate, String> {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun decode(databaseValue: String): LocalDate {
            return LocalDate.parse(databaseValue)
        }
        override fun encode(value: LocalDate): String {
            return value.toString()
        }
    }
}