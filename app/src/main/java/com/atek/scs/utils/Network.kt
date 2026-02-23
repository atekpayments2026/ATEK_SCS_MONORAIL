package com.atek.scs.utils

import org.tinylog.Logger
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.*

fun getIPAddress(useIPv4: Boolean = true): String {
    try {
        val interfaces: List<NetworkInterface> = Collections.list(NetworkInterface.getNetworkInterfaces())
        for (inf in interfaces) {
            val adders: List<InetAddress> = Collections.list(inf.inetAddresses)
            for (adder in adders) {
                if (!adder.isLoopbackAddress) {
                    val sAdder = adder.hostAddress!!
                    val isIPv4 = sAdder.indexOf(':') < 0
                    if (useIPv4) {
                        if (isIPv4) return sAdder
                    } else {
                        if (!isIPv4) {
                            val deli = sAdder.indexOf('%')
                            return if (deli < 0) sAdder.uppercase(Locale.getDefault())
                            else sAdder.take(deli).uppercase(Locale.getDefault())
                        }
                    }
                }
            }
        }
    } catch (e: Exception) {
        Logger.error(e)
    }
    return ""
}