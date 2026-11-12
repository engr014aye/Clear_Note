package com.umerini.clearnote.ui.editor

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umerini.clearnote.data.model.GlassThemePalette
import com.umerini.clearnote.data.model.NoteCategory
import com.umerini.clearnote.ui.components.GlassIconButton
import com.umerini.clearnote.ui.components.HapticType
import com.umerini.clearnote.ui.components.LiquidGlassConfirmDialog
import com.umerini.clearnote.ui.components.rememberLiquidHaptics
import com.umerini.clearnote.ui.theme.DeepSlate800
import com.umerini.clearnote.ui.theme.DeepSlate900
import com.umerini.clearnote.ui.theme.ElectricCyan
import com.umerini.clearnote.ui.theme.EmeraldMint
import com.umerini.clearnote.ui.theme.LiquidPhysics
import com.umerini.clearnote.ui.theme.PureWhite
import com.umerini.clearnote.ui.theme.RoseQuartz
import com.umerini.clearnote.ui.theme.Slate300
import com.umerini.clearnote.ui.theme.Slate400
import com.umerini.clearnote.ui.theme.bouncingOverscroll
import com.umerini.clearnote.ui.theme.liquidGlass

@Composable
fun NoteEditorScreen(
    viewModel: NoteEditorViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val haptics = rememberLiquidHaptics()
    val currentTheme = GlassThemePalette.getThemeById(uiState.colorIndex)

    var showDeleteConfirm by remember { mutableStateOf(false) }
    var showCategoryMenu by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isDeleted) {
        if (uiState.isDeleted) {
            onNavigateBack()
        }
    }

    val dynamicBackground = Brush.verticalGradient(
        colors = listOf(
            currentTheme.glassTint.copy(alpha = 0.35f),
            DeepSlate900,
            DeepSlate900
        )
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(dynamicBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
        ) {
            // Liquid Glass Top Navigation Bar
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
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Back Button
                    GlassIconButton(
                        icon = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        onClick = {
                            haptics.perform(HapticType.LIGHT_CLICK)
                            onNavigateBack()
                        }
                    )

                    // Category Pill with Dropdown
                    Box {
                        Box(
                            modifier = Modifier
                                .liquidGlass(
                                    shape = RoundedCornerShape(14.dp),
                                    backgroundColor = currentTheme.glassTint,
                                    borderColor = currentTheme.borderColor
                                )
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = {
                                        haptics.perform(HapticType.SELECTION_TICK)
                                        showCategoryMenu = true
                                    }
                                )
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = uiState.category.displayName,
                                color = currentTheme.primaryColor,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        DropdownMenu(
                            expanded = showCategoryMenu,
                            onDismissRequest = { showCategoryMenu = false },
                            modifier = Modifier.background(DeepSlate800)
                        ) {
                            NoteCategory.entries.filter { it != NoteCategory.ALL }.forEach { cat ->
                                DropdownMenuItem(
                                    text = { Text(cat.displayName, color = PureWhite) },
                                    onClick = {
                                        haptics.perform(HapticType.SELECTION_TICK)
                                        viewModel.setCategory(cat)
                                        showCategoryMenu = false
                                    }
                                )
                            }
                        }
                    }

                    // Action Icons: Pin, Copy, Delete
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Pin Toggle
                        GlassIconButton(
                            icon = Icons.Default.PushPin,
                            contentDescription = "Pin Note",
                            isActive = uiState.isPinned,
                            onClick = {
                                haptics.perform(HapticType.LIGHT_CLICK)
                                viewModel.togglePin()
                            }
                        )

                        // Copy to Clipboard
                        GlassIconButton(
                            icon = Icons.Default.ContentCopy,
                            contentDescription = "Copy Note",
                            onClick = {
                                haptics.perform(HapticType.SUCCESS_DOUBLE)
                                val textToCopy = buildString {
                                    if (uiState.title.isNotBlank()) appendLine(uiState.title)
                                    if (uiState.isChecklistMode) {
                                        uiState.checklistItems.forEach {
                                            appendLine("${if (it.isChecked) "[✓]" else "[ ]"} ${it.text}")
                                        }
                                    } else {
                                        appendLine(uiState.content)
                                    }
                                }
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                clipboard.setPrimaryClip(ClipData.newPlainText("ClearNote", textToCopy))
                                Toast.makeText(context, "Note copied to clipboard", Toast.LENGTH_SHORT).show()
                            }
                        )

                        // Delete
                        GlassIconButton(
                            icon = Icons.Default.Delete,
                            contentDescription = "Delete Note",
                            onClick = {
                                haptics.perform(HapticType.HEAVY_BUMP)
                                showDeleteConfirm = true
                            }
                        )
                    }
                }
            }

            // Glass Color Theme Palette Picker Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                GlassThemePalette.themes.forEach { theme ->
                    val isSelected = theme.id == uiState.colorIndex
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(theme.primaryColor)
                            .border(
                                width = if (isSelected) 2.5.dp else 1.dp,
                                color = if (isSelected) PureWhite else Color.Transparent,
                                shape = CircleShape
                            )
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = {
                                    haptics.perform(HapticType.SELECTION_TICK)
                                    viewModel.setColorIndex(theme.id)
                                }
                            )
                    )
                }
            }

            // Note Content Canvas (Apple-bouncy scroll)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState())
                    .bouncingOverscroll()
            ) {
                Spacer(modifier = Modifier.height(10.dp))

                // Title Input Field
                Box(modifier = Modifier.fillMaxWidth()) {
                    if (uiState.title.isEmpty()) {
                        Text(
                            text = "Title",
                            color = Slate400.copy(alpha = 0.6f),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    BasicTextField(
                        value = uiState.title,
                        onValueChange = { viewModel.updateTitle(it) },
                        textStyle = TextStyle(
                            color = PureWhite,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        cursorBrush = SolidColor(currentTheme.primaryColor),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Body Editor (Checklist Mode vs Standard Note Text)
                if (uiState.isChecklistMode) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        uiState.checklistItems.forEachIndexed { index, item ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Checkbox toggle
                                Icon(
                                    imageVector = if (item.isChecked) Icons.Default.CheckCircle else Icons.Outlined.Circle,
                                    contentDescription = null,
                                    tint = if (item.isChecked) EmeraldMint else Slate400,
                                    modifier = Modifier
                                        .size(22.dp)
                                        .clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = null,
                                            onClick = {
                                                haptics.perform(HapticType.MEDIUM_TAP)
                                                viewModel.toggleChecklistItem(index)
                                            }
                                        )
                                )

                                Spacer(modifier = Modifier.width(10.dp))

                                // Item text field
                                BasicTextField(
                                    value = item.text,
                                    onValueChange = { viewModel.updateChecklistItem(index, it) },
                                    textStyle = TextStyle(
                                        color = if (item.isChecked) Slate400 else PureWhite,
                                        fontSize = 16.sp
                                    ),
                                    cursorBrush = SolidColor(currentTheme.primaryColor),
                                    modifier = Modifier.weight(1f)
                                )

                                // Delete item
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Remove item",
                                    tint = Slate400.copy(alpha = 0.6f),
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = null,
                                            onClick = {
                                                haptics.perform(HapticType.LIGHT_CLICK)
                                                viewModel.removeChecklistItem(index)
                                            }
                                        )
                                )
                            }
                        }

                        // Add new checklist item button
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = {
                                        haptics.perform(HapticType.LIGHT_CLICK)
                                        viewModel.addChecklistItem()
                                    }
                                )
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Item",
                                tint = currentTheme.primaryColor,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Add checklist item…",
                                color = currentTheme.primaryColor,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                } else {
                    Box(modifier = Modifier.fillMaxSize()) {
                        if (uiState.content.isEmpty()) {
                            Text(
                                text = "Start typing your note…",
                                color = Slate400.copy(alpha = 0.5f),
                                fontSize = 16.sp,
                                lineHeight = 24.sp
                            )
                        }
                        BasicTextField(
                            value = uiState.content,
                            onValueChange = { viewModel.updateContent(it) },
                            textStyle = TextStyle(
                                color = Slate300,
                                fontSize = 16.sp,
                                lineHeight = 24.sp
                            ),
                            cursorBrush = SolidColor(currentTheme.primaryColor),
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(60.dp))
            }

            // Bottom Liquid Glass Tool Dock
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .liquidGlass(
                        shape = RoundedCornerShape(22.dp),
                        backgroundColor = Color(0x331E293B),
                        borderColor = Color(0x3394A3B8),
                        elevation = 8.dp
                    )
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Mode Toggle: Checklist / Notes
                    Box(
                        modifier = Modifier
                            .liquidGlass(
                                shape = RoundedCornerShape(12.dp),
                                backgroundColor = if (uiState.isChecklistMode) currentTheme.glassTint else Color(0x22FFFFFF),
                                borderColor = if (uiState.isChecklistMode) currentTheme.borderColor else Color(0x22FFFFFF)
                            )
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = {
                                    haptics.perform(HapticType.LIGHT_CLICK)
                                    viewModel.toggleChecklistMode()
                                }
                            )
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.FormatListBulleted,
                                contentDescription = "Checklist Toggle",
                                tint = if (uiState.isChecklistMode) currentTheme.primaryColor else Slate400,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (uiState.isChecklistMode) "Checklist" else "Text",
                                color = if (uiState.isChecklistMode) currentTheme.primaryColor else Slate400,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    // Metadata: Word count, Char count, Date
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "${uiState.wordCount} words",
                            color = Slate400,
                            fontSize = 11.sp
                        )
                        Text(
                            text = "•",
                            color = Slate400.copy(alpha = 0.5f),
                            fontSize = 11.sp
                        )
                        Text(
                            text = uiState.formattedDate,
                            color = Slate400,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        // Delete Confirmation Dialog
        if (showDeleteConfirm) {
            LiquidGlassConfirmDialog(
                title = "Delete Note?",
                message = "Are you sure you want to permanently delete this note? This action cannot be reversed.",
                confirmText = "Delete Permanently",
                isDestructive = true,
                onConfirm = {
                    showDeleteConfirm = false
                    viewModel.deleteNote()
                },
                onDismiss = {
                    showDeleteConfirm = false
                }
            )
        }
    }
}
