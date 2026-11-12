package com.umerini.clearnote.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.umerini.clearnote.ui.theme.EmeraldMint
import com.umerini.clearnote.ui.theme.PureWhite
import com.umerini.clearnote.ui.theme.RoseQuartz
import com.umerini.clearnote.ui.theme.Slate300
import com.umerini.clearnote.ui.theme.Slate400
import com.umerini.clearnote.ui.theme.liquidGlass

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiquidGlassConfirmDialog(
    title: String,
    message: String,
    confirmText: String = "Delete",
    isDestructive: Boolean = true,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val haptics = rememberLiquidHaptics()

    BasicAlertDialog(
        onDismissRequest = onDismiss
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .liquidGlass(
                    shape = RoundedCornerShape(26.dp),
                    backgroundColor = Color(0xEE0F172A),
                    borderColor = Color(0x4D94A3B8),
                    elevation = 16.dp
                )
                .padding(22.dp)
        ) {
            Column {
                Text(
                    text = title,
                    color = PureWhite,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = message,
                    color = Slate300,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Cancel button
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = {
                                    haptics.perform(HapticType.LIGHT_CLICK)
                                    onDismiss()
                                }
                            )
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = "Cancel",
                            color = Slate400,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Confirm button
                    val actionColor = if (isDestructive) RoseQuartz else ElectricCyan
                    Box(
                        modifier = Modifier
                            .liquidGlass(
                                shape = RoundedCornerShape(14.dp),
                                backgroundColor = actionColor.copy(alpha = 0.85f),
                                borderColor = PureWhite.copy(alpha = 0.5f)
                            )
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = {
                                    haptics.perform(HapticType.HEAVY_BUMP)
                                    onConfirm()
                                }
                            )
                            .padding(horizontal = 18.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = confirmText,
                            color = PureWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiquidGlassInfoDialog(
    totalNotes: Int,
    onDismiss: () -> Unit
) {
    val haptics = rememberLiquidHaptics()

    BasicAlertDialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .liquidGlass(
                    shape = RoundedCornerShape(26.dp),
                    backgroundColor = Color(0xEE0F172A),
                    borderColor = Color(0x4D38BDF8),
                    elevation = 16.dp
                )
                .padding(24.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(EmeraldMint.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = EmeraldMint,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "ClearNote",
                            color = PureWhite,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Liquid Glass Notepad • v1.0.0",
                            color = Slate400,
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Feature Pill 1: Offline Guarantee
                PrivacyFeatureItem(
                    title = "100% Offline-First",
                    desc = "Zero internet permission requested. Your notes never leave this device."
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Feature Pill 2: Room DB Local Storage
                PrivacyFeatureItem(
                    title = "Room Database Engine",
                    desc = "Rock-solid SQLite persistence with instantaneous query speeds."
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Feature Pill 3: Stats
                PrivacyFeatureItem(
                    title = "Local Vault Storage",
                    desc = "$totalNotes notes stored securely on local storage."
                )

                Spacer(modifier = Modifier.height(22.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .liquidGlass(
                            shape = RoundedCornerShape(14.dp),
                            backgroundColor = ElectricCyan.copy(alpha = 0.85f),
                            borderColor = PureWhite.copy(alpha = 0.5f)
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {
                                haptics.perform(HapticType.LIGHT_CLICK)
                                onDismiss()
                            }
                        )
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Got It",
                        color = PureWhite,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun PrivacyFeatureItem(title: String, desc: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .liquidGlass(
                shape = RoundedCornerShape(14.dp),
                backgroundColor = Color(0x221E293B),
                borderColor = Color(0x22FFFFFF)
            )
            .padding(12.dp)
    ) {
        Column {
            Text(
                text = title,
                color = PureWhite,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = desc,
                color = Slate400,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )
        }
    }
}
