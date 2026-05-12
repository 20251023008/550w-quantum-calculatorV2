package com.a550w.quantumcomputer

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.a550w.quantumcomputer.CyberColors

@Composable
fun ErrorScreen(message: String, onNavigateBack: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "error_glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(300, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "error_glow_alpha"
    )

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
                text = "SYSTEM ERROR",
                color = CyberColors.Error.copy(alpha = glowAlpha),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .shadow(16.dp)
                    .graphicsLayer {
                        alpha = glowAlpha
                    }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = message,
                color = CyberColors.Error,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily.Monospace,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))

            Box(
                modifier = Modifier
                    .shadow(12.dp)
                    .background(CyberColors.Error.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                    .border(2.dp, CyberColors.Error, RoundedCornerShape(12.dp))
                    .clickable { onNavigateBack() }
                    .padding(horizontal = 48.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "[ 返回 ]",
                    color = CyberColors.Error,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}
