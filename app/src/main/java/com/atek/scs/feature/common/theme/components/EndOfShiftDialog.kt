package com.atek.scs.feature.common.theme.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.atek.scs.feature.common.theme.PreviewWithTheme
import com.atek.scs.feature.common.theme.imageResource
import kotlinx.coroutines.launch

object EndOfShiftDialog {

    var showDialog by mutableStateOf(false)
    var onRePrint: (suspend () -> Unit)? = null
    var onLogout: (suspend () -> Unit)? = null

    fun show(
        onRePrint: (suspend () -> Unit)? = null,
        onDismiss: (suspend () -> Unit)? = null
    ) {
        this.showDialog = true
        this.onRePrint = onRePrint
        this.onLogout = onDismiss
    }
}

@Composable
fun EndOfShiftDialog() {
    val scope = rememberCoroutineScope()
    if (EndOfShiftDialog.showDialog) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(false) {}
                .background(Color.Black.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            Dialog(onDismissRequest = { AlertDialog.showDialog = false }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = Color.Black
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .size(200.dp)
                                .padding(16.dp),
                            bitmap = imageResource("end_of_shift"),
                            contentDescription = "END OF SHIFT",
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            modifier = Modifier.fillMaxWidth().padding(8.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            text = "Please check if end of shift printed properly \n If not please press Reprint.",
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Row {
                            Button(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(8.dp),
                                shape = MaterialTheme.shapes.small,
                                onClick = {
                                    scope.launch {
                                        EndOfShiftDialog.onRePrint?.invoke()
                                        EndOfShiftDialog.showDialog = false
                                    }
                                },
                            ) {
                                Text(
                                    modifier = Modifier.padding(8.dp),
                                    style = MaterialTheme.typography.titleLarge,
                                    text = "RE-PRINT"
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(8.dp),
                                shape = MaterialTheme.shapes.small,
                                onClick = {
                                    scope.launch {
                                        EndOfShiftDialog.onLogout?.invoke()
                                        EndOfShiftDialog.showDialog = false
                                    }
                                },
                            ) {
                                Text(
                                    modifier = Modifier.padding(8.dp),
                                    style = MaterialTheme.typography.titleMedium,
                                    text = "DISMISS"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview
private fun Preview() {
    EndOfShiftDialog.showDialog = true
    PreviewWithTheme {
        EndOfShiftDialog()
    }
}