package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.WorshipRecord
import com.example.model.AppLanguage
import com.example.model.DayPrayerSchedule
import com.example.ui.theme.QuranFontFamily
import kotlinx.coroutines.delay
import java.util.Calendar
import java.util.TimeZone

@Composable
fun SunniFiqhCompanionCard(
    todaySchedule: DayPrayerSchedule,
    language: AppLanguage,
    todayRecord: WorshipRecord? = null,
    modifier: Modifier = Modifier
) {
    val isId = language == AppLanguage.INDONESIAN
    val locTz = remember(todaySchedule.timeZoneId) { TimeZone.getTimeZone(todaySchedule.timeZoneId) }

    var nowMillis by remember { mutableLongStateOf(System.currentTimeMillis()) }
    LaunchedEffect(Unit) {
        while (true) {
            nowMillis = System.currentTimeMillis()
            delay(1000L)
        }
    }

    val cal = remember(nowMillis, locTz) {
        Calendar.getInstance(locTz).apply { timeInMillis = nowMillis }
    }
    val currentMins = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE)
    val isFriday = cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY

    fun parseMins(timeStr: String): Int {
        val parts = timeStr.split(":")
        val h = parts.getOrNull(0)?.toIntOrNull() ?: 0
        val m = parts.getOrNull(1)?.toIntOrNull() ?: 0
        return h * 60 + m
    }

    val subuhMins = parseMins(todaySchedule.subuh)
    val terbitMins = parseMins(todaySchedule.terbit)
    val dhuhaMins = parseMins(todaySchedule.dhuha)
    val dzuhurMins = parseMins(todaySchedule.dzuhur)
    val asharMins = parseMins(todaySchedule.ashar)
    val maghribMins = parseMins(todaySchedule.maghrib)
    val isyaMins = parseMins(todaySchedule.isya)

    val subuhDone = todayRecord?.subuh ?: false
    val asharDone = todayRecord?.ashar ?: false

    // Check Forbidden Prayer Times (Waktu Haram Salat Mutlaq) in Sunni Syafii Fiqh:
    // 1. Sunrise (Terbit hingga 15 menit setelahnya - Syuruq)
    // 2. Istiwa' (12 menit menjelang Dzuhur, kecuali hari Jumat)
    // 3. Sunset (15 menit sebelum Maghrib)
    // 4. Pascasubuh hingga terbit (jika SUDAH melaksanakan Salat Subuh)
    // 5. Pascashar hingga 15 menit sebelum Maghrib (jika SUDAH melaksanakan Salat Ashar)
    val isForbiddenSyuruq = currentMins in terbitMins..(terbitMins + 15)
    val isForbiddenIstiwa = currentMins in (dzuhurMins - 12)..<dzuhurMins && !isFriday
    val isForbiddenSunset = currentMins in (maghribMins - 15)..<maghribMins
    val isForbiddenPostSubuh = subuhDone && currentMins in subuhMins..<terbitMins
    val isForbiddenPostAshar = asharDone && currentMins in asharMins..(maghribMins - 15)

    val isForbiddenTime = isForbiddenSyuruq || isForbiddenIstiwa || isForbiddenSunset || isForbiddenPostSubuh || isForbiddenPostAshar

    val forbiddenReason = when {
        isForbiddenSyuruq -> if (isId) "Matahari sedang terbit (Syuruq). Diharamkan salat sunnah mutlaq hingga matahari terangkat setinggi tombak (+15 menit)." else "وقت شروق الشمس. يحرم فيه ابتداء صلاة النفل المطلق حتى ترتفع الشمس قدر رمح (+١٥ دقيقة)."
        isForbiddenIstiwa -> if (isId) "Matahari tepat di puncak (Istiwa'). Diharamkan salat sunnah mutlaq menjelang Dzuhur (kecuali hari Jumat)." else "وقت استواء الشمس في كبد السماء. يحرم فيه التطوع حتى تزول الشمس (إلا يوم الجمعة)."
        isForbiddenSunset -> if (isId) "Matahari mendekati terbenam. Diharamkan salat sunnah mutlaq 15 menit sebelum Maghrib." else "اصفرار الشمس واقتراب الغروب. يحرم فيه النفل المطلق ١٥ دقيقة قبل المغرب."
        isForbiddenPostSubuh -> if (isId) "Anda sudah menunaikan Salat Subuh. Dilarang salat sunnah mutlaq tanpa sebab hingga matahari terbit." else "بعد أداء صلاة الفجر. يكره/يحرم ابتداء صلاة النفل المطلق الذي لا سبب له حتى تشرق الشمس."
        isForbiddenPostAshar -> if (isId) "Anda sudah menunaikan Salat Ashar. Dilarang salat sunnah mutlaq tanpa sebab hingga matahari terbenam." else "بعد أداء صلاة العصر. يكره/يحرم ابتداء صلاة النفل المطلق الذي لا سبب له حتى تغرب الشمس."
        else -> ""
    }

    val normalTimeReason = when {
        currentMins in subuhMins..<terbitMins && !subuhDone ->
            if (isId) "Saat ini Waktu Sah Salat Subuh Fardhu (Belum dikerjakan). Segera tunaikan Salat Subuh!" else "وقت صلاة الفجر المكتوبة حالياً (لم تُؤَدَّ بعد). بادِر بأداء صلاة الفجر!"
        currentMins in dhuhaMins..<dzuhurMins ->
            if (isId) "Saat ini Waktu Sah Salat Sunnah Dhuha (Utama saat matahari mulai hangat)." else "وقت صلاة الضحى المباركة (وأفضلها عند اشتداد الحر)."
        currentMins in dzuhurMins..<asharMins ->
            if (isId) "Saat ini Waktu Sah Salat Dzuhur Fardhu & Sunnah Rawatib." else "وقت صلاة الظهر المكتوبة والسنن الراتبة."
        currentMins in asharMins..(maghribMins - 15) && !asharDone ->
            if (isId) "Saat ini Waktu Sah Salat Ashar Fardhu (Belum dikerjakan). Segera tunaikan Salat Ashar!" else "وقت صلاة العصر المكتوبة حالياً (لم تُؤَدَّ بعد). بادِر بأداء صلاة العصر!"
        currentMins in maghribMins..<isyaMins ->
            if (isId) "Saat ini Waktu Sah Salat Maghrib Fardhu & Sunnah Rawatib." else "وقت صلاة المغرب المكتوبة والسنن الراتبة."
        currentMins >= isyaMins || currentMins < subuhMins ->
            if (isId) "Saat ini Waktu Sah Salat Isya Fardhu, Sunnah Rawatib, Tahajud, & Witir." else "وقت صلاة العشاء المكتوبة والرواتب وقيام الليل والوتر."
        else ->
            if (isId) "Saat ini aman untuk menunaikan salat fardhu maupun salat sunnah mutlaq." else "يجوز أداء صلاة النوافل المطلقة والرواتب حالياً."
    }

    var isExpandedGuide by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isForbiddenTime) MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.45f)
            else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
        ),
        modifier = modifier
            .fillMaxWidth()
            .testTag("sunni_fiqh_companion_card")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Title
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
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Fiqih Salat Sunni",
                        tint = if (isForbiddenTime) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isId) "Detektor Waktu & Fiqih Salat Sunni" else "مراقب أوقات النهي وفقه الصلاة",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (isForbiddenTime) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                        maxLines = 2,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isForbiddenTime) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                ) {
                    Text(
                        text = if (isId) "Madzhab Syafi'i" else "المذهب الشافعي",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary,
                        maxLines = 1,
                        softWrap = false,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Forbidden Detector Live Badge
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(12.dp)
                ) {
                    Icon(
                        imageVector = if (isForbiddenTime) Icons.Default.Warning else Icons.Default.CheckCircle,
                        contentDescription = "Forbidden Detector",
                        tint = if (isForbiddenTime) MaterialTheme.colorScheme.error else Color(0xFF2E7D32),
                        modifier = Modifier.size(26.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isForbiddenTime) {
                                if (isId) "⚠️ Waktu Haram / Makruh Salat Sunnah" else "⚠️ وقت نهي عن الصلاة"
                            } else {
                                if (isId) "✅ Waktu Sah Salat Fardhu & Sunnah" else "✅ وقت مباح للصلوات"
                            },
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (isForbiddenTime) MaterialTheme.colorScheme.error else Color(0xFF2E7D32)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (isForbiddenTime) forbiddenReason else normalTimeReason,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Expandable Sunni Fiqh Quick Guide
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpandedGuide = !isExpandedGuide }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f).padding(end = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.MenuBook,
                            contentDescription = "Panduan Rukun Salat",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isId) "Ringkasan 13 Rukun & Syarat Sah Salat" else "أركان وشروط صحة الصلاة",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Icon(
                        imageVector = if (isExpandedGuide) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = "Expand",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            AnimatedVisibility(visible = isExpandedGuide) {
                Column(modifier = Modifier.padding(top = 10.dp)) {
                    val rukunList = listOf(
                        "1. Niat dalam hati saat Takbiratul Ihram" to "النية",
                        "2. Berdiri bagi yang mampu" to "القيام مع القدرة",
                        "3. Takbiratul Ihram (Allahu Akbar)" to "تكبيرة الإحرام",
                        "4. Membaca Surat Al-Fatihah" to "قراءة الفاتحة",
                        "5. Ruku' dengan thuma'ninah" to "الركوع والاطمئنان",
                        "6. I'tidal dengan thuma'ninah" to "الاعتدال والاطمئنان",
                        "7. Sujud 2x dengan thuma'ninah" to "السجود والاطمئنان",
                        "8. Duduk antara 2 sujud" to "الجلوس بين السجدتين",
                        "9. Duduk Tasyahud Akhir" to "الجلوس للأخير",
                        "10. Membaca Tasyahud Akhir" to "قراءة التشهد الأخير",
                        "11. Membaca Shalawat Nabi" to "الصلاة على النبي",
                        "12. Mengucapkan Salam Pertama" to "التسليمة الأولى",
                        "13. Tertib berurutan" to "الترتيب"
                    )

                    rkListDisplay(rukunList, isId)
                }
            }
        }
    }
}

@Composable
private fun rkListDisplay(rukunList: List<Pair<String, String>>, isId: Boolean) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        rukunList.forEach { (idText, arText) ->
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = idText,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = arText,
                        fontFamily = QuranFontFamily,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
