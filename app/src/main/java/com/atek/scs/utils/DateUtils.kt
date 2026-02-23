package com.atek.scs.utils

import com.atek.scs.utils.DateFormats.DEFAULT_DATE_FORMAT
import java.text.SimpleDateFormat
import java.util.*

object DateFormats {
    const val DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
    const val FILE_DATE_FORMAT = "yyMMdd"
}

fun date(format: String = DEFAULT_DATE_FORMAT): String {
    val dateFormat = SimpleDateFormat(format, Locale.ENGLISH)
    dateFormat.timeZone = TimeZone.getTimeZone("GMT+5:30")
    return dateFormat.format(Calendar.getInstance().time).toString()
}

fun epochDate(format: String = DEFAULT_DATE_FORMAT): Long {
    val dateFormat = SimpleDateFormat(format, Locale.ENGLISH)
    dateFormat.timeZone = TimeZone.getTimeZone("GMT+5:30")
    return dateFormat.format(Calendar.getInstance().time).toString().toEpoch()
}

fun String.toEpoch(format: String = DEFAULT_DATE_FORMAT): Long {
    val dateFormat = SimpleDateFormat(format, Locale.ENGLISH)
    dateFormat.timeZone = TimeZone.getTimeZone("GMT+5:30")
    return dateFormat.parse(this)?.time ?: throw Exception("Failed to parse date [$this | $format]")
}

fun String.toCsaTxnDate(ced: String): Long {
    val relTxnMinute = this.toLong(16)
    val relTxnEpoch = relTxnMinute * 60
    val cedEpoch = ced.toEpoch("yyMMdd") / 1000
    return (relTxnEpoch + cedEpoch) * 1000
}

fun Long.addMinutes(minutes: Int): Long {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    calendar.add(Calendar.MINUTE, minutes)
    return calendar.timeInMillis
}

fun Long.addDays(days: Int): Long {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    calendar.add(Calendar.DAY_OF_MONTH, days)
    calendar.set(Calendar.HOUR_OF_DAY, 1)
    calendar.set(Calendar.MINUTE, 10)
    calendar.set(Calendar.SECOND, 0)
    return calendar.timeInMillis
}

fun Long.toDateString(format: String = DEFAULT_DATE_FORMAT): String {
    val dateFormat = SimpleDateFormat(format, Locale.ENGLISH)
    dateFormat.timeZone = TimeZone.getTimeZone("GMT+5:30")
    return dateFormat.format(this).toString()
}

/**
 * Set time to metro last operation time
 * @return timestamp with time set to metro last operation time
 *
 * Note: Metro last operation time is 1:10 AM. The calculation is performed in the IST (Indian Standard Time) time zone.
 */
fun Long.setDayLastMetroOperationTime(): Long {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this

    // Get the current hour and minute
    val currentCalender = Calendar.getInstance()
    val hour = currentCalender.get(Calendar.HOUR_OF_DAY)
    val minute = currentCalender.get(Calendar.MINUTE)

    // If the current time is between midnight and 1:10 AM, set the time to 1:10 AM of the same day
    if (hour == 0 || (hour == 1 && minute in 0..10)) {
        calendar.set(Calendar.HOUR_OF_DAY, 1)
        calendar.set(Calendar.MINUTE, 10)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    // Otherwise, set the time to 1:10 AM of the next day
    calendar.add(Calendar.DAY_OF_MONTH, 1)
    calendar.set(Calendar.HOUR_OF_DAY, 1)
    calendar.set(Calendar.MINUTE, 10)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.timeInMillis
}
