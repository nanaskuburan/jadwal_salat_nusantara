package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.foundation.BorderStroke
import androidx.compose.animation.animateContentSize
import androidx.compose.material3.IconButton
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.WorshipRecord
import com.example.model.AppLanguage
import com.example.model.DayPrayerSchedule
import com.example.model.NgawiKecamatan
import com.example.model.PrayerTimeItem
import com.example.model.PrayerType
import com.example.ui.components.WidgetHomeScreenCard
import com.example.viewmodel.CountdownState

@Composable
fun HomeScreen(
    language: AppLanguage,
    kecamatan: NgawiKecamatan,
    selectedLocation: com.example.model.IndonesiaLocation = com.example.model.IndonesiaLocation.NGAWI_KOTA,
    downloadedLocations: Set<String> = setOf("ngawi_kota"),
    isAutoGpsEnabled: Boolean = true,
    todaySchedule: DayPrayerSchedule?,
    countdownState: CountdownState,
    todayRecord: WorshipRecord,
    isAzanEnabled: Boolean,
    isAzanPlaying: Boolean,
    isOnline: Boolean = false,
    isSimpleUiMode: Boolean = false,
    currentThemeMode: com.example.model.AppThemeMode = com.example.model.AppThemeMode.SYSTEM,
    currentPrimaryColor: Color = Color(0xFF2E7D32),
    boundaryAlert: com.example.viewmodel.PrayerViewModel.BoundaryAlert? = null,
    onDismissBoundaryAlert: () -> Unit = {},
    onThemeModeChange: (com.example.model.AppThemeMode) -> Unit = {},
    onPrimaryColorChange: (Color) -> Unit = {},
    onToggleAzanEnabled: (Boolean) -> Unit,
    onPlayAzan: () -> Unit,
    onStopAzan: () -> Unit,
    onTogglePrayer: (PrayerType) -> Unit,
    onOpenLocationPicker: () -> Unit = {},
    onDownloadLocation: (com.example.model.IndonesiaLocation) -> Unit = {},
    onToggleSimpleUiMode: (Boolean) -> Unit = {},
    onNavigateToTab: (Int) -> Unit = {}
) {
    if (todaySchedule == null) return

    if (isSimpleUiMode) {
        SimpleHomeScreenLayout(
            language = language,
            selectedLocation = selectedLocation,
            todaySchedule = todaySchedule,
            countdownState = countdownState,
            todayRecord = todayRecord,
            isAzanEnabled = isAzanEnabled,
            isAzanPlaying = isAzanPlaying,
            onToggleAzanEnabled = onToggleAzanEnabled,
            onPlayAzan = onPlayAzan,
            onStopAzan = onStopAzan,
            onTogglePrayer = onTogglePrayer,
            onOpenLocationPicker = onOpenLocationPicker,
            onToggleSimpleUiMode = onToggleSimpleUiMode,
            onNavigateToTab = onNavigateToTab
        )
        return
    }

    val isId = language == AppLanguage.INDONESIAN

    androidx.compose.foundation.layout.BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val screenWidth = maxWidth
        val gridColumns = when {
            screenWidth >= 600.dp -> 4
            screenWidth < 320.dp -> 1
            else -> 2
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = if (screenWidth >= 600.dp) 24.dp else 16.dp)
        ) {
            // AZAN PLAYING ACTIVE BANNER
        if (isAzanPlaying) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("azan_playing_banner")
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                contentDescription = "Azan Playing",
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (isId) "Azan Nabawi Berkumandang..." else "صوت الأذان يرتفع الآن...",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                                Text(
                                    text = if (isId) "Telah Masuk Waktu Salat" else "حان الآن وقت الصلاة",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f)
                                )
                            }
                        }
                        Button(
                            onClick = onStopAzan,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                contentColor = MaterialTheme.colorScheme.onErrorContainer
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.VolumeOff, contentDescription = "Stop", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (isId) "Hentikan" else "إيقاف", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // REAL-TIME BOUNDARY TRANSITION ALERT (SE-INDONESIA)
        if (boundaryAlert != null && boundaryAlert.show) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize()
                        .testTag("boundary_transition_alert")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Icon(
                                    imageVector = Icons.Default.DirectionsBus,
                                    contentDescription = "Travel Tracker",
                                    tint = MaterialTheme.colorScheme.tertiary,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isId) "🚌 Info Batas Wilayah Musafir" else "🚌 تنبيه عبور الحدود للمسافر",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.tertiary
                                )
                            }
                            IconButton(
                                onClick = onDismissBoundaryAlert,
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close",
                                    tint = MaterialTheme.colorScheme.tertiary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = if (isId) {
                                "Anda baru saja melintasi batas wilayah dari ${boundaryAlert.previousRegency} memasuki ${boundaryAlert.currentRegency}, Provinsi ${boundaryAlert.province}."
                            } else {
                                "لقد عبرت للتو الحدود من ${boundaryAlert.previousRegency} ودخلت ${boundaryAlert.currentRegency}، محافظة ${boundaryAlert.province}."
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = 20.sp
                        )
                        
                        Spacer(modifier = Modifier.height(10.dp))
                        
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.GpsFixed,
                                contentDescription = "GPS Auto Sync",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isId) "Suhu & Jadwal Salat disinkronkan otomatis tanpa delay!" else "تحديث تلقائي لمواقيت الصلاة والطقس!",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))

            // HERO BANNER & COUNTDOWN CARD
            Card(
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("hero_countdown_card")
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    // Background Hero Image
                    Image(
                        painter = painterResource(id = R.drawable.hero_banner_asset_1786140353158),
                        contentDescription = "Mosque Banner",
                        modifier = Modifier
                            .matchParentSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Gradient Overlay for Readability
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.3f),
                                        Color.Black.copy(alpha = 0.88f)
                                    )
                                )
                            )
                    )

                    // Hero Content
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                                val pasaran = if (isId) 
                                    com.example.calculator.JavaneseCalendar.getPasaran(todaySchedule.year, todaySchedule.month, todaySchedule.day) 
                                else 
                                    com.example.calculator.JavaneseCalendar.getPasaranAr(todaySchedule.year, todaySchedule.month, todaySchedule.day)
                                val gregDateStr = if (isId) todaySchedule.gregorianFormattedId else todaySchedule.gregorianFormattedAr
                                Text(
                                    text = "$gregDateStr \u202A($pasaran)\u202C",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                                Text(
                                    text = if (isId) todaySchedule.hijriFormattedId else todaySchedule.hijriFormattedAr,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFC8D1C9)
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                // AZAN ALARM TOGGLE BADGE
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isAzanEnabled) Color(0xFF5A6B5D) else Color.DarkGray.copy(alpha = 0.6f),
                                    modifier = Modifier.clickable { onToggleAzanEnabled(!isAzanEnabled) }
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (isAzanEnabled) Icons.Default.NotificationsActive else Icons.Default.NotificationsOff,
                                            contentDescription = "Azan Status",
                                            tint = Color.White,
                                            modifier = Modifier.size(15.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = if (isAzanEnabled) "Azan ON" else "Azan OFF",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                // AZAN PLAY/PREVIEW BADGE (MANUAL TEST PLAYBACK)
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isAzanPlaying) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.clickable {
                                        if (isAzanPlaying) onStopAzan() else onPlayAzan()
                                    }
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (isAzanPlaying) Icons.AutoMirrored.Filled.VolumeOff else Icons.AutoMirrored.Filled.VolumeUp,
                                            contentDescription = "Tes Azan",
                                            tint = Color.White,
                                            modifier = Modifier.size(15.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = if (isAzanPlaying) {
                                                if (isId) "Mati" else "إيقاف"
                                            } else {
                                                if (isId) "Tes Azan" else "تجربة"
                                            },
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // COUNTDOWN BOX
                        val nextPrayerName = when (countdownState.nextPrayerType) {
                            PrayerType.SUBUH -> if (isId) "Subuh" else "الفجر"
                            PrayerType.DZUHUR -> if (isId) "Dzuhur" else "الظهر"
                            PrayerType.ASHAR -> if (isId) "Ashar" else "العصر"
                            PrayerType.MAGHRIB -> if (isId) "Maghrib" else "المغرب"
                            PrayerType.ISYA -> if (isId) "Isya" else "العشاء"
                            else -> if (isId) "Salat" else "الصلاة"
                        }

                        Text(
                            text = if (isId) "Menuju Salat $nextPrayerName \u202A(${countdownState.nextPrayerTimeStr} ${selectedLocation.timeZoneName})\u202C"
                            else "الوقت المتبقي لصلاة $nextPrayerName \u202A(${countdownState.nextPrayerTimeStr} ${selectedLocation.timeZoneName})\u202C",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            color = Color.White.copy(alpha = 0.95f)
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Timer Display (Keep LTR sequence so Hours : Minutes : Seconds reads naturally)
                        androidx.compose.runtime.CompositionLocalProvider(
                            androidx.compose.ui.platform.LocalLayoutDirection provides androidx.compose.ui.unit.LayoutDirection.Ltr
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                TimerPill(value = String.format("%02d", countdownState.hoursRemaining), label = if (isId) "Jam" else "ساعة")
                                Text(" : ", style = MaterialTheme.typography.headlineLarge, color = Color.White, fontWeight = FontWeight.Bold)
                                TimerPill(value = String.format("%02d", countdownState.minutesRemaining), label = if (isId) "Menit" else "دقيقة")
                                Text(" : ", style = MaterialTheme.typography.headlineLarge, color = Color.White, fontWeight = FontWeight.Bold)
                                TimerPill(value = String.format("%02d", countdownState.secondsRemaining), label = if (isId) "Detik" else "ثانية")
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // REALTIME CELESTIAL DAY-NIGHT CYCLE ORBIT SIMULATION
                        com.example.ui.components.DayNightCycleView(
                            todaySchedule = todaySchedule,
                            language = language
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // SECTION: TODAY PRAYER TIMES
            Text(
                text = if (isId) "Jadwal Salat Hari Ini (${todaySchedule.locationName})" else "جدول الصلاة اليوم (${todaySchedule.locationName})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }

        // GRID OF TODAY'S PRAYER TIMES
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                todaySchedule.prayerList.chunked(gridColumns).forEach { rowItems ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        rowItems.forEach { prayer ->
                            Box(modifier = Modifier.weight(1f)) {
                                PrayerGridItemCard(
                                    item = prayer,
                                    language = language,
                                    isNext = prayer.type == countdownState.nextPrayerType
                                )
                            }
                        }
                        repeat(gridColumns - rowItems.size) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // INNOVATIVE FEATURE 1: SUNNAH PRAYER TIMINGS (SEPERTIGA MALAM & DHUHA)
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("sunnah_timings_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                            Icon(
                                imageVector = Icons.Default.WbSunny,
                                contentDescription = "Sunnah",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isId) "Panduan Waktu Ibadah Sunnah" else "أوقات العبادات المستحبة",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                maxLines = 2,
                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primary,
                        ) {
                            Text(
                                text = if (isId) "Akurat (${selectedLocation.name})" else "دقيق",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimary,
                                maxLines = 1,
                                softWrap = false,
                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Dynamic Sunnah Calculations based on today's schedule
                    val isyaParts = todaySchedule.isya.split(":")
                    val subuhParts = todaySchedule.subuh.split(":")
                    val terbitParts = todaySchedule.terbit.split(":")
                    val dzuhurParts = todaySchedule.dzuhur.split(":")

                    val isyaMins = (isyaParts.getOrNull(0)?.toIntOrNull() ?: 19) * 60 + (isyaParts.getOrNull(1)?.toIntOrNull() ?: 15)
                    val subuhMins = ((subuhParts.getOrNull(0)?.toIntOrNull() ?: 4) + 24) * 60 + (subuhParts.getOrNull(1)?.toIntOrNull() ?: 20)
                    val nightLengthMins = subuhMins - isyaMins
                    val oneThirdNight = nightLengthMins / 3
                    val lastThirdStartMins = subuhMins - oneThirdNight

                    val lastThirdH = ((lastThirdStartMins / 60) % 24)
                    val lastThirdM = lastThirdStartMins % 60
                    val sepertigaStartStr = String.format("%02d:%02d", lastThirdH, lastThirdM)

                    val terbitMins = (terbitParts.getOrNull(0)?.toIntOrNull() ?: 5) * 60 + (terbitParts.getOrNull(1)?.toIntOrNull() ?: 40)
                    val dhuhaStartMins = terbitMins + 20
                    val dhuhaStartStr = String.format("%02d:%02d", dhuhaStartMins / 60, dhuhaStartMins % 60)

                    val dzuhurMins = (dzuhurParts.getOrNull(0)?.toIntOrNull() ?: 11) * 60 + (dzuhurParts.getOrNull(1)?.toIntOrNull() ?: 45)
                    val dhuhaEndMins = dzuhurMins - 15
                    val dhuhaEndStr = String.format("%02d:%02d", dhuhaEndMins / 60, dhuhaEndMins % 60)

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Card Sepertiga Malam / Tahajud
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.surface,
                            tonalElevation = 2.dp,
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = if (isId) "🌙 Sepertiga Malam (Tahajud)" else "🌙 الثلث الأخير من الليل",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "$sepertigaStartStr - ${todaySchedule.subuh} WIB",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = if (isId) "Waktu terbaik doa dikabulkan" else "أفضل وقت لإجابة الدعاء",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        // Card Waktu Dhuha
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.surface,
                            tonalElevation = 2.dp,
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = if (isId) "☀️ Rentang Waktu Dhuha" else "☀️ وقت صلاة الضحى",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "$dhuhaStartStr - $dhuhaEndStr WIB",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = if (isId) "Pembuka pintu rezeki" else "جالبة للرزق والبركة",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // GPS TRACKER & MUSAFIR STATUS
        if (isAutoGpsEnabled) {
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.GpsFixed,
                                    contentDescription = "GPS Active",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isId) "Pelacak GPS & Batas Wilayah Aktif" else "تتبع نظام تحديد المواقع الجغرافي النشط",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50.dp))
                                    .background(MaterialTheme.colorScheme.primaryContainer)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = if (isId) "MODE MUSAFIR" else "وضع المسافر",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (isId) "Kabupaten / Kota" else "المدينة / المنطقة",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = selectedLocation.name.split(",").lastOrNull()?.trim() ?: selectedLocation.name,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (isId) "Provinsi" else "المحافظة",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = selectedLocation.province,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(10.dp))
                        
                        Divider(color = MaterialTheme.colorScheme.outlineVariant)
                        
                        Spacer(modifier = Modifier.height(10.dp))
                        
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.PinDrop,
                                    contentDescription = "Coordinates",
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Lat: %.4f, Lon: %.4f".format(selectedLocation.lat, selectedLocation.lon),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            
                            Text(
                                text = if (isId) "Zona Waktu: ${selectedLocation.timeZoneName}" else "المنطقة الزمنية: ${selectedLocation.timeZoneName}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }

        // INNOVATIVE FEATURE 2: SUNNI FIQH & FORBIDDEN PRAYER DETECTOR
        item {
            com.example.ui.components.SunniFiqhCompanionCard(
                todaySchedule = todaySchedule,
                language = language,
                todayRecord = todayRecord
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        // DAILY ISLAMIC HADITH / QUOTE
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (isId) "Mutiara Hadis Hari Ini" else "حديث اليوم",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "إِنَّ الصَّلَاةَ كَانَتْ عَلَى الْمُؤْمِنِينَ كِتَابًـا مَّوْقُوتًا",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (isId)
                            "\"Sungguh, salat itu adalah kewajiban yang ditentukan waktunya atas orang-orang yang beriman.\" (QS. An-Nisa: 103)"
                        else
                            "\"إن الصلاة كانت على المؤمنين كتابا موقوتا\" (سورة النساء: ١٠٣)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
}

@Composable
fun TimerPill(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color.White.copy(alpha = 0.25f)
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = Color.White.copy(alpha = 0.8f)
        )
    }
}

@Composable
fun PrayerGridItemCard(
    item: PrayerTimeItem,
    language: AppLanguage,
    isNext: Boolean
) {
    val isId = language == AppLanguage.INDONESIAN

    val name = when (item.type) {
        PrayerType.IMSAK -> if (isId) "Imsak" else "الإمساك"
        PrayerType.SUBUH -> if (isId) "Subuh" else "الفجر"
        PrayerType.TERBIT -> if (isId) "Terbit" else "الشروق"
        PrayerType.DHUHA -> if (isId) "Dhuha" else "الضحى"
        PrayerType.DZUHUR -> if (isId) "Dzuhur" else "الظهر"
        PrayerType.ASHAR -> if (isId) "Ashar" else "العصر"
        PrayerType.MAGHRIB -> if (isId) "Maghrib" else "المغرب"
        PrayerType.ISYA -> if (isId) "Isya" else "العشاء"
    }

    val containerColor = if (isNext) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surface
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isNext) 4.dp else 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("prayer_card_${item.type.name}")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f, fill = false)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(
                            if (isNext) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                        )
                ) {
                    Icon(
                        imageVector = if (item.type == PrayerType.TERBIT || item.type == PrayerType.DHUHA) Icons.Default.WbSunny else Icons.Default.AccessTime,
                        contentDescription = name,
                        tint = if (isNext) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = if (isNext) FontWeight.Bold else FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                    Text(
                        text = item.type.arName,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = item.timeFormatted,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (isNext) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )
        }
    }
}

@Composable
fun WorshipCheckRow(
    name: String,
    isChecked: Boolean,
    onToggle: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (isChecked) FontWeight.Bold else FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f).padding(end = 8.dp)
        )

        Checkbox(
            checked = isChecked,
            onCheckedChange = { onToggle() },
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}

@Composable
fun SimpleHomeScreenLayout(
    language: AppLanguage,
    selectedLocation: com.example.model.IndonesiaLocation,
    todaySchedule: DayPrayerSchedule,
    countdownState: CountdownState,
    todayRecord: WorshipRecord,
    isAzanEnabled: Boolean,
    isAzanPlaying: Boolean,
    onToggleAzanEnabled: (Boolean) -> Unit,
    onPlayAzan: () -> Unit,
    onStopAzan: () -> Unit,
    onTogglePrayer: (PrayerType) -> Unit,
    onOpenLocationPicker: () -> Unit,
    onToggleSimpleUiMode: (Boolean) -> Unit,
    onNavigateToTab: (Int) -> Unit
) {
    val isId = language == AppLanguage.INDONESIAN

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // AZAN PLAYING BANNER (Only when active)
        if (isAzanPlaying) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                contentDescription = "Azan",
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = if (isId) "Suara Azan Berkumandang" else "صوت الأذان يرتفع الآن",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                                Text(
                                    text = if (isId) "Ketuk tombol untuk mematikan" else "اضغط لإيقاف الأذان",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f)
                                )
                            }
                        }
                        Button(
                            onClick = onStopAzan,
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text(if (isId) "Hentikan" else "إيقاف", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
        }

        // TANGGAL MASEHI, PASARAN JAWA, & TANGGAL HIJRIAH
        item {
            val pasaran = if (isId) 
                com.example.calculator.JavaneseCalendar.getPasaran(todaySchedule.year, todaySchedule.month, todaySchedule.day) 
            else 
                com.example.calculator.JavaneseCalendar.getPasaranAr(todaySchedule.year, todaySchedule.month, todaySchedule.day)

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "📅 ${if (isId) todaySchedule.gregorianFormattedId else todaySchedule.gregorianFormattedAr} • $pasaran",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer
                    ) {
                        Text(
                            text = "🌙 ${if (isId) todaySchedule.hijriFormattedId else todaySchedule.hijriFormattedAr}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        // COUNTDOWN NEXT PRAYER CARD (BIG TIMER)
        item {
            val nextPrayerName = if (isId) (countdownState.nextPrayerType?.idName ?: "Subuh") else (countdownState.nextPrayerType?.arName ?: "الفجر")
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("simple_countdown_hero_card")
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (isId) "⏳ SALAT BERIKUTNYA" else "⏳ الصلاة القادمة",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "$nextPrayerName • ${countdownState.nextPrayerTimeStr}",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (isId) {
                            "${countdownState.hoursRemaining}j ${countdownState.minutesRemaining}m ${countdownState.secondsRemaining}d lagi"
                        } else {
                            "متبقي ${countdownState.hoursRemaining}س ${countdownState.minutesRemaining}د ${countdownState.secondsRemaining}ث"
                        },
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.95f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    LinearProgressIndicator(
                        progress = { countdownState.progress },
                        color = MaterialTheme.colorScheme.onPrimary,
                        trackColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.3f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp))
                    )
                }
            }
        }

        // JADWAL SALAT HARI INI (EXTRA LARGE CARDS)
        item {
            Text(
                text = if (isId) "📋 Jadwal Salat Hari Ini" else "📋 مواقيت الصلاة اليوم",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        for (item in todaySchedule.prayerList) {
            item {
                val isChecked = when (item.type) {
                    PrayerType.IMSAK -> false
                    PrayerType.SUBUH -> todayRecord.subuh
                    PrayerType.TERBIT -> false
                    PrayerType.DHUHA -> todayRecord.dhuha
                    PrayerType.DZUHUR -> todayRecord.dzuhur
                    PrayerType.ASHAR -> todayRecord.ashar
                    PrayerType.MAGHRIB -> todayRecord.maghrib
                    PrayerType.ISYA -> todayRecord.isya
                }

                val isNext = countdownState.nextPrayerType == item.type

                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isNext) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = if (isNext) 4.dp else 1.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("simple_prayer_item_${item.type.name.lowercase()}")
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = if (isId) item.type.idName else item.type.arName,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isNext) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = item.type.arName,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "\u202A${item.timeFormatted} ${selectedLocation.timeZoneName}\u202C",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        if (item.type != PrayerType.TERBIT && item.type != PrayerType.IMSAK) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isChecked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { onTogglePrayer(item.type) }
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Sudah Salat",
                                        tint = if (isChecked) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (isChecked) (if (isId) "Sudah" else "تمت") else (if (isId) "Belum" else "لم تتم"),
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isChecked) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
