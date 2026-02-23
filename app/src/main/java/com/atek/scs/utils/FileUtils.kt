package com.atek.scs.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.reflect.Type
import java.util.*

/**
 * Global Gson instance.
 */
val gson: Gson = GsonBuilder().create()

/**
 * Convert an object to a JSON string.
 * @return The JSON string.
 */
fun Any.toJson(): String = gson.toJson(this)

/**
 * Convert a JSON string to an object.
 * @return The object.
 */
inline fun <reified T> String.fromJson(): T = gson.fromJson(this, T::class.java)

/**
 * Convert a JSON string to an object.
 * @param type The type of the object.
 * @return The object.
 */
inline fun <reified T> String.fromJson(type: Type): T = gson.fromJson(this, type)

/**
 * Read a JSON file from the assets folder.
 * @param fileName The name of the file.
 * @return The object.
 */
inline fun <reified T : Any> readJsonFromAsset(fileName: String): T {
    val i: InputStream = File(fileName).inputStream()
    val br = BufferedReader(InputStreamReader(i))
    return Gson().fromJson(br, T::class.java)
}

/**
 * Read a JSON file from the file system.
 * @param fileLocation The location of the file.
 * @return The object.
 */
inline fun <reified T> jsonReader(fileLocation: String): T {
    val file = File(fileLocation)
    return Gson().fromJson(file.readText(), T::class.java)
}


fun Long.toFileSize(): String {
    val kb = 1024
    val mb = kb * 1024
    val gb = mb * 1024
    return when {
        this >= gb -> String.format(Locale.ENGLISH, "%.2f GB", this.toDouble() / gb)
        this >= mb -> String.format(Locale.ENGLISH, "%.2f MB", this.toDouble() / mb)
        this >= kb -> String.format(Locale.ENGLISH, "%.2f KB", this.toDouble() / kb)
        else -> String.format(Locale.ENGLISH, "%d bytes", this)
    }
}

