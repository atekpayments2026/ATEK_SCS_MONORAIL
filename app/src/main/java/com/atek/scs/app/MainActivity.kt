package com.atek.scs.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cafe.adriel.voyager.navigator.Navigator
import com.atek.scs.feature.common.activity.MainScreen
import com.atek.scs.feature.common.system.loadEnv
import com.atek.scs.feature.common.theme.AtekScsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        loadEnv(this)
        setContent {
            AtekScsTheme {
                Navigator(MainScreen)
            }
        }
    }
}