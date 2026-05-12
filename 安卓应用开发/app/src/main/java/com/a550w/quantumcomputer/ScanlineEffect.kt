package com.a550w.quantumcomputer

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun ScanlineEffect(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "scanline")
    val scanLinePosition by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "scanline_position"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val scanY = size.height * scanLinePosition
        drawLine(
            color = Color(0x2000F3FF),
            start = Offset(0f, scanY),
            end = Offset(size.width, scanY),
            strokeWidth = 2.dp.toPx()
        )

        val gridColor = Color(0x10FFFFFF)
        val gridSpacing = 40.dp.toPx()
        for (x in 0..(size.width / gridSpacing).toInt()) {
            drawLine(
                color = gridColor,
                start = Offset(x * gridSpacing, 0f),
                end = Offset(x * gridSpacing, size.height),
                strokeWidth = 0.5.dp.toPx()
            )
        }
        for (y in 0..(size.height / gridSpacing).toInt()) {
            drawLine(
                color = gridColor,
                start = Offset(0f, y * gridSpacing),
                end = Offset(size.width, y * gridSpacing),
                strokeWidth = 0.5.dp.toPx()
            )
        }
    }
}
