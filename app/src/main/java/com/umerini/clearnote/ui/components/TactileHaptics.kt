package com.umerini.clearnote.ui.components

import android.content.Context
import android.os.Build
import android.os.CombinedVibration
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback

enum class HapticType {
    LIGHT_CLICK,
    MEDIUM_TAP,
    HEAVY_BUMP,
    SUCCESS_DOUBLE,
    SELECTION_TICK
}

class LiquidHaptics(
    private val context: Context,
    private val composeHaptic: androidx.compose.ui.hapticfeedback.HapticFeedback
) {
    private val vibrator: Vibrator? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vibratorManager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    }

    fun perform(type: HapticType) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && vibrator != null && vibrator?.hasVibrator() == true) {
                val effect = when (type) {
                    HapticType.LIGHT_CLICK -> VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK)
                    HapticType.MEDIUM_TAP -> VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK)
                    HapticType.HEAVY_BUMP -> VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK)
                    HapticType.SUCCESS_DOUBLE -> VibrationEffect.createPredefined(VibrationEffect.EFFECT_DOUBLE_CLICK)
                    HapticType.SELECTION_TICK -> VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK)
                }
                vibrator?.vibrate(effect)
            } else {
                when (type) {
                    HapticType.LIGHT_CLICK, HapticType.SELECTION_TICK -> {
                        composeHaptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    }
                    HapticType.MEDIUM_TAP -> {
                        composeHaptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    }
                    HapticType.HEAVY_BUMP, HapticType.SUCCESS_DOUBLE -> {
                        composeHaptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    }
                }
            }
        } catch (_: Exception) {
            // Fallback gracefully if vibration is disabled on device
            try {
                composeHaptic.performHapticFeedback(HapticFeedbackType.LongPress)
            } catch (_: Exception) {}
        }
    }
}

@Composable
fun rememberLiquidHaptics(): LiquidHaptics {
    val context = LocalContext.current
    val composeHaptic = LocalHapticFeedback.current
    return remember(context, composeHaptic) {
        LiquidHaptics(context, composeHaptic)
    }
}
