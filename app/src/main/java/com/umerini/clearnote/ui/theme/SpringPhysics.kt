package com.umerini.clearnote.ui.theme

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.sign

/**
 * Apple-inspired Spring Physics specifications for fluid, tactile interactions.
 */
object LiquidPhysics {
    // Signature smooth Apple-style spring with medium bounce
    fun <T> appleBouncy(
        dampingRatio: Float = Spring.DampingRatioMediumBouncy,
        stiffness: Float = Spring.StiffnessMediumLow
    ): AnimationSpec<T> = spring(
        dampingRatio = dampingRatio,
        stiffness = stiffness
    )

    // Ultra fluid gentle spring for cards and modal sheets
    fun <T> fluidGentle(): AnimationSpec<T> = spring(
        dampingRatio = Spring.DampingRatioLowBouncy,
        stiffness = Spring.StiffnessLow
    )

    // Snappy spring for toggles, icons, and micro-interactions
    fun <T> tactileSnappy(): AnimationSpec<T> = spring(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessMedium
    )
}

/**
 * Custom Apple-inspired rubber-band bouncing overscroll modifier.
 * Applies progressive tension resistance when dragging past edges,
 * and rebounds smoothly using spring physics upon release.
 */
fun Modifier.bouncingOverscroll(
    maxOverscroll: Float = 280f,
    resistanceFactor: Float = 0.45f
): Modifier = composed {
    val coroutineScope = rememberCoroutineScope()
    val overscrollOffset = remember { Animatable(0f) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                // If already in overscroll, consume scroll to move back toward zero
                val current = overscrollOffset.value
                if (current != 0f) {
                    val delta = available.y
                    // Check if scroll is pulling back toward neutral
                    if ((current > 0 && delta < 0) || (current < 0 && delta > 0)) {
                        val newOffset = current + delta
                        val consumedY = if (sign(current) != sign(newOffset)) {
                            // Crossed zero
                            coroutineScope.launch { overscrollOffset.snapTo(0f) }
                            -current
                        } else {
                            coroutineScope.launch { overscrollOffset.snapTo(newOffset) }
                            delta
                        }
                        return Offset(0f, consumedY)
                    }
                }
                return Offset.Zero
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                // Leftover scroll at boundary: pull into rubber-band overscroll
                if (source == NestedScrollSource.UserInput && available.y != 0f) {
                    val current = overscrollOffset.value
                    val tension = (1f - (abs(current) / maxOverscroll).coerceIn(0f, 0.85f))
                    val adjustedDelta = available.y * resistanceFactor * tension
                    val target = (current + adjustedDelta).coerceIn(-maxOverscroll, maxOverscroll)
                    coroutineScope.launch {
                        overscrollOffset.snapTo(target)
                    }
                    return Offset(0f, available.y)
                }
                return Offset.Zero
            }

            override suspend fun onPreFling(available: Velocity): Velocity {
                // When release happens while overscrolled, bounce back with spring
                if (overscrollOffset.value != 0f) {
                    overscrollOffset.animateTo(
                        targetValue = 0f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMediumLow
                        )
                    )
                    return available
                }
                return Velocity.Zero
            }

            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                // Boundary fling bounce
                if (abs(available.y) > 200f) {
                    val bounceAmplitude = (available.y * 0.04f).coerceIn(-120f, 120f)
                    overscrollOffset.animateTo(
                        targetValue = bounceAmplitude,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessHigh
                        )
                    )
                    overscrollOffset.animateTo(
                        targetValue = 0f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMediumLow
                        )
                    )
                }
                return Velocity.Zero
            }
        }
    }

    this
        .nestedScroll(nestedScrollConnection)
        .graphicsLayer {
            translationY = overscrollOffset.value
        }
}
