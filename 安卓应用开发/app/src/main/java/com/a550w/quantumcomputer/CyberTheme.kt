package com.a550w.quantumcomputer

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.a550w.quantumcomputer.CyberColors

private val CyberColorScheme = darkColorScheme(
    primary = CyberColors.Primary,
    secondary = CyberColors.Secondary,
    tertiary = CyberColors.Secondary,
    background = CyberColors.Background,
    surface = CyberColors.Surface,
    error = CyberColors.Error,
    onPrimary = CyberColors.Background,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = CyberColors.Text,
    onSurface = CyberColors.Text,
    onError = Color.White
)

@Composable
fun CyberTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = CyberColorScheme,
        content = content
    )
}
