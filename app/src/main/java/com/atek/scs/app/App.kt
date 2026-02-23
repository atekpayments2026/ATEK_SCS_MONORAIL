package com.atek.scs.app

import android.app.Application
import com.atek.scs.feature.common.database.AppDatabase

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        AppDatabase.init(this)
    }
}