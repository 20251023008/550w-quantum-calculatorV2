package com.a550w.quantumcomputer

import android.speech.tts.TextToSpeech
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.a550w.quantumcomputer.CyberColors
import kotlinx.coroutines.delay
import java.util.Locale

@Composable
fun HandCountingScreen(
    result: Int,
    expression: String,
    onComplete: () -> Unit
) {
    val context = LocalContext.current
    var ttsReady by remember { mutableStateOf(false) }
    var tts: TextToSpeech? by remember { mutableStateOf(null) }
    var currentCount by remember { mutableIntStateOf(0) }
    var showOverflow by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.CHINESE
                tts?.setSpeechRate(0.8f)
                ttsReady = true
            }
        }
        onDispose {
            tts?.stop()
            tts?.shutdown()
        }
    }

    LaunchedEffect(result, expression) {
        val parts = expression.toList()
        val num1 = parts.getOrNull(0)?.digitToIntOrNull() ?: 0
        val num2 = parts.getOrNull(2)?.digitToIntOrNull() ?: 0
        val operator = parts.getOrNull(1)?.toString() ?: "+"
        val expectedSum = if (operator == "+") num1 + num2 else 0
        val maxCount = if (operator == "+") expectedSum else result

        for (i in 1..maxCount) {
            delay(400)
            currentCount = i
            if (currentCount == 10 && result > 10) {
                showOverflow = true
                break
            }
        }

        if (!showOverflow && currentCount == result) {
            delay(500)
            val chineseNumbers = listOf("零", "一", "二", "三", "四", "五", "六", "七", "八", "九", "十")
            val numberText = if (result <= 10) chineseNumbers[result] else result.toString()

            if (ttsReady) {
                tts?.speak("答案是$numberText", TextToSpeech.QUEUE_FLUSH, null, "result_tts")
            }

            delay(2000)
            onComplete()
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "hand_glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "hand_glow_alpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberColors.Background),
        contentAlignment = Alignment.Center
    ) {
        ScanlineEffect()

        if (showOverflow) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "SYSTEM OVERFLOW",
                    color = CyberColors.Error.copy(alpha = glowAlpha),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "已超出计算范围，疑似栈溢出",
                    color = CyberColors.Error,
                    fontSize = 18.sp,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "[ 正在重启系统... ]",
                    color = CyberColors.Primary,
                    fontSize = 16.sp,
                    fontFamily = FontFamily.Monospace
                )
            }

            LaunchedEffect(Unit) {
                delay(3000)
                onComplete()
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "量子手指计数中",
                    color = CyberColors.Primary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = expression,
                        color = CyberColors.Text,
                        fontSize = 24.sp,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "= $currentCount",
                        color = CyberColors.Primary,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Spacer(modifier = Modifier.height(48.dp))

                HandVectorGraphic(
                    fingersUp = currentCount.coerceIn(0, 10),
                    modifier = Modifier.size(300.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "当前计数: $currentCount / $result",
                    color = CyberColors.Text,
                    fontSize = 16.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

@Composable
fun HandVectorGraphic(
    fingersUp: Int,
    modifier: Modifier = Modifier
) {
    val fingerColor by animateColorAsState(
        targetValue = if (fingersUp >= 1) CyberColors.Primary else CyberColors.Text.copy(alpha = 0.3f),
        label = "finger_color"
    )

    Canvas(modifier = modifier) {
        val palmWidth = size.width * 0.8f
        val palmHeight = size.height * 0.5f
        val palmLeft = (size.width - palmWidth) / 2
        val palmTop = size.height * 0.4f

        drawRoundRect(
            color = CyberColors.Surface,
            topLeft = Offset(palmLeft, palmTop),
            size = Size(palmWidth, palmHeight),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(20f, 20f)
        )

        drawRoundRect(
            color = CyberColors.Primary.copy(alpha = 0.3f),
            topLeft = Offset(palmLeft, palmTop),
            size = Size(palmWidth, palmHeight),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(20f, 20f),
            style = Stroke(width = 3f)
        )

        val fingerWidth = palmWidth / 6
        val fingerHeight = size.height * 0.35f
        val fingerSpacing = fingerWidth * 0.3f

        for (i in 0 until 5) {
            val fingerX = palmLeft + i * (fingerWidth + fingerSpacing) + fingerSpacing
            val fingerY = palmTop - fingerHeight * 0.3f
            val isUp = i < fingersUp

            val path = Path().apply {
                moveTo(fingerX, fingerY + fingerHeight)
                lineTo(fingerX, fingerY + fingerHeight * 0.3f)
                quadraticBezierTo(
                    fingerX, fingerY,
                    fingerX + fingerWidth / 2, fingerY
                )
                quadraticBezierTo(
                    fingerX + fingerWidth, fingerY,
                    fingerX + fingerWidth, fingerY + fingerHeight * 0.3f
                )
                lineTo(fingerX + fingerWidth, fingerY + fingerHeight)
                close()
            }

            drawPath(
                path = path,
                color = if (isUp) fingerColor else CyberColors.Text.copy(alpha = 0.2f),
                style = if (isUp) Fill else Fill
            )

            if (isUp) {
                drawPath(
                    path = path,
                    color = CyberColors.Primary.copy(alpha = 0.5f),
                    style = Stroke(width = 2f)
                )
            }
        }

        val thumbX = palmLeft - fingerWidth * 0.3f
        val thumbY = palmTop + palmHeight * 0.3f
        val thumbUp = fingersUp >= 6

        val thumbPath = Path().apply {
            moveTo(thumbX + fingerWidth, thumbY)
            lineTo(thumbX - fingerWidth * 0.5f, thumbY - fingerHeight * 0.4f)
            quadraticBezierTo(
                thumbX - fingerWidth, thumbY - fingerHeight * 0.5f,
                thumbX - fingerWidth * 0.3f, thumbY - fingerHeight * 0.2f
            )
            lineTo(thumbX + fingerWidth * 0.5f, thumbY + fingerHeight * 0.2f)
            close()
        }

        drawPath(
            path = thumbPath,
            color = if (thumbUp) fingerColor else CyberColors.Text.copy(alpha = 0.2f),
            style = Fill
        )
    }
}
