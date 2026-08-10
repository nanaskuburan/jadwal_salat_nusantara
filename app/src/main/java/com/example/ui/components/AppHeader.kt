package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.NgawiKecamatan

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppHeader(
    language: AppLanguage,
    selectedKecamatan: NgawiKecamatan,
    selectedLocation: com.example.model.IndonesiaLocation = com.example.model.IndonesiaLocation.NGAWI_KOTA,
    isAutoGpsEnabled: Boolean = true,
    isSimpleUiMode: Boolean = false,
    onLanguageChange: (AppLanguage) -> Unit,
    onKecamatanChange: (NgawiKecamatan) -> Unit,
    onOpenLocationPicker: () -> Unit = {},
    onAutoGpsToggle: (Boolean) -> Unit = {},
    onToggleSimpleUiMode: (Boolean) -> Unit = {}
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        ),
        title = {
            Column {
                Text(
                    text = if (language == AppLanguage.INDONESIAN) "Jadwal Salat Nusantara" else "مواقيت الصلاة في إندونيسيا",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.testTag("app_title")
                )
                
                // Location Selector Pill
                androidx.compose.runtime.CompositionLocalProvider(androidx.compose.ui.platform.LocalLayoutDirection provides androidx.compose.ui.unit.LayoutDirection.Ltr) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(top = 2.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isAutoGpsEnabled) MaterialTheme.colorScheme.primaryContainer 
                                else MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f)
                            )
                            .clickable { onOpenLocationPicker() }
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                            .testTag("kecamatan_selector")
                    ) {
                        Icon(
                            imageVector = if (isAutoGpsEnabled) Icons.Default.GpsFixed else Icons.Default.LocationOn,
                            contentDescription = "Lokasi",
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "\u202A${selectedLocation.name} (${selectedLocation.timeZoneName})\u202C",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Pilih Lokasi",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        },
        actions = {
            // UI Mode Toggle Button (Mode Simpel vs Mode Lengkap)
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = if (isSimpleUiMode) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.surfaceContainerHigh,
                modifier = Modifier
                    .padding(end = 6.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { onToggleSimpleUiMode(!isSimpleUiMode) }
                    .testTag("ui_mode_toggle_button")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = if (language == AppLanguage.INDONESIAN) {
                            if (isSimpleUiMode) "⚡ Simpel" else "✨ Lengkap"
                        } else {
                            if (isSimpleUiMode) "⚡ بسيط" else "✨ شامل"
                        },
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (isSimpleUiMode) MaterialTheme.colorScheme.onTertiaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Language Switcher Button
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.secondaryContainer,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .clickable {
                        val nextLang = if (language == AppLanguage.INDONESIAN) AppLanguage.ARABIC else AppLanguage.INDONESIAN
                        onLanguageChange(nextLang)
                    }
                    .testTag("language_toggle_button")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = "Ubah Bahasa",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (language == AppLanguage.INDONESIAN) "ID" else "العربية",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }
    )
}
