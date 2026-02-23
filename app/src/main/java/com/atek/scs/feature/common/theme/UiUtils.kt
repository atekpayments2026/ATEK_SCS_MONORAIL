package com.atek.scs.feature.common.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.atek.scs.feature.common.theme.components.AlertDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.tinylog.Logger

@Composable
fun PreviewWithTheme(
    darkTheme: Boolean = false,
    block: @Composable () -> Unit
) {
    AtekScsTheme(
        darkTheme = darkTheme,
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Box(
                modifier = Modifier.padding(innerPadding)
            ) {
                block()
            }
        }
    }
}

fun ScreenModel.withLoading(
    isLoading: MutableState<Boolean>,
    block: suspend () -> Unit
) {
    screenModelScope.launch {
        isLoading.value = true
        try {
            withContext(Dispatchers.Default) {
                block()
            }
        } catch (e: Exception) {
            Logger.error(e)
            withContext(Dispatchers.Main) {
                AlertDialog.show(
                    type = AlertDialog.Type.ERROR,
                    message = e.message ?: e.stackTraceToString()
                )
            }
        } finally {
            isLoading.value = false
        }
    }
}


suspend fun withLoading(
    isLoading: MutableState<Boolean>,
    block: suspend () -> Unit
) = withContext(Dispatchers.Main) {
    try {
        isLoading.value = true
        withContext(Dispatchers.Default) {
            block()
        }
    } catch (e: Exception) {
        Logger.error(e)
        AlertDialog.show(
            type = AlertDialog.Type.ERROR,
            message = e.message ?: e.stackTraceToString()
        )
    } finally {
        isLoading.value = false
    }
}

suspend fun <T> withResponseLoading(
    isLoading: MutableState<Boolean>,
    block: suspend () -> T?
) = withContext(Dispatchers.Main) {
    try {
        isLoading.value = true
        withContext(Dispatchers.Default) {
            block()
        }
    } catch (e: Exception) {
        Logger.error(e)
        AlertDialog.show(
            type = AlertDialog.Type.ERROR,
            message = e.message ?: e.stackTraceToString()
        )
        null
    } finally {
        isLoading.value = false
    }
}

fun <T> List<T>.reorderForColumns(columns: Int): List<T> {
    return List(this.size) { index ->
        val rowIndex = index / columns
        val colIndex = index % columns
        val newIndex = colIndex * (this.size / columns) + rowIndex
        this.getOrNull(newIndex)
    }.filterNotNull()
}

//@OptIn(ExperimentalResourceApi::class)
//@Composable
//fun imageResource(fileName: String) = useResource("drawable/${fileName}.png") { it.readAllBytes().decodeToImageBitmap() }

@Composable
fun imageResource(fileName: String): ImageBitmap {
    val context = LocalContext.current

    return remember(fileName) {
        val resId = context.resources.getIdentifier(
            fileName,
            "drawable",
            context.packageName
        )

        require(resId != 0) {
            "Drawable resource '$fileName' not found"
        }

        val drawable = ContextCompat.getDrawable(context, resId)
            ?: error("Failed to load drawable '$fileName'")

        drawable.toBitmap().asImageBitmap()
    }
}
