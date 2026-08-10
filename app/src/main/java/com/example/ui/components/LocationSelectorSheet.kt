package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.IndonesiaLocation

import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationSelectorSheet(
    selectedLocation: IndonesiaLocation,
    defaultLocation: IndonesiaLocation = IndonesiaLocation.NGAWI_KOTA,
    downloadedLocations: Set<String>,
    isAutoGpsEnabled: Boolean,
    userGpsCoords: Pair<Double, Double>? = null,
    language: AppLanguage,
    onSelectLocation: (IndonesiaLocation) -> Unit,
    onSetDefaultLocation: (IndonesiaLocation) -> Unit = {},
    onDownloadLocation: (IndonesiaLocation) -> Unit,
    onToggleAutoGps: (Boolean) -> Unit,
    onRequestGpsRefresh: () -> Unit = {},
    onDismiss: () -> Unit
) {
    val isId = language == AppLanguage.INDONESIAN
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var searchQuery by remember { mutableStateOf("") }
    var selectedTab by remember { mutableIntStateOf(if (selectedLocation.isNgawiRegion) 0 else 1) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Header Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isId) "Pilih Lokasi & Jadwal Salat" else "اختر الموقع وجدول الصلاة",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // AUTO GPS TOGGLE & DETECT CARD
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
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
                                imageVector = Icons.Default.MyLocation,
                                contentDescription = "GPS",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (isId) "Deteksi Presisi GPS Otomatis" else "التحديد التلقائي للموقع",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = if (isId) "Akurat berdasarkan koordinat lintang & bujur GPS real-time" else "تحديد دقيق بناءً على إحداثيات GPS الفعلية",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isAutoGpsEnabled) MaterialTheme.colorScheme.primary else Color.Gray.copy(alpha = 0.3f),
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { onToggleAutoGps(!isAutoGpsEnabled) }
                        ) {
                            Text(
                                text = if (isAutoGpsEnabled) "GPS ON" else "OFF",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (isAutoGpsEnabled) Color.White else Color.DarkGray,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }

                    // Display Current GPS Coordinates & Quick Refresh Button
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                            val coordsText = if (userGpsCoords != null) {
                                "📍 Lat: ${String.format("%.4f", userGpsCoords.first)}, Lon: ${String.format("%.4f", userGpsCoords.second)}"
                            } else {
                                if (isId) "📍 Meminta sinyal koordinat GPS..." else "📍 جاري البحث عن GPS..."
                            }
                            Text(
                                text = coordsText,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Button(
                            onClick = {
                                onRequestGpsRefresh()
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MyLocation,
                                contentDescription = "Deteksi",
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isId) "Deteksi Sekarang" else "تحديد الآن",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Search Input
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text(
                        text = if (isId) "Cari kota, kabupaten, atau kecamatan seluruh Indonesia..." else "ابحث عن المدينة أو المنطقة في جميع أنحاء إندونيسيا...",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Clear")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Category Tabs
            androidx.compose.runtime.CompositionLocalProvider(androidx.compose.ui.platform.LocalLayoutDirection provides androidx.compose.ui.unit.LayoutDirection.Ltr) {
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    modifier = Modifier.clip(RoundedCornerShape(12.dp))
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = {
                            Text(
                                text = if (isId) "Kab. Ngawi (100% Offline)" else "محافظة نغاوي (دون إنترنت)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = {
                            Text(
                                text = if (isId) "Seluruh Indonesia" else "جميع مناطق إندونيسيا",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Filtered Locations
            val listToDisplay = remember(selectedTab, searchQuery) {
                if (searchQuery.isNotBlank()) {
                    IndonesiaLocation.ALL_INDONESIA_LOCATIONS.filter {
                        it.name.contains(searchQuery, ignoreCase = true) ||
                                it.province.contains(searchQuery, ignoreCase = true) ||
                                it.id.contains(searchQuery, ignoreCase = true)
                    }
                } else if (selectedTab == 0) {
                    IndonesiaLocation.NGAWI_KECAMATAN_LIST
                } else {
                    IndonesiaLocation.INDONESIA_CITIES
                }
            }

            if (listToDisplay.isEmpty() && searchQuery.isNotBlank()) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = if (isId) "📍 Lokasi tidak ditemukan" else "📍 لم يتم العثور على الموقع",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isId) "Tidak ada kota/kecamatan bernama \"$searchQuery\"" else "لا توجد مدينة أو منطقة بهذا الاسم",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .testTag("location_selector_list")
                ) {
                itemsIndexed(listToDisplay, key = { index, loc -> "${loc.id}_$index" }) { _, loc ->
                    val isSelected = selectedLocation.id == loc.id
                    val isDefault = defaultLocation.id == loc.id
                    val isDownloaded = loc.isNgawiRegion || downloadedLocations.contains(loc.id)

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSelected) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = if (isSelected) 2.dp else 0.5.dp,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .clickable {
                                onSelectLocation(loc)
                                onDismiss()
                            }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.padding(14.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = loc.name,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1,
                                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(1f, fill = false)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                                    ) {
                                        Text(
                                            text = loc.timeZoneName,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary,
                                            maxLines = 1,
                                            softWrap = false,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp)
                                        )
                                    }
                                }

                                Text(
                                    text = "${loc.province} • Lat ${String.format("%.2f", loc.lat)}, Lon ${String.format("%.2f", loc.lon)}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                // DEFAULT LOCATION BADGE / BUTTON
                                if (isDefault) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color(0xFFFFB300).copy(alpha = 0.2f)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Star,
                                                contentDescription = "Default",
                                                tint = Color(0xFFE65100),
                                                modifier = Modifier.size(12.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = if (isId) "Lokasi Utama (Default)" else "الموقع الرئيسي الافتراضي",
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFFE65100),
                                                fontSize = 10.sp
                                            )
                                        }
                                    }
                                } else {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                                        modifier = Modifier.clickable {
                                            onSetDefaultLocation(loc)
                                        }
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.StarBorder,
                                                contentDescription = "Set Default",
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(12.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = if (isId) "Jadikan Utama (Default)" else "تعيين كافتراضي",
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Medium,
                                                color = MaterialTheme.colorScheme.primary,
                                                fontSize = 10.sp
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (isDownloaded) {
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = Color(0xFF2E7D32).copy(alpha = 0.15f)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = "Offline Available",
                                                tint = Color(0xFF2E7D32),
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = if (isId) {
                                                    if (loc.isNgawiRegion) "Offline Ngawi" else "Tersimpan"
                                                } else {
                                                    if (loc.isNgawiRegion) "محفوظ (نغاوي)" else "محفوظ"
                                                },
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF2E7D32)
                                            )
                                        }
                                    }
                                } else {
                                    Button(
                                        onClick = { onDownloadLocation(loc) },
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                                        modifier = Modifier.height(34.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CloudDownload,
                                            contentDescription = "Download",
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = if (isId) "Unduh" else "تحميل",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
}
