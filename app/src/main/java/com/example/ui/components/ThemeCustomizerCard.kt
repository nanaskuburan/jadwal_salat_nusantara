package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.material.icons.filled.Brightness2
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.SettingsSuggest
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.AppThemeMode

val PRESET_THEME_COLORS = listOf(
    Color(0xFF2E7D32) to "Emerald",
    Color(0xFF1565C0) to "Sapphire",
    Color(0xFF6A1B9A) to "Purple",
    Color(0xFFD84315) to "Amber",
    Color(0xFFC62828) to "Ruby",
    Color(0xFF00695C) to "Teal",
    Color(0xFF37474F) to "Midnight"
)

@Composable
fun ThemeCustomizerCard(
    language: AppLanguage,
    currentThemeMode: AppThemeMode,
    currentPrimaryColor: Color,
    onThemeModeChange: (AppThemeMode) -> Unit,
    onPrimaryColorChange: (Color) -> Unit,
    modifier: Modifier = Modifier
) {
    val isId = language == AppLanguage.INDONESIAN
    var showRgbPicker by remember { mutableStateOf(false) }

    val initialArgb = currentPrimaryColor.toArgb()
    var redValue by remember(currentPrimaryColor) { mutableFloatStateOf(android.graphics.Color.red(initialArgb).toFloat()) }
    var greenValue by remember(currentPrimaryColor) { mutableFloatStateOf(android.graphics.Color.green(initialArgb).toFloat()) }
    var blueValue by remember(currentPrimaryColor) { mutableFloatStateOf(android.graphics.Color.blue(initialArgb).toFloat()) }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("theme_customizer_card")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Palette,
                        contentDescription = "Tema UI",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isId) "Pengatur Mode UI & Warna Custom" else "مظهر التطبيق والألوان",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 2,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = if (isId) "Unlimited" else "غير محدود",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        maxLines = 1,
                        softWrap = false,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 1. SWITCH MODE GELAP / TERANG / SISTEM
            Text(
                text = if (isId) "Mode Tampilan UI:" else "وضع المظهر:",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // LIGHT MODE CHIP
                val isLight = currentThemeMode == AppThemeMode.LIGHT
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isLight) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onThemeModeChange(AppThemeMode.LIGHT) }
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LightMode,
                            contentDescription = "Light",
                            tint = if (isLight) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isId) "Terang" else "فاتح",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = if (isLight) FontWeight.Bold else FontWeight.Normal,
                            color = if (isLight) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                // DARK MODE CHIP
                val isDark = currentThemeMode == AppThemeMode.DARK
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isDark) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onThemeModeChange(AppThemeMode.DARK) }
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DarkMode,
                            contentDescription = "Dark",
                            tint = if (isDark) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isId) "Gelap" else "داكن",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = if (isDark) FontWeight.Bold else FontWeight.Normal,
                            color = if (isDark) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                // SYSTEM MODE CHIP
                val isSystem = currentThemeMode == AppThemeMode.SYSTEM
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSystem) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onThemeModeChange(AppThemeMode.SYSTEM) }
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.SettingsSuggest,
                            contentDescription = "System",
                            tint = if (isSystem) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isId) "Auto" else "تلقائي",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = if (isSystem) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSystem) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 2. PRESET WARMA PALETTE & UNLIMITED RGB PICKER TOGGLE
            Text(
                text = if (isId) "Pilih Warna Akses Aksen UI:" else "اختر اللون الرئيسي:",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Preset Swatches
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                PRESET_THEME_COLORS.forEach { (color, label) ->
                    val isSelected = currentPrimaryColor == color
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = if (isSelected) 3.dp else 0.dp,
                                color = if (isSelected) MaterialTheme.colorScheme.onSurface else Color.Transparent,
                                shape = CircleShape
                            )
                            .clickable { onPrimaryColorChange(color) }
                    ) {
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Unlimited Custom RGB Picker Toggle Button
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showRgbPicker = !showRgbPicker }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.ColorLens,
                            contentDescription = "Custom RGB Color",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isId) "Custom RGB Warna Bebas (Unlimited)" else "تخصيص اللون الفريد",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Icon(
                        imageVector = if (showRgbPicker) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = "Expand",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            AnimatedVisibility(visible = showRgbPicker) {
                Column(modifier = Modifier.padding(top = 10.dp)) {
                    // RGB Sliders
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Red Slider
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "R", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = Color.Red, modifier = Modifier.width(20.dp))
                            Slider(
                                value = redValue,
                                onValueChange = {
                                    redValue = it
                                    val custom = Color(redValue.toInt(), greenValue.toInt(), blueValue.toInt())
                                    onPrimaryColorChange(custom)
                                },
                                valueRange = 0f..255f,
                                colors = SliderDefaults.colors(thumbColor = Color.Red, activeTrackColor = Color.Red),
                                modifier = Modifier.weight(1f)
                            )
                            Text(text = "${redValue.toInt()}", style = MaterialTheme.typography.labelSmall, modifier = Modifier.width(30.dp))
                        }

                        // Green Slider
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "G", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = Color.Green, modifier = Modifier.width(20.dp))
                            Slider(
                                value = greenValue,
                                onValueChange = {
                                    greenValue = it
                                    val custom = Color(redValue.toInt(), greenValue.toInt(), blueValue.toInt())
                                    onPrimaryColorChange(custom)
                                },
                                valueRange = 0f..255f,
                                colors = SliderDefaults.colors(thumbColor = Color.Green, activeTrackColor = Color.Green),
                                modifier = Modifier.weight(1f)
                            )
                            Text(text = "${greenValue.toInt()}", style = MaterialTheme.typography.labelSmall, modifier = Modifier.width(30.dp))
                        }

                        // Blue Slider
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "B", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = Color.Blue, modifier = Modifier.width(20.dp))
                            Slider(
                                value = blueValue,
                                onValueChange = {
                                    blueValue = it
                                    val custom = Color(redValue.toInt(), greenValue.toInt(), blueValue.toInt())
                                    onPrimaryColorChange(custom)
                                },
                                valueRange = 0f..255f,
                                colors = SliderDefaults.colors(thumbColor = Color.Blue, activeTrackColor = Color.Blue),
                                modifier = Modifier.weight(1f)
                            )
                            Text(text = "${blueValue.toInt()}", style = MaterialTheme.typography.labelSmall, modifier = Modifier.width(30.dp))
                        }
                    }
                }
            }
        }
    }
}
