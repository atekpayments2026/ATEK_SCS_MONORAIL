package com.atek.scs.feature.common.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.atek.scs.feature.common.theme.PreviewWithTheme

@Composable
fun FullScreenLoader(
    message: String = "Please wait processing your request..."
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(false) {}
            .background(
                Color.Black.copy(alpha = 0.5f)
            ),
        contentAlignment = Alignment.Center
    ) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth(0.5f)
        ) {
            Column(
                modifier = Modifier
                    .padding(32.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(30.dp)
                )
                Spacer(modifier = Modifier.padding(8.dp))
                Text(
                    text = message,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
@Preview
private fun FullScreenLoaderPreview() {
    PreviewWithTheme {
        FullScreenLoader()
    }
}