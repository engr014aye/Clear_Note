package com.umerini.clearnote.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Primary Glass System Palette - Dark / Midnight Frost Theme
val DeepSlate900 = Color(0xFF090D16)
val DeepSlate800 = Color(0xFF0F172A)
val DeepSlate700 = Color(0xFF1E293B)
val Slate600 = Color(0xFF475569)
val Slate400 = Color(0xFF94A3B8)
val Slate300 = Color(0xFFCBD5E1)
val Slate100 = Color(0xFFF1F5F9)
val PureWhite = Color(0xFFFFFFFF)

// Liquid Glass Accent Lights
val ElectricCyan = Color(0xFF0EA5E9)
val CyanGlow = Color(0xFF38BDF8)
val EmeraldMint = Color(0xFF10B981)
val MintGlow = Color(0xFF34D399)
val AmethystViolet = Color(0xFF8B5CF6)
val VioletGlow = Color(0xFFA78BFA)
val SunsetAmber = Color(0xFFF59E0B)
val AmberGlow = Color(0xFFFBBF24)
val RoseQuartz = Color(0xFFF43F5E)
val RoseGlow = Color(0xFFFB7185)

// Translucent Frosted Glass Colors
val GlassBackgroundDark = Color(0x990F172A)
val GlassSurfaceDark = Color(0x331E293B)
val GlassCardDark = Color(0x401E293B)
val GlassHighlightDark = Color(0x26FFFFFF)
val GlassBorderDark = Color(0x3394A3B8)

val GlassBackgroundLight = Color(0xCCF8FAFC)
val GlassSurfaceLight = Color(0x59FFFFFF)
val GlassCardLight = Color(0x80FFFFFF)
val GlassHighlightLight = Color(0x99FFFFFF)
val GlassBorderLight = Color(0x40CBD5E1)

// Dynamic Liquid Gradient Brushes
val BackgroundGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF0B111E),
        Color(0xFF0F172A),
        Color(0xFF090E17)
    )
)

val GlassBorderSpecular = Brush.linearGradient(
    colors = listOf(
        Color(0x80FFFFFF),
        Color(0x26FFFFFF),
        Color(0x0DFFFFFF)
    )
)

val CyanGlassSpecular = Brush.linearGradient(
    colors = listOf(
        Color(0x9938BDF8),
        Color(0x3338BDF8),
        Color(0x0D0EA5E9)
    )
)
