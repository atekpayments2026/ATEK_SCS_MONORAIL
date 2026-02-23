package com.atek.scs.feature.common.theme.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.atek.scs.feature.common.theme.PreviewWithTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

object AlertDialog {

    var message by mutableStateOf("")
    var type by mutableStateOf(Type.ERROR)
    var showDialog by mutableStateOf(false)
    private var onDismissAction: () -> Unit = {}

    suspend fun show(
        message: String,
        type: Type = Type.ERROR,
        onDismiss: () -> Unit = { showDialog = false }
    ) = withContext(Dispatchers.Main) {
        AlertDialog.message = message
        AlertDialog.type = type
        showDialog = true
        onDismissAction = onDismiss
    }

    fun showNotThreadSafe(
        message: String,
        type: Type = Type.ERROR,
        onDismiss: () -> Unit = { showDialog = false }
    ) {
        AlertDialog.message = message
        AlertDialog.type = type
        showDialog = true
        onDismissAction = onDismiss
    }

    fun dismiss() {
        showDialog = false
        onDismissAction()
    }

    enum class Type {
        ERROR,
        SUCCESS
    }

}

@Composable
fun AlertDialog() {

    val message by AlertDialog::message
    val type by AlertDialog::type
    val showDialog by AlertDialog::showDialog

    if (showDialog) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(false) {}
                .background(Color.Black.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            Dialog(
                onDismissRequest = { AlertDialog.showDialog = false },
                properties = DialogProperties(
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false
                )
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = Color.Black
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        /*AsyncImage(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .size(100.dp)
                                .padding(8.dp),
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(
                                    if (type == AlertDialog.Type.ERROR) R.drawable.error
                                    else R.drawable.success
                                )
                                .decoderFactory(
                                    if (SDK_INT >= 28) ImageDecoderDecoder.Factory()
                                    else GifDecoder.Factory()
                                )
                                .crossfade(true)
                                .build(),
                            contentDescription = type.name,
                        )*/
                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(8.dp),
                            text = message,
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center
                        )
                        Button(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(8.dp),
                            shape = MaterialTheme.shapes.small,
                            onClick = { AlertDialog.dismiss() },
                        ) {
                            Text(
                                modifier = Modifier.padding(8.dp),
                                style = MaterialTheme.typography.titleMedium,
                                text = "Dismiss"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview
private fun PreviewAlertDialog() {
    runBlocking { AlertDialog.show("This is an error message") }
    PreviewWithTheme {
        AlertDialog()
    }
}