package com.umerini.clearnote.ui.theme

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Liquid Glass styling modifier providing translucent frosted background,
 * delicate specular highlight border, and subtle depth elevation.
 */
fun Modifier.liquidGlass(
    shape: Shape = RoundedCornerShape(20.dp),
    backgroundColor: Color = Color(0x331E293B),
    borderColor: Color = Color(0x3394A3B8),
    borderWidth: Dp = 1.dp,
    elevation: Dp = 0.dp
): Modifier = this
    .then(
        if (elevation > 0.dp) {
            Modifier.shadow(
                elevation = elevation,
                shape = shape,
                ambientColor = Color(0x40000000),
                spotColor = Color(0x60000000)
            )
        } else Modifier
    )
    .clip(shape)
    .background(
        brush = Brush.verticalGradient(
            colors = listOf(
                backgroundColor.copy(alpha = (backgroundColor.alpha * 1.25f).coerceAtMost(0.9f)),
                backgroundColor.copy(alpha = backgroundColor.alpha * 0.85f)
            )
        )
    )
    .border(
        width = borderWidth,
        brush = Brush.linearGradient(
            colors = listOf(
                Color.White.copy(alpha = (borderColor.alpha * 1.5f).coerceAtMost(0.6f)),
                borderColor.copy(alpha = borderColor.alpha * 0.8f),
                borderColor.copy(alpha = 0.05f)
            )
        ),
        shape = shape
    )

/**
 * Liquid Glass Card with glowing frosted tint and specular light reflection
 */
fun Modifier.liquidGlassCard(
    shape: Shape = RoundedCornerShape(22.dp),
    tintColor: Color = Color(0x280EA5E9),
    borderHighlight: Color = Color(0x4D38BDF8),
    elevation: Dp = 4.dp
): Modifier = this
    .shadow(
        elevation = elevation,
        shape = shape,
        ambientColor = Color(0x20000000),
        spotColor = tintColor.copy(alpha = 0.35f)
    )
    .clip(shape)
    .background(
        brush = Brush.verticalGradient(
            colors = listOf(
                tintColor.copy(alpha = (tintColor.alpha * 1.4f).coerceAtMost(0.85f)),
                Color(0x331E293B),
                Color(0x240F172A)
            )
        )
    )
    .border(
        width = 1.dp,
        brush = Brush.linearGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.45f),
                borderHighlight,
                Color.White.copy(alpha = 0.08f)
            )
        ),
        shape = shape
    )

/**
 * Hardware-accelerated frosted glass container with deep blur on Android 12+ (API 31+)
 * and silky layered translucency on all versions.
 */
@Composable
fun LiquidGlassContainer(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(24.dp),
    tintColor: Color = Color(0x331E293B),
    borderColor: Color = Color(0x40CBD5E1),
    blurRadius: Dp = 20.dp,
    content: @Composable BoxScope.() -> Unit
) {
    val blurModifier = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        Modifier.blur(blurRadius)
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .clip(shape)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        tintColor.copy(alpha = (tintColor.alpha * 1.3f).coerceAtMost(0.9f)),
                        tintColor.copy(alpha = tintColor.alpha * 0.75f)
                    )
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.5f),
                        borderColor,
                        Color.White.copy(alpha = 0.05f)
                    )
                ),
                shape = shape
            ),
        content = content
    )
}
