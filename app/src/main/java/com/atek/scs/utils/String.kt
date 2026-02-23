package com.atek.scs.utils

import org.tinylog.Logger
import java.math.BigInteger
import kotlin.math.ceil


/**
 * Convert a hexadecimal string to a binary coded decimal (BCD) array.
 * @param padLeft If true, the input string will be padded with a zero on the left if its length is odd.
 * @return The BCD array.
 */
fun String.toBCD(padLeft: Boolean = true): ByteArray {

    // Helper function to convert a character byte to its integer value
    fun charToInt(b: Byte): Int {
        return when (b) {
            in 'a'.code.toByte()..'z'.code.toByte() -> b - 'a'.code.toByte() + 0x0A
            in 'A'.code.toByte()..'Z'.code.toByte() -> b - 'A'.code.toByte() + 0x0A
            else -> b - '0'.code.toByte()
        }
    }

    // Pad the input string with '0' on the left if its length is odd and padLeft is true
    val paddedInput = if (this.length % 2 != 0) {
        if (padLeft) "0$this" else "${this}0"
    } else {
        this
    }

    // Convert the padded input string to a byte array
    val bytes = paddedInput.toByteArray()
    // Create a BCD byte array of half the length of the input string
    val bcd = ByteArray(bytes.size / 2)

    // Convert the input string to BCD format
    for (i in bcd.indices) {
        val highNibble = charToInt(bytes[2 * i])
        val lowNibble = charToInt(bytes[2 * i + 1])
        bcd[i] = ((highNibble shl 4) + lowNibble).toByte()
    }

    return bcd
}

/**
 * Convert a binary coded decimal (BCD) array to a hexadecimal string.
 * @return The hexadecimal string.
 */
fun ByteArray.toHex() = joinToString("") { "%02X".format(it) }

fun ByteArray.toHex(length: Int): String {
    val hex = StringBuffer()
    for (i in 0 until length)
        hex.append(String.format("%02X", this[i]))
    return hex.toString()
}

/**
 * Convert a hexadecimal string to an integer.
 * @return The integer.
 */
fun Int.toHex(): String {
    val hex = StringBuffer()
    hex.append(String.format("%02X", this))
    return hex.toString()
}

/**
 * Convert an integer to a byte array.
 * @param bigEndian If true, the most significant byte will be at index 0.
 * @return The byte array.
 */
fun Int.toByteArray(bigEndian: Boolean = true): ByteArray {
    return if (bigEndian) byteArrayOf(
        (this ushr 24 and 0xFF).toByte(),
        (this ushr 16 and 0xFF).toByte(),
        (this ushr 8 and 0xFF).toByte(),
        (this and 0xFF).toByte()
    ).dropWhile { it == 0.toByte() }.toByteArray()
    else byteArrayOf(
        (this and 0xFF).toByte(),
        (this ushr 8 and 0xFF).toByte(),
        (this ushr 16 and 0xFF).toByte(),
        (this ushr 24 and 0xFF).toByte()
    ).dropWhile { it == 0.toByte() }.toByteArray()
}

fun StringBuilder.appendIfNotNull(value: String?) {
    if (value != null) {
        this.append(value)
    }
}

// -------------------------------- DUKPT ENC ----------------------------------------------

private fun String.decodeHex(): ByteArray {
    check(length % 2 == 0) { "Must have an even length" }
    return chunked(2)
        .map { it.toInt(16).toByte() }
        .toByteArray()
}

fun String.addEncPadding(): String {
    var str = this
    val hexes = "0123456789ABCDEF"
    val pad = (8 * ceil(str.length.toDouble() / (2 * 8))).toInt()
    val mod = str.length % 2
    if (mod > 0) str = "0$str"
    val exp = str.decodeHex()
    val finalExp = ByteArray(pad)
    System.arraycopy(exp, 0, finalExp, pad - exp.size, exp.size)
    val hex = java.lang.StringBuilder(2 * finalExp.size)
    for (b in finalExp)
        hex.append(hexes[b.toInt() and 0xF0 shr 4]).append(hexes[b.toInt() and 0x0F])
    return hex.toString()
}

// -------------------------------- DUKPT MAC ----------------------------------------------

fun String.addPaddingMac(): String {
    var data = this.toHex()
    data = data.addPaddingInLast()
    return data
}

private fun String.toHex(): String = String.format("%040x", BigInteger(1, this.toByteArray()))

private fun String.addPaddingInLast(): String {
    var str = this
    val hexes = "0123456789ABCDEF"
    val pad = (8 * ceil(str.length.toDouble() / (2 * 8))).toInt()
    val mod = str.length % 2
    if (mod > 0) str = "0$str"
    val exp: ByteArray = str.decodeHex()
    val finalExp = ByteArray(pad)
    System.arraycopy(exp, 0, finalExp, 0, exp.size)
    val hex = java.lang.StringBuilder(2 * finalExp.size)
    for (b in finalExp) hex.append(hexes[b.toInt() and 0xF0 shr 4])
        .append(hexes[b.toInt() and 0x0F])
    return hex.toString()
}

// -------------------------------- CLOSE LOOP ----------------------------------------------
fun ascciToHex(name: String): String {
    val stringBuilder = StringBuilder()
    for (c in name) {
        val hexValue = Integer.toHexString(c.code)
        stringBuilder.append(hexValue)
    }
    return stringBuilder.toString()
}

fun hexToAscci(hex: String): String {
    val stringBuilder = StringBuilder()
    val hexPairs = hex.chunked(2)
    for (hexPair in hexPairs) {
        val decimalValue = hexPair.toInt(16)
        stringBuilder.append(decimalValue.toChar())
    }
    return stringBuilder.toString()
}

fun Int.toRupee(): Double {
    return if (this != 0) {
        this / 100.0
    } else {
        0.0
    }
}

fun Double.toPaisa(): Int {
    return (this * 100).toInt()
}

fun String.subString(start: Int, len: Int): String {
    return try {
        this.substring(start, start + len)
    } catch (e: Exception) {
        Logger.error(e)
        ""
    }
}

fun String.hexToBin(): String {
    try {
        val out = StringBuilder()
        this.forEach {
            if (it == '0') out.append("0000")
            if (it == '1') out.append("0001")
            if (it == '2') out.append("0010")
            if (it == '3') out.append("0011")
            if (it == '4') out.append("0100")
            if (it == '5') out.append("0101")
            if (it == '6') out.append("0110")
            if (it == '7') out.append("0111")
            if (it == '8') out.append("1000")
            if (it == '9') out.append("1001")
            if (it == 'A') out.append("1010")
            if (it == 'B') out.append("1011")
            if (it == 'C') out.append("1100")
            if (it == 'D') out.append("1101")
            if (it == 'E') out.append("1110")
            if (it == 'F') out.append("1111")
        }
        return out.toString()
    } catch (e: Exception) {
        Logger.error(e)
        return ""
    }
}

fun String.binToHex(): String {
    val out = StringBuilder()
    val outArray = this.deviceInParts(4)
    outArray.forEach {
        if (it == "0000") out.append('0')
        if (it == "0001") out.append('1')
        if (it == "0010") out.append('2')
        if (it == "0011") out.append('3')
        if (it == "0100") out.append('4')
        if (it == "0101") out.append('5')
        if (it == "0110") out.append('6')
        if (it == "0111") out.append('7')
        if (it == "1000") out.append('8')
        if (it == "1001") out.append('9')
        if (it == "1010") out.append('A')
        if (it == "1011") out.append('B')
        if (it == "1100") out.append('C')
        if (it == "1101") out.append('D')
        if (it == "1110") out.append('E')
        if (it == "1111") out.append('F')
    }
    return out.toString()
}

fun String.deviceInParts(parts: Int): ArrayList<String> {
    val outArray = arrayListOf<String>()
    for (i in indices step parts) {
        outArray.add(this.subString(i, parts))
    }
    return outArray
}

fun String.toCenter(maxWeight: Int = 60): String {
    val diff = maxWeight - this.length
    return this.padStart(this.length + diff / 2)
}

fun String.toTable(maxWeight: Int = 76): String {
    val strings = this.split("|")
    val tabularString = StringBuilder()
    val unitLen = maxWeight / strings.size
    strings.forEachIndexed { index: Int, str: String ->
        if (strings.size == 2) {
            if (index == 0) {
                tabularString.append(str.padEnd(unitLen))
            } else {
                tabularString.append(str)
            }
        } else if (strings.size == 3) {
            when (index) {
                0 -> tabularString.append(str.padEnd(unitLen * 2))
                1 -> tabularString.append(str.padStart(unitLen / 4).padEnd(unitLen / 2))
                else -> tabularString.append(str)
            }
        }
    }
    return tabularString.toString() + "\n"
}

fun String.addSpaceInBetween(maxWeight: Int = 76): String {
    val strings = this.split("|")
    val diff = maxWeight - this.length - 1
    val spacedString = StringBuilder()
    spacedString.append(" ")
    spacedString.append(strings[0].padEnd(strings[0].length + diff))
    if (strings.size > 1) spacedString.append(strings[1])
    spacedString.append(" ")
    return spacedString.toString()
}

fun Number.toPaisa(): Int {
    return (this.toDouble() * 100).toInt()
}

fun Number.toRupee(): Double {
    return if (this != 0) {
        this.toDouble() / 100
    } else {
        0.0
    }
}