package com.a550w.quantumcomputer

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
fun CalculatorScreen(
    onCalculate: (String) -> Unit
) {
    var expression by remember { mutableStateOf("") }

    val isValidExpression = expression.matches(Regex("^[0-9][+\\-][0-9]$"))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberColors.Background)
    ) {
        ScanlineEffect()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .border(2.dp, CyberColors.Primary, RoundedCornerShape(8.dp))
                    .background(CyberColors.Surface, RoundedCornerShape(8.dp))
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = expression.ifEmpty { " " },
                    color = CyberColors.Primary,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "550W 超级量子计算器",
                color = CyberColors.Primary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )

            Spacer(modifier = Modifier.weight(1f))

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    listOf("7", "8", "9").forEach { digit ->
                        CalculatorButton(
                            text = digit,
                            onClick = {
                                if (expression.length < 3) {
                                    expression += digit
                                }
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    listOf("4", "5", "6").forEach { digit ->
                        CalculatorButton(
                            text = digit,
                            onClick = {
                                if (expression.length < 3) {
                                    expression += digit
                                }
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    listOf("1", "2", "3").forEach { digit ->
                        CalculatorButton(
                            text = digit,
                            onClick = {
                                if (expression.length < 3) {
                                    expression += digit
                                }
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CalculatorButton(
                        text = "+",
                        onClick = {
                            if (expression.length == 1) {
                                expression += "+"
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                    CalculatorButton(
                        text = "0",
                        onClick = {
                            if (expression.length < 3) {
                                expression += "0"
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                    CalculatorButton(
                        text = "-",
                        onClick = {
                            if (expression.length == 1) {
                                expression += "-"
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CalculatorButton(
                        text = "C",
                        onClick = { expression = "" },
                        modifier = Modifier.weight(1f),
                        isSecondary = true
                    )
                    CalculatorButton(
                        text = "=",
                        onClick = {
                            if (isValidExpression) {
                                onCalculate(expression)
                            }
                        },
                        modifier = Modifier.weight(2f),
                        isPrimary = true,
                        enabled = isValidExpression
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun CalculatorButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isPrimary: Boolean = false,
    isSecondary: Boolean = false,
    enabled: Boolean = true
) {
    val infiniteTransition = rememberInfiniteTransition(label = "button_glow")
    val glowIntensity by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = if (isPrimary) 0.8f else 0.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_intensity"
    )

    val backgroundColor = when {
        isPrimary -> CyberColors.Primary.copy(alpha = 0.2f)
        isSecondary -> CyberColors.Secondary.copy(alpha = 0.2f)
        else -> CyberColors.ButtonBackground
    }

    val textColor = when {
        isPrimary -> CyberColors.Primary
        isSecondary -> CyberColors.Secondary
        else -> CyberColors.Text
    }

    Box(
        modifier = modifier
            .height(64.dp)
            .shadow(elevation = if (enabled) 8.dp else 0.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor, RoundedCornerShape(12.dp))
            .border(
                width = 2.dp,
                color = if (enabled) textColor.copy(alpha = glowIntensity) else CyberColors.Text.copy(alpha = 0.3f),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(
                enabled = enabled,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor.copy(alpha = if (enabled) 1f else 0.5f),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            textAlign = TextAlign.Center
        )
    }
}
