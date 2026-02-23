package com.atek.scs.utils.extentions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

open class Activity : CoroutineScope by MainScope() {

    open fun onCreate() {

    }

    open fun shutdown(callback: () -> Unit) {
        this.cancel("System shutdown initiated.")
    }

}