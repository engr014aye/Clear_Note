package com.umerini.clearnote.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = ElectricCyan,
    onPrimary = PureWhite,
    primaryContainer = CyanGlow.copy(alpha = 0.2f),
    onPrimaryContainer = PureWhite,
    secondary = EmeraldMint,
    onSecondary = PureWhite,
    tertiary = AmethystViolet,
    background = DeepSlate900,
    onBackground = Slate100,
    surface = DeepSlate800,
    onSurface = Slate100,
    surfaceVariant = DeepSlate700,
    onSurfaceVariant = Slate300,
    outline = Slate600,
    outlineVariant = DeepSlate700
)

private val LightColorScheme = lightColorScheme(
    primary = ElectricCyan,
    onPrimary = PureWhite,
    primaryContainer = CyanGlow.copy(alpha = 0.2f),
    onPrimaryContainer = DeepSlate900,
    secondary = EmeraldMint,
    onSecondary = PureWhite,
    tertiary = AmethystViolet,
    background = Color(0xFFF8FAFC),
    onBackground = DeepSlate900,
    surface = PureWhite,
    onSurface = DeepSlate900,
    surfaceVariant = Color(0xFFE2E8F0),
    onSurfaceVariant = Slate600,
    outline = Slate400,
    outlineVariant = Color(0xFFCBD5E1)
)

@Composable
fun ClearNoteTheme(
    darkTheme: Boolean = true, // Default to stunning Liquid Glass dark theme
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window ?: return@SideEffect
            val windowInsetsController = WindowCompat.getInsetsController(window, view)
            windowInsetsController.isAppearanceLightStatusBars = !darkTheme
            windowInsetsController.isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
