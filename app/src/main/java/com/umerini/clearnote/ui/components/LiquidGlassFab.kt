package com.umerini.clearnote.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umerini.clearnote.ui.theme.CyanGlow
import com.umerini.clearnote.ui.theme.ElectricCyan
import com.umerini.clearnote.ui.theme.LiquidPhysics
import com.umerini.clearnote.ui.theme.PureWhite
import com.umerini.clearnote.ui.theme.liquidGlass

@Composable
fun LiquidGlassFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    label: String = "New Note"
) {
    val haptics = rememberLiquidHaptics()
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = LiquidPhysics.appleBouncy(),
        label = "fabScale"
    )

    Box(
        modifier = modifier
            .scale(scale)
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(28.dp),
                spotColor = CyanGlow.copy(alpha = 0.5f),
                ambientColor = Color(0x30000000)
            )
            .liquidGlass(
                shape = RoundedCornerShape(28.dp),
                backgroundColor = ElectricCyan.copy(alpha = 0.85f),
                borderColor = PureWhite.copy(alpha = 0.75f),
                borderWidth = 1.2.dp
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    haptics.perform(HapticType.MEDIUM_TAP)
                    onClick()
                }
            )
            .padding(horizontal = 20.dp, vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = label,
                tint = PureWhite,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                color = PureWhite,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
