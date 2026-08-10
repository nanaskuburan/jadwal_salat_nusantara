package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculator.JavaneseCalendar
import com.example.calculator.PrayerTimeCalculator
import com.example.model.AppLanguage
import com.example.model.DayPrayerSchedule
import com.example.model.IslamicHoliday

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    language: AppLanguage,
    selectedYear: Int,
    selectedMonth: Int,
    monthlySchedules: List<DayPrayerSchedule>,
    holidays: List<IslamicHoliday>,
    selectedLocation: com.example.model.IndonesiaLocation = com.example.model.IndonesiaLocation.NGAWI_KOTA,
    isSimpleUiMode: Boolean = false,
    onMonthYearChange: (Int, Int) -> Unit
) {
    val isId = language == AppLanguage.INDONESIAN
    val context = LocalContext.current

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var yearDropdownExpanded by remember { mutableStateOf(false) }
    var monthDropdownExpanded by remember { mutableStateOf(false) }

    // Real device system date (synchronized with Android gadget clock)
    val todayCal = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone(selectedLocation.timeZoneId))
    val currentRealYear = todayCal.get(java.util.Calendar.YEAR)
    val currentRealMonth = todayCal.get(java.util.Calendar.MONTH) + 1
    val currentRealDay = todayCal.get(java.util.Calendar.DAY_OF_MONTH)

    // Toggle between Day-Card View (Senior/Minimalist) & Full Table View
    var isCardViewMode by remember(isSimpleUiMode) { mutableStateOf(isSimpleUiMode) }

    // Auto-sync selected day to today's date if viewing current real month & year
    val initialDayIndex = remember(selectedYear, selectedMonth, monthlySchedules) {
        if (selectedYear == currentRealYear && selectedMonth == currentRealMonth) {
            (currentRealDay - 1).coerceIn(0, (monthlySchedules.size - 1).coerceAtLeast(0))
        } else {
            0
        }
    }

    var selectedDayIndex by remember(selectedYear, selectedMonth, monthlySchedules) {
        mutableIntStateOf(initialDayIndex)
    }

    val dayListState = rememberLazyListState()

    LaunchedEffect(selectedDayIndex) {
        if (monthlySchedules.isNotEmpty() && selectedDayIndex in monthlySchedules.indices) {
            dayListState.animateScrollToItem(selectedDayIndex)
        }
    }

    val yearsList = (2026..2036).toList()
    val monthsList = (1..12).toList()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // TAB SWITCHER: JADWAL BULANAN vs HARI BESAR ISLAM
        androidx.compose.runtime.CompositionLocalProvider(androidx.compose.ui.platform.LocalLayoutDirection provides androidx.compose.ui.unit.LayoutDirection.Ltr) {
            PrimaryTabRow(selectedTabIndex = selectedTabIndex) {
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = { selectedTabIndex = 0 },
                    text = {
                        Text(
                            text = if (isId) "Jadwal Salat" else "جدول الصلاة",
                            style = if (isSimpleUiMode) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    modifier = Modifier.testTag("tab_monthly_schedule")
                )
                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 },
                    text = {
                        Text(
                            text = if (isId) "Ramadhan & Hari Besar" else "الأعياد والمناسبات",
                            style = if (isSimpleUiMode) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    modifier = Modifier.testTag("tab_islamic_holidays")
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // MONTH & YEAR SELECTOR BAR
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = if (isSimpleUiMode) 8.dp else 6.dp)
            ) {
                IconButton(
                    onClick = {
                        var newM = selectedMonth - 1
                        var newY = selectedYear
                        if (newM < 1) {
                            newM = 12
                            newY -= 1
                        }
                        if (newY >= 2026) {
                            onMonthYearChange(newY, newM)
                        }
                    },
                    modifier = Modifier.size(if (isSimpleUiMode) 48.dp else 40.dp)
                ) {
                    Icon(
                        Icons.Default.ChevronLeft,
                        contentDescription = "Bulan Sebelum",
                        modifier = Modifier.size(if (isSimpleUiMode) 32.dp else 24.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Month Dropdown
                            Box {
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .clickable { monthDropdownExpanded = true }
                                        .padding(
                                            horizontal = if (isSimpleUiMode) 14.dp else 10.dp,
                                            vertical = if (isSimpleUiMode) 8.dp else 4.dp
                                        )
                                        .testTag("select_month_button")
                                ) {
                                    Text(
                                        text = if (isId) PrayerTimeCalculator.getIndonesianMonthName(selectedMonth) else PrayerTimeCalculator.getArabicMonthName(selectedMonth),
                                        style = if (isSimpleUiMode) MaterialTheme.typography.titleLarge else MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }

                                DropdownMenu(
                                    expanded = monthDropdownExpanded,
                                    onDismissRequest = { monthDropdownExpanded = false }
                                ) {
                                    monthsList.forEach { m ->
                                        val mName = if (isId) PrayerTimeCalculator.getIndonesianMonthName(m) else PrayerTimeCalculator.getArabicMonthName(m)
                                        DropdownMenuItem(
                                            text = { Text(mName, fontWeight = if (m == selectedMonth) FontWeight.Bold else FontWeight.Normal) },
                                            onClick = {
                                                onMonthYearChange(selectedYear, m)
                                                monthDropdownExpanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // Year Dropdown (10-Year 2026-2036)
                            Box {
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .clickable { yearDropdownExpanded = true }
                                        .padding(
                                            horizontal = if (isSimpleUiMode) 14.dp else 10.dp,
                                            vertical = if (isSimpleUiMode) 8.dp else 4.dp
                                        )
                                        .testTag("select_year_button")
                                ) {
                                    Text(
                                        text = "$selectedYear",
                                        style = if (isSimpleUiMode) MaterialTheme.typography.titleLarge else MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                }

                                DropdownMenu(
                                    expanded = yearDropdownExpanded,
                                    onDismissRequest = { yearDropdownExpanded = false }
                                ) {
                                    yearsList.forEach { y ->
                                        DropdownMenuItem(
                                            text = { Text("$y", fontWeight = if (y == selectedYear) FontWeight.Bold else FontWeight.Normal) },
                                            onClick = {
                                                onMonthYearChange(y, selectedMonth)
                                                yearDropdownExpanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // TODAY BUTTON (Reset to Android gadget current time)
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.tertiaryContainer,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable {
                                        onMonthYearChange(currentRealYear, currentRealMonth)
                                        selectedDayIndex = (currentRealDay - 1).coerceIn(0, (monthlySchedules.size - 1).coerceAtLeast(0))
                                    }
                                    .padding(
                                        horizontal = if (isSimpleUiMode) 10.dp else 8.dp,
                                        vertical = if (isSimpleUiMode) 8.dp else 4.dp
                                    )
                                    .testTag("today_reset_button")
                            ) {
                                Text(
                                    text = if (isId) "Hari Ini 🎯" else "اليوم 🎯",
                                    style = if (isSimpleUiMode) MaterialTheme.typography.titleMedium else MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer
                                )
                            }
                        }

                        // Display Hijri Month Span Subtitle (e.g. 🌙 Safar - Rabi'ul Awal 1448 H)
                        if (monthlySchedules.isNotEmpty()) {
                            val firstHijri = if (isId) monthlySchedules.first().hijriFormattedId else monthlySchedules.first().hijriFormattedAr
                            val lastHijri = if (isId) monthlySchedules.last().hijriFormattedId else monthlySchedules.last().hijriFormattedAr
                            Text(
                                text = "🌙 $firstHijri  ➔  $lastHijri",
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }

                IconButton(
                    onClick = {
                        var newM = selectedMonth + 1
                        var newY = selectedYear
                        if (newM > 12) {
                            newM = 1
                            newY += 1
                        }
                        if (newY <= 2036) {
                            onMonthYearChange(newY, newM)
                        }
                    },
                    modifier = Modifier.size(if (isSimpleUiMode) 48.dp else 40.dp)
                ) {
                    Icon(
                        Icons.Default.ChevronRight,
                        contentDescription = "Bulan Berikut",
                        modifier = Modifier.size(if (isSimpleUiMode) 32.dp else 24.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (selectedTabIndex == 0) {
            // SUB-HEADER: LOCATION & VIEW TOGGLE
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "\u202A📍 ${selectedLocation.name} (${selectedLocation.timeZoneName})\u202C",
                    style = if (isSimpleUiMode) MaterialTheme.typography.titleMedium else MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // TOGGLE CARD vs TABLE VIEW
                    IconButton(
                        onClick = { isCardViewMode = !isCardViewMode },
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.tertiaryContainer)
                            .size(36.dp)
                    ) {
                        Icon(
                            imageVector = if (isCardViewMode) Icons.AutoMirrored.Filled.ViewList else Icons.Default.ViewAgenda,
                            contentDescription = "Switch View",
                            tint = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // COPY BUTTON
                    Button(
                        onClick = {
                            val sb = StringBuilder()
                            sb.append("Jadwal Salat ${selectedLocation.name} - ${PrayerTimeCalculator.getIndonesianMonthName(selectedMonth)} $selectedYear\n\n")
                            sb.append("Tgl | Subuh | Dzuhur | Ashar | Maghrib | Isya\n")
                            sb.append("-------------------------------------------\n")
                            monthlySchedules.forEach { day ->
                                sb.append("${day.day} | ${day.subuh} | ${day.dzuhur} | ${day.ashar} | ${day.maghrib} | ${day.isya}\n")
                            }
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
                            val clip = ClipData.newPlainText("Jadwal Salat ${selectedLocation.name}", sb.toString())
                            clipboard?.setPrimaryClip(clip)
                            Toast.makeText(context, if (isId) "Jadwal disalin ke clipboard!" else "تم نسخ الجدول!", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                        modifier = Modifier.testTag("copy_schedule_button")
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Salin", modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSecondaryContainer)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (isId) "Salin" else "نسخ", color = MaterialTheme.colorScheme.onSecondaryContainer, style = MaterialTheme.typography.labelMedium)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (isCardViewMode) {
                // ==================== MODE CARD PER-HARI (MINIMALIS & RAMAH LANSIA) ====================
                if (monthlySchedules.isNotEmpty()) {
                    val safeDayIndex = selectedDayIndex.coerceIn(0, monthlySchedules.lastIndex)
                    val activeDay = monthlySchedules[safeDayIndex]
                    val pasaran = if (isId)
                        JavaneseCalendar.getPasaran(activeDay.year, activeDay.month, activeDay.day)
                    else
                        JavaneseCalendar.getPasaranAr(activeDay.year, activeDay.month, activeDay.day)

                    // 1. STRIP PEMILIH TANGGAL (HORIZONTAL DAY CAROUSEL)
                    LazyRow(
                        state = dayListState,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(monthlySchedules.size) { index ->
                            val item = monthlySchedules[index]
                            val isSelected = index == safeDayIndex
                            val isTodayChip = item.day == currentRealDay && selectedYear == currentRealYear && selectedMonth == currentRealMonth
                            val itemHijriShort = if (isId) item.hijriFormattedId.split(" ").dropLast(2).joinToString(" ") else item.hijriFormattedAr.split(" ").dropLast(2).joinToString(" ")
                            val itemPasaran = if (isId) JavaneseCalendar.getPasaran(item.year, item.month, item.day) else JavaneseCalendar.getPasaranAr(item.year, item.month, item.day)

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) MaterialTheme.colorScheme.primary 
                                        else if (isTodayChip) MaterialTheme.colorScheme.tertiaryContainer 
                                        else MaterialTheme.colorScheme.surfaceContainerHigh,
                                border = if (isTodayChip && !isSelected) androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.tertiary) else null,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { selectedDayIndex = index }
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                ) {
                                    if (isTodayChip) {
                                        Text(
                                            text = if (isId) "HARI INI" else "اليوم",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onTertiaryContainer
                                        )
                                    }
                                    Text(
                                        text = if (isId) item.dayOfWeekNameId.take(3) else item.dayOfWeekNameAr,
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f) else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "${item.day}",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                                    )
                                    // Prominent Hijri date and Pasaran on date chip
                                    Text(
                                        text = itemHijriShort,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = itemPasaran,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontSize = 9.sp,
                                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f) else MaterialTheme.colorScheme.secondary
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // 2. HEADER TANGGAL HARI INI
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            IconButton(
                                onClick = {
                                    if (safeDayIndex > 0) selectedDayIndex = safeDayIndex - 1
                                },
                                enabled = safeDayIndex > 0,
                                modifier = Modifier.size(44.dp)
                            ) {
                                Icon(Icons.Default.ChevronLeft, contentDescription = "Prev Day", modifier = Modifier.size(32.dp))
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = if (isId) activeDay.gregorianFormattedId else activeDay.gregorianFormattedAr,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.secondaryContainer
                                ) {
                                    Text(
                                        text = "🌙 ${if (isId) activeDay.hijriFormattedId else activeDay.hijriFormattedAr} • $pasaran",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }

                            IconButton(
                                onClick = {
                                    if (safeDayIndex < monthlySchedules.lastIndex) selectedDayIndex = safeDayIndex + 1
                                },
                                enabled = safeDayIndex < monthlySchedules.lastIndex,
                                modifier = Modifier.size(44.dp)
                            ) {
                                Icon(Icons.Default.ChevronRight, contentDescription = "Next Day", modifier = Modifier.size(32.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // 3. DAFTAR KARTU JADWAL SALAT BESAR & SANGAT CLEAR
                    val itemsList = listOf(
                        Triple(if (isId) "Imsak" else "الإمساك", activeDay.imsak, "🌅"),
                        Triple(if (isId) "Subuh" else "الفجر", activeDay.subuh, "🌄"),
                        Triple(if (isId) "Terbit / Dhuha" else "الشروق / الضحى", "${activeDay.terbit} / ${activeDay.dhuha}", "☀️"),
                        Triple(if (isId) "Dzuhur" else "الظهر", activeDay.dzuhur, "🌤️"),
                        Triple(if (isId) "Ashar" else "العصر", activeDay.ashar, "🌥️"),
                        Triple(if (isId) "Maghrib" else "المغرب", activeDay.maghrib, "🌇"),
                        Triple(if (isId) "Isya" else "العشاء", activeDay.isya, "🌙")
                    )

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed(itemsList, key = { index, item -> "${item.first}_$index" }) { _, (name, time, emoji) ->
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = emoji, fontSize = 28.sp)
                                        Spacer(modifier = Modifier.width(16.dp))
                                        Text(
                                            text = name,
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }

                                    Text(
                                        text = time,
                                        style = MaterialTheme.typography.headlineMedium,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                // ==================== MODE TABEL RINGKAS (FULL TABLE VIEW) ====================
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(if (isId) "Tgl / Hijri / Jawa" else "التاريخ / الهجري / جاوا", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.6f))
                        Text(if (isId) "Subuh" else "الفجر", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                        Text(if (isId) "Dzuhur" else "الظهر", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                        Text(if (isId) "Ashar" else "العصر", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                        Text(if (isId) "Maghrib" else "المغرب", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                        Text(if (isId) "Isya" else "العشاء", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    itemsIndexed(monthlySchedules, key = { index, day -> "${day.year}_${day.month}_${day.day}_$index" }) { _, day ->
                        val isTodayRow = day.day == currentRealDay && day.month == currentRealMonth && day.year == currentRealYear
                        Card(
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isTodayRow) MaterialTheme.colorScheme.tertiaryContainer 
                                                else if (day.day % 2 == 0) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f) 
                                                else MaterialTheme.colorScheme.surface
                            ),
                            border = if (isTodayRow) androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.tertiary) else null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val pasaran = if (isId)
                                    JavaneseCalendar.getPasaran(day.year, day.month, day.day)
                                else
                                    JavaneseCalendar.getPasaranAr(day.year, day.month, day.day)

                                Column(modifier = Modifier.weight(1.6f)) {
                                    Text(
                                        text = if (isId) "${day.day} ${day.dayOfWeekNameId.take(3)}" else "${day.day} ${day.dayOfWeekNameAr}",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = "🌙 ${if (isId) day.hijriFormattedId else day.hijriFormattedAr}",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.tertiary
                                    )
                                    Text(
                                        text = pasaran,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }

                                Text(day.subuh, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
                                Text(day.dzuhur, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
                                Text(day.ashar, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
                                Text(day.maghrib, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
                                Text(day.isya, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        } else {
            // HOLIDAYS & RAMADHAN LIST FOR SELECTED YEAR
            Text(
                text = if (isId) "Hari Besar Islam Tahun $selectedYear (${selectedLocation.name})" else "المناسبات الإسلامية لعام $selectedYear",
                style = if (isSimpleUiMode) MaterialTheme.typography.titleLarge else MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(holidays, key = { index, holiday -> "${holiday.year}_${holiday.month}_${holiday.day}_${holiday.nameId}_$index" }) { _, holiday ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("holiday_card_${holiday.year}_${holiday.month}")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(if (isSimpleUiMode) 20.dp else 16.dp)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(if (isSimpleUiMode) 52.dp else 44.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MaterialTheme.colorScheme.secondaryContainer)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Holiday",
                                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                                    modifier = Modifier.size(if (isSimpleUiMode) 28.dp else 24.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column {
                                Text(
                                    text = if (isId) holiday.nameId else holiday.nameAr,
                                    style = if (isSimpleUiMode) MaterialTheme.typography.titleLarge else MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "${holiday.gregorianDateStr} • ${holiday.hijriDateStr}",
                                    style = if (isSimpleUiMode) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary,
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

