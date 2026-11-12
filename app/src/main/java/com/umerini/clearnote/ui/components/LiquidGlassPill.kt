package com.umerini.clearnote.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umerini.clearnote.ui.theme.ElectricCyan
import com.umerini.clearnote.ui.theme.LiquidPhysics
import com.umerini.clearnote.ui.theme.PureWhite
import com.umerini.clearnote.ui.theme.Slate400
import com.umerini.clearnote.ui.theme.liquidGlass

@Composable
fun LiquidGlassPill(
    label: String,
    isSelected: Boolean,
    count: Int? = null,
    onClick: () -> Unit
) {
    val haptics = rememberLiquidHaptics()
    val interactionSource = remember { MutableInteractionSource() }

    val bgColor by animateColorAsState(
        targetValue = if (isSelected) ElectricCyan.copy(alpha = 0.28f) else Color(0x221E293B),
        animationSpec = LiquidPhysics.tactileSnappy(),
        label = "pillBgColor"
    )

    val borderColor by animateColorAsState(
        targetValue = if (isSelected) ElectricCyan.copy(alpha = 0.85f) else Color(0x3394A3B8),
        animationSpec = LiquidPhysics.tactileSnappy(),
        label = "pillBorderColor"
    )

    val elevation by animateDpAsState(
        targetValue = if (isSelected) 4.dp else 0.dp,
        animationSpec = LiquidPhysics.appleBouncy(),
        label = "pillElevation"
    )

    Box(
        modifier = Modifier
            .liquidGlass(
                shape = RoundedCornerShape(16.dp),
                backgroundColor = bgColor,
                borderColor = borderColor,
                elevation = elevation
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    haptics.perform(HapticType.SELECTION_TICK)
                    onClick()
                }
            )
            .padding(horizontal = 14.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .liquidGlass(
                            shape = CircleShape,
                            backgroundColor = ElectricCyan,
                            borderColor = PureWhite
                        )
                )
                Spacer(modifier = Modifier.width(6.dp))
            }

            Text(
                text = label,
                color = if (isSelected) PureWhite else Slate400,
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
            )

            if (count != null && count > 0) {
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "$count",
                    color = if (isSelected) PureWhite.copy(alpha = 0.9f) else Slate400.copy(alpha = 0.7f),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
