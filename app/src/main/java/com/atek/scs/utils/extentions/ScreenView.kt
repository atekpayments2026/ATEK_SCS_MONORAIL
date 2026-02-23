package com.atek.scs.utils.extentions

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.core.lifecycle.LifecycleEffectOnce
import cafe.adriel.voyager.core.screen.Screen

@OptIn(ExperimentalVoyagerApi::class)
interface ScreenView : Screen {

    @Composable
    fun <T : ViewModel> registerLifecycle(viewModel: T) {
        LifecycleEffectOnce {
            viewModel.onCreate()
        }
    }

}