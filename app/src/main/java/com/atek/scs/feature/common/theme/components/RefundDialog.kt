package com.atek.scs.feature.common.theme.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.window.DialogProperties
import com.atek.scs.feature.common.theme.PreviewWithTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

object RefundDialog {

    var ticketAmount by mutableDoubleStateOf(0.0)
    var refundAmount by mutableDoubleStateOf(0.0)
    var refundCharges by mutableDoubleStateOf(0.0)
    var callback: (suspend () -> Unit)? = null
    var show by mutableStateOf(false)

    suspend fun show(
        amount: Double,
        refund: Double,
        charges: Double,
        refundCallback: suspend () -> Unit
    ) = withContext(Dispatchers.Main) {
        ticketAmount = amount
        refundAmount = refund
        refundCharges = charges
        callback = refundCallback
        show = true
    }

    fun dismiss() {
        show = false
        callback = null
    }

}

@Composable
fun RefundDialog() {
    val scope = rememberCoroutineScope()
    if (RefundDialog.show) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(false) {}
                .background(Color.Black.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            Dialog(
                onDismissRequest = { RefundDialog.show = false },
                properties = DialogProperties(
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false
                )
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(),
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
                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(8.dp),
                            text = "REFUND TICKET",
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally),
                            text = "TICKET AMOUNT : ${RefundDialog.ticketAmount}",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally),
                            text = "REFUND CHARGES : ${RefundDialog.refundCharges}",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(8.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally),
                            text = "REFUND AMOUNT : ${RefundDialog.refundAmount}",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        Row {
                            Button(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(8.dp),
                                shape = MaterialTheme.shapes.small,
                                onClick = {
                                    RefundDialog.dismiss()
                                },
                            ) {
                                Text(
                                    modifier = Modifier.padding(8.dp),
                                    style = MaterialTheme.typography.titleMedium,
                                    text = "DISMISS"
                                )
                            }
                            Button(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(8.dp),
                                shape = MaterialTheme.shapes.small,
                                onClick = {
                                    scope.launch {
                                        RefundDialog.callback?.invoke()
                                        RefundDialog.dismiss()
                                    }
                                },
                            ) {
                                Text(
                                    modifier = Modifier.padding(8.dp),
                                    style = MaterialTheme.typography.titleMedium,
                                    text = "REFUND TICKET"
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
private fun PreviewAlertDialog() {
    runBlocking {
        RefundDialog.show(
            amount = 100.0,
            refund = 50.0,
            charges = 10.0
        ) {

        }
    }
    PreviewWithTheme {
        RefundDialog()
    }
}