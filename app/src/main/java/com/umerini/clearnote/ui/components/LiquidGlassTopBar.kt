package com.umerini.clearnote.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umerini.clearnote.ui.theme.ElectricCyan
import com.umerini.clearnote.ui.theme.PureWhite
import com.umerini.clearnote.ui.theme.Slate400
import com.umerini.clearnote.ui.theme.liquidGlass

@Composable
fun LiquidGlassTopBar(
    title: String = "ClearNote",
    subtitle: String = "100% Offline & Private",
    isGridView: Boolean = true,
    isSearchVisible: Boolean = false,
    onToggleSearch: () -> Unit = {},
    onToggleView: () -> Unit = {},
    onOpenInfo: () -> Unit = {}
) {
    val haptics = rememberLiquidHaptics()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .liquidGlass(
                shape = RoundedCornerShape(26.dp),
                backgroundColor = Color(0x331E293B),
                borderColor = Color(0x3394A3B8),
                elevation = 6.dp
            )
            .padding(horizontal = 18.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // App Title & Tagline with Glass Accent
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Glass indicator orb
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .liquidGlass(
                            shape = CircleShape,
                            backgroundColor = ElectricCyan,
                            borderColor = PureWhite.copy(alpha = 0.8f)
                        )
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = title,
                        color = PureWhite,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.3).sp
                    )
                    Text(
                        text = subtitle,
                        color = Slate400,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }

            // Action Icons Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Search Toggle
                GlassIconButton(
                    icon = Icons.Default.Search,
                    contentDescription = "Search Notes",
                    isActive = isSearchVisible,
                    onClick = {
                        haptics.perform(HapticType.LIGHT_CLICK)
                        onToggleSearch()
                    }
                )

                // Grid / List View Toggle
                GlassIconButton(
                    icon = if (isGridView) Icons.Default.ViewAgenda else Icons.Default.GridView,
                    contentDescription = "Toggle View Mode",
                    onClick = {
                        haptics.perform(HapticType.LIGHT_CLICK)
                        onToggleView()
                    }
                )

                // Info / Privacy Guarantee
                GlassIconButton(
                    icon = Icons.Default.Info,
                    contentDescription = "App Info",
                    onClick = {
                        haptics.perform(HapticType.LIGHT_CLICK)
                        onOpenInfo()
                    }
                )
            }
        }
    }
}

@Composable
fun GlassIconButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    isActive: Boolean = false,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .liquidGlass(
                shape = CircleShape,
                backgroundColor = if (isActive) ElectricCyan.copy(alpha = 0.35f) else Color(0x22FFFFFF),
                borderColor = if (isActive) ElectricCyan.copy(alpha = 0.6f) else Color(0x26FFFFFF)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = if (isActive) ElectricCyan else PureWhite,
            modifier = Modifier.size(19.dp)
        )
    }
}
