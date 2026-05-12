package com.a550w.quantumcomputer

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.a550w.quantumcomputer.CyberColors
import kotlinx.coroutines.delay

@Composable
fun ComputingScreen(onNavigateToHandCounting: (Int) -> Unit, onNavigateToError: (String) -> Unit, expression: String) {
    val fullText = "已成功驱动550W超级量子计算器"
    var displayedText by remember { mutableStateOf("") }
    var currentIndex by remember { mutableIntStateOf(0) }

    val parts = expression.toList()
    val num1 = parts.getOrNull(0)?.digitToIntOrNull() ?: 0
    val operator = parts.getOrNull(1)?.toString() ?: "+"
    val num2 = parts.getOrNull(2)?.digitToIntOrNull() ?: 0

    val result = if (operator == "+") num1 + num2 else num1 - num2

    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )

    val isValid = expression.matches(Regex("^[0-9][+\\-][0-9]$")) && result in 0..10

    LaunchedEffect(Unit) {
        for (i in fullText.indices) {
            delay(50)
            displayedText = fullText.substring(0, i + 1)
        }
        delay(1000)

        if (!isValid) {
            onNavigateToError("已超出计算范围，疑似栈溢出")
        } else {
            onNavigateToHandCounting(result)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberColors.Background),
        contentAlignment = Alignment.Center
    ) {
        ScanlineEffect()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = displayedText,
                color = CyberColors.Primary.copy(alpha = glowAlpha),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.shadow(16.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "$num1 $operator $num2 = ?",
                color = CyberColors.Text,
                fontSize = 20.sp,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}
