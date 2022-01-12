package com.devmon.crcp.ui.screening.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun DashDivider(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.onSurface.copy(alpha = 0.12f),
    thickness: Dp = 1.dp,
    startIndent: Dp = 0.dp,
    dashGap: Dp = 0.dp,
    dashWidth: Dp = 1.dp,
) {
    val indentMod = modifier.fillMaxWidth().run {
        if (startIndent.value != 0f) {
            padding(start = startIndent)
        } else {
            this
        }
    }

    Canvas(modifier = indentMod) {
        val pathEffect = if (dashGap == 0.dp) {
            null
        } else {
            // (on, off)
            PathEffect.dashPathEffect(floatArrayOf(dashWidth.toPx(), dashGap.toPx()))
        }

        val canvasWidth = size.width

        drawLine(
            start = Offset(x = 0f, y = 0f),
            end = Offset(x = canvasWidth, y = 0f),
            color = color,
            pathEffect = pathEffect,
            strokeWidth = thickness.toPx()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DashDividerPreview() {
    Column(
        modifier = Modifier.height(100.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        DashDivider(
            color = "#000000".color,
            startIndent = 50.dp,
            thickness = 5.dp,
            dashGap = 4.dp,
            dashWidth = 10.dp
        )
    }
}