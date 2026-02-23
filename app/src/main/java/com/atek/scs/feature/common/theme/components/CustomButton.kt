package com.atek.scs.feature.common.theme.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ItemButton(
    modifier: Modifier,
    text: String,
    onClick: () -> Unit,
    isEnable: Boolean = true,
    isSelected: Boolean
) {
    Card(
        modifier = modifier,
        onClick = onClick,
        shape = MaterialTheme.shapes.small,
        enabled = isEnable,
        colors = CardDefaults.cardColors()
            .copy(
                containerColor = if (isSelected) MaterialTheme.colorScheme.tertiary
                else MaterialTheme.colorScheme.secondary,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
        border = BorderStroke(1.dp, Color.Gray)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = text,
                fontSize = 16.sp,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        }
    }
}