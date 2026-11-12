package com.umerini.clearnote.data.model

import androidx.compose.ui.graphics.Color

data class GlassTheme(
    val id: Int,
    val name: String,
    val primaryColor: Color,
    val glassTint: Color,
    val glowColor: Color,
    val borderColor: Color
)

object GlassThemePalette {
    val themes = listOf(
        GlassTheme(
            id = 0,
            name = "Frost Cyan",
            primaryColor = Color(0xFF0EA5E9),
            glassTint = Color(0x280EA5E9),
            glowColor = Color(0x3338BDF8),
            borderColor = Color(0x6638BDF8)
        ),
        GlassTheme(
            id = 1,
            name = "Emerald Glow",
            primaryColor = Color(0xFF10B981),
            glassTint = Color(0x2810B981),
            glowColor = Color(0x3334D399),
            borderColor = Color(0x6634D399)
        ),
        GlassTheme(
            id = 2,
            name = "Amethyst Quartz",
            primaryColor = Color(0xFF8B5CF6),
            glassTint = Color(0x288B5CF6),
            glowColor = Color(0x33A78BFA),
            borderColor = Color(0x66A78BFA)
        ),
        GlassTheme(
            id = 3,
            name = "Sunset Amber",
            primaryColor = Color(0xFFF59E0B),
            glassTint = Color(0x28F59E0B),
            glowColor = Color(0x33FBBF24),
            borderColor = Color(0x66FBBF24)
        ),
        GlassTheme(
            id = 4,
            name = "Rose Glass",
            primaryColor = Color(0xFFF43F5E),
            glassTint = Color(0x28F43F5E),
            glowColor = Color(0x33FB7185),
            borderColor = Color(0x66FB7185)
        ),
        GlassTheme(
            id = 5,
            name = "Pure Crystal",
            primaryColor = Color(0xFF64748B),
            glassTint = Color(0x22FFFFFF),
            glowColor = Color(0x2294A3B8),
            borderColor = Color(0x4DFFFFFF)
        )
    )

    fun getThemeById(id: Int): GlassTheme {
        return themes.find { it.id == id } ?: themes.first()
    }
}
