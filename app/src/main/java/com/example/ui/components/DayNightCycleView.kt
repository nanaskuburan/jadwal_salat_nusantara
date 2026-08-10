package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.draw.clip
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
import androidx.compose.material.icons.filled.Brightness2
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.WbCloudy
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.DayPrayerSchedule
import kotlinx.coroutines.delay
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import kotlin.math.PI
import kotlin.math.sin

object MoonPhaseCalculator {
    enum class MoonPhase(val idName: String, val arName: String, val symbol: String) {
        NEW_MOON("Bulan Mati (Mati)", "المحاق", "🌑"),
        WAXING_CRESCENT("Sabit Awal (Hilal)", "الهلال المتزايد", "🌒"),
        FIRST_QUARTER("Separuh Awal", "التربيع الأول", "🌓"),
        WAXING_GIBBOUS("Cembung Awal", "الأحدب المتزايد", "🌔"),
        FULL_MOON("Bulan Purnama (Penuh)", "البدر التام", "🌕"),
        WANING_GIBBOUS("Cembung Akhir", "الأحدب المتناقص", "🌖"),
        LAST_QUARTER("Separuh Akhir", "التربيع الأخير", "🌗"),
        WANING_CRESCENT("Sabit Akhir", "الهلال المتناقص", "🌘")
    }

    data class MoonPhaseInfo(
        val phase: MoonPhase,
        val ageDays: Double,
        val illuminationPercent: Int,
        val cycleFraction: Double
    )

    fun calculateMoonPhase(calendar: java.util.Calendar = java.util.Calendar.getInstance()): MoonPhaseInfo {
        val year = calendar.get(java.util.Calendar.YEAR)
        val month = calendar.get(java.util.Calendar.MONTH) + 1
        val day = calendar.get(java.util.Calendar.DAY_OF_MONTH)
        val hour = calendar.get(java.util.Calendar.HOUR_OF_DAY)

        var y = year
        var m = month
        if (m <= 2) {
            y -= 1
            m += 12
        }
        val a = y / 100
        val b = a / 4
        val c = 2 - a + b
        val e = (365.25 * (y + 4716)).toInt()
        val f = (30.6001 * (m + 1)).toInt()
        val julianDay = e + f + day + (hour / 24.0) + c - 1524.5

        val daysSinceNew = julianDay - 2451549.5
        val synodicMonth = 29.53058867
        val newMoons = daysSinceNew / synodicMonth
        var currentCycleFraction = newMoons - kotlin.math.floor(newMoons)
        if (currentCycleFraction < 0) currentCycleFraction += 1.0
        val ageDays = currentCycleFraction * synodicMonth

        val illumination = (1.0 - kotlin.math.cos(2 * Math.PI * currentCycleFraction)) / 2.0
        val illuminationPercent = (illumination * 100).toInt().coerceIn(0, 100)

        val phase = when {
            ageDays < 1.0 || ageDays >= 28.5 -> MoonPhase.NEW_MOON
            ageDays < 6.5 -> MoonPhase.WAXING_CRESCENT
            ageDays < 8.5 -> MoonPhase.FIRST_QUARTER
            ageDays < 13.8 -> MoonPhase.WAXING_GIBBOUS
            ageDays < 15.8 -> MoonPhase.FULL_MOON
            ageDays < 21.0 -> MoonPhase.WANING_GIBBOUS
            ageDays < 23.0 -> MoonPhase.LAST_QUARTER
            else -> MoonPhase.WANING_CRESCENT
        }

        return MoonPhaseInfo(phase, ageDays, illuminationPercent, currentCycleFraction)
    }
}

private fun lerpColor(start: Color, stop: Color, fraction: Float): Color {
    val f = if (fraction.isNaN()) 0f else fraction.coerceIn(0f, 1f)
    return Color(
        red = start.red + f * (stop.red - start.red),
        green = start.green + f * (stop.green - start.green),
        blue = start.blue + f * (stop.blue - start.blue),
        alpha = start.alpha + f * (stop.alpha - start.alpha)
    )
}

private fun lerpGradient(startColors: List<Color>, stopColors: List<Color>, fraction: Float): List<Color> {
    val f = fraction.coerceIn(0f, 1f)
    val maxLen = maxOf(startColors.size, stopColors.size)
    return List(maxLen) { i ->
        val c1 = startColors.getOrElse(i) { startColors.last() }
        val c2 = stopColors.getOrElse(i) { stopColors.last() }
        lerpColor(c1, c2, f)
    }
}

@Composable
fun DayNightCycleView(
    todaySchedule: DayPrayerSchedule,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val isId = language == AppLanguage.INDONESIAN
    val targetTimeZone = remember(todaySchedule.timeZoneId) { TimeZone.getTimeZone(todaySchedule.timeZoneId) }

    // Ticker to refresh celestial calculations in real time every second
    var nowMillis by remember { mutableLongStateOf(System.currentTimeMillis()) }
    LaunchedEffect(Unit) {
        while (true) {
            nowMillis = System.currentTimeMillis()
            delay(1000L)
        }
    }

    val cal = remember(nowMillis, targetTimeZone) {
        Calendar.getInstance(targetTimeZone).apply { timeInMillis = nowMillis }
    }
    val realHour = cal.get(Calendar.HOUR_OF_DAY)
    val realMinute = cal.get(Calendar.MINUTE)
    val realSecond = cal.get(Calendar.SECOND)
    val currentMinutes24h = realHour * 60 + realMinute + realSecond / 60f

    // Format current time string (HH:mm:ss Local Timezone)
    val currentTimeStr = remember(realHour, realMinute, realSecond, todaySchedule.timeZoneName) {
        String.format(Locale.US, "%02d:%02d:%02d %s", realHour, realMinute, realSecond, todaySchedule.timeZoneName)
    }

    // Helper to convert "HH:mm" to minutes from midnight
    fun parseMins(timeStr: String): Float {
        val parts = timeStr.split(":")
        val h = parts.getOrNull(0)?.toIntOrNull() ?: 0
        val m = parts.getOrNull(1)?.toIntOrNull() ?: 0
        return (h * 60 + m).toFloat()
    }

    val subuhMins = parseMins(todaySchedule.subuh)
    val terbitMins = parseMins(todaySchedule.terbit)
    val dzuhurMins = parseMins(todaySchedule.dzuhur)
    val asharMins = parseMins(todaySchedule.ashar)
    val maghribMins = parseMins(todaySchedule.maghrib)
    val isyaMins = parseMins(todaySchedule.isya)

    // Determine Daytime vs Nighttime accurately based on Sun above horizon (Terbit to Maghrib)
    val isDaytime = currentMinutes24h >= terbitMins && currentMinutes24h < maghribMins

    // Full 24h Progress Ratio (0.0 at 00:00 to 1.0 at 24:00)
    val progress24h = (currentMinutes24h / 1440f).coerceIn(0f, 1f)

    // Dash effects remembered outside canvas draw loop
    val horizonDash = remember { PathEffect.dashPathEffect(floatArrayOf(8f, 8f), 0f) }
    val arcDash = remember { PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f) }

    // Continuous Real-Time Sky Gradient Lerp according to exact astronomical time
    val skyGradientColors = remember(currentMinutes24h, subuhMins, terbitMins, dzuhurMins, asharMins, maghribMins, isyaMins) {
        val midnightG = listOf(Color(0xFF030712), Color(0xFF0A1128), Color(0xFF001F54))
        val fajarStartG = listOf(Color(0xFF0B091A), Color(0xFF161B33), Color(0xFF2D1E2F))
        val subuhG = listOf(Color(0xFF10002B), Color(0xFF3C096C), Color(0xFF7B2CBF), Color(0xFFFF9E00))
        val terbitG = listOf(Color(0xFFB71C1C), Color(0xFFE65100), Color(0xFFFB8C00), Color(0xFFFFD54F))
        val dhuhaG = listOf(Color(0xFF0277BD), Color(0xFF0288D1), Color(0xFF4FC3F7))
        val dzuhurG = listOf(Color(0xFF0288D1), Color(0xFF03A9F4), Color(0xFF81D4FA))
        val asharG = listOf(Color(0xFF1A237E), Color(0xFF0288D1), Color(0xFFFFB74D))
        val maghribG = listOf(Color(0xFF311B92), Color(0xFFB71C1C), Color(0xFFE65100), Color(0xFFFFB74D))
        val isyaG = listOf(Color(0xFF10002B), Color(0xFF240046), Color(0xFF3C096C))

        val fajarStartMins = subuhMins - 40f
        val dhuhaMins = terbitMins + 45f

        when {
            currentMinutes24h < fajarStartMins -> {
                val fraction = currentMinutes24h / fajarStartMins
                lerpGradient(midnightG, fajarStartG, fraction)
            }
            currentMinutes24h < subuhMins -> {
                val fraction = (currentMinutes24h - fajarStartMins) / (subuhMins - fajarStartMins)
                lerpGradient(fajarStartG, subuhG, fraction)
            }
            currentMinutes24h < terbitMins -> {
                val fraction = (currentMinutes24h - subuhMins) / (terbitMins - subuhMins)
                lerpGradient(subuhG, terbitG, fraction)
            }
            currentMinutes24h < dhuhaMins -> {
                val fraction = (currentMinutes24h - terbitMins) / (dhuhaMins - terbitMins)
                lerpGradient(terbitG, dhuhaG, fraction)
            }
            currentMinutes24h < dzuhurMins -> {
                val fraction = (currentMinutes24h - dhuhaMins) / (dzuhurMins - dhuhaMins)
                lerpGradient(dhuhaG, dzuhurG, fraction)
            }
            currentMinutes24h < asharMins -> {
                val fraction = (currentMinutes24h - dzuhurMins) / (asharMins - dzuhurMins)
                lerpGradient(dzuhurG, asharG, fraction)
            }
            currentMinutes24h < maghribMins -> {
                val fraction = (currentMinutes24h - asharMins) / (maghribMins - asharMins)
                lerpGradient(asharG, maghribG, fraction)
            }
            currentMinutes24h < isyaMins -> {
                val fraction = (currentMinutes24h - maghribMins) / (isyaMins - maghribMins)
                lerpGradient(maghribG, isyaG, fraction)
            }
            else -> {
                val fraction = (currentMinutes24h - isyaMins) / (1440f - isyaMins)
                lerpGradient(isyaG, midnightG, fraction)
            }
        }
    }

    val accentColor = if (isDaytime) Color(0xFFFFD54F) else Color(0xFF81D4FA)

    val cycleStatusText = when {
        currentMinutes24h < 180f -> if (isId) "1/3 MALAM AKHIR" else "الثلث الأخير من الليل"
        currentMinutes24h < (subuhMins - 40f) -> if (isId) "SEPERTIGA MALAM" else "الهزيع الأخير من الليل"
        currentMinutes24h < subuhMins -> if (isId) "JELANG SUBUH" else "قبيل الفجر الصادق"
        currentMinutes24h < terbitMins -> if (isId) "FAJAR MEREKAH" else "الفجر الصادق"
        currentMinutes24h < (terbitMins + 30f) -> if (isId) "TERBIT SURYA" else "إشراق الشمس"
        currentMinutes24h < (terbitMins + 100f) -> if (isId) "PAGI CERAH" else "الصباح المشرق"
        currentMinutes24h < (dzuhurMins - 30f) -> if (isId) "DHUHA TINGGI" else "وقت الضحى الأكبر"
        currentMinutes24h < (dzuhurMins - 5f) -> if (isId) "JELANG REMBANG" else "قبيل الاستواء"
        currentMinutes24h < (dzuhurMins + 45f) -> if (isId) "TENGAH HARI" else "زوال الشمس والظهيرة"
        currentMinutes24h < (asharMins - 30f) -> if (isId) "SIANG CERAH" else "بعد الظهيرة"
        currentMinutes24h < (asharMins + 60f) -> if (isId) "SORE HARI" else "وقت العصر والأصيل"
        currentMinutes24h < (maghribMins - 20f) -> if (isId) "JELANG SENJA" else "قبيل غروب الشمس"
        currentMinutes24h < maghribMins -> if (isId) "TERBENAM SURYA" else "غروب الشمس"
        currentMinutes24h < (maghribMins + 45f) -> if (isId) "SENJA MERAH" else "الشفق الأحمر"
        currentMinutes24h < isyaMins -> if (isId) "REMBANG SENJA" else "الشفق الأبيض"
        currentMinutes24h < (isyaMins + 90f) -> if (isId) "AWAL MALAM" else "بداية الليل والعشاء"
        currentMinutes24h < 1320f -> if (isId) "MALAM SYAHDU" else "سواد الليل الهادئ"
        else -> if (isId) "1/3 MALAM AWAL" else "الثلث الأول من الليل"
    }

    val cycleStatusIcon = when {
        currentMinutes24h < 180f -> Icons.Default.NightsStay
        currentMinutes24h < (subuhMins - 40f) -> Icons.Default.NightsStay
        currentMinutes24h < subuhMins -> Icons.Default.WbTwilight
        currentMinutes24h < terbitMins -> Icons.Default.WbTwilight
        currentMinutes24h < (terbitMins + 30f) -> Icons.Default.WbSunny
        currentMinutes24h < (terbitMins + 100f) -> Icons.Default.WbSunny
        currentMinutes24h < (dzuhurMins - 30f) -> Icons.Default.WbSunny
        currentMinutes24h < (dzuhurMins - 5f) -> Icons.Default.WbSunny
        currentMinutes24h < (dzuhurMins + 45f) -> Icons.Default.WbSunny
        currentMinutes24h < (asharMins - 30f) -> Icons.Default.WbSunny
        currentMinutes24h < (asharMins + 60f) -> Icons.Default.WbCloudy
        currentMinutes24h < (maghribMins - 20f) -> Icons.Default.WbCloudy
        currentMinutes24h < maghribMins -> Icons.Default.WbTwilight
        currentMinutes24h < (maghribMins + 45f) -> Icons.Default.WbTwilight
        currentMinutes24h < isyaMins -> Icons.Default.WbTwilight
        currentMinutes24h < (isyaMins + 90f) -> Icons.Default.Brightness2
        currentMinutes24h < 1320f -> Icons.Default.Brightness2
        else -> Icons.Default.NightsStay
    }

    val cycleStatusColor = when {
        currentMinutes24h < 180f -> Color(0xFF7C4DFF)
        currentMinutes24h < (subuhMins - 40f) -> Color(0xFF90CAF9)
        currentMinutes24h < subuhMins -> Color(0xFFB388FF)
        currentMinutes24h < terbitMins -> Color(0xFFFFB74D)
        currentMinutes24h < (terbitMins + 30f) -> Color(0xFFFF9E00)
        currentMinutes24h < (terbitMins + 100f) -> Color(0xFF4FC3F7)
        currentMinutes24h < (dzuhurMins - 30f) -> Color(0xFF29B6F6)
        currentMinutes24h < (dzuhurMins - 5f) -> Color(0xFFFFD54F)
        currentMinutes24h < (dzuhurMins + 45f) -> Color(0xFFFFE082)
        currentMinutes24h < (asharMins - 30f) -> Color(0xFFFFCA28)
        currentMinutes24h < (asharMins + 60f) -> Color(0xFFFFB74D)
        currentMinutes24h < (maghribMins - 20f) -> Color(0xFFFF7043)
        currentMinutes24h < maghribMins -> Color(0xFFFFAB91)
        currentMinutes24h < (maghribMins + 45f) -> Color(0xFFEC407A)
        currentMinutes24h < isyaMins -> Color(0xFFAB47BC)
        currentMinutes24h < (isyaMins + 90f) -> Color(0xFF7E57C2)
        currentMinutes24h < 1320f -> Color(0xFF5C6BC0)
        else -> Color(0xFF3F51B5)
    }

    val currentPhaseTitle = when {
        currentMinutes24h in 0f..subuhMins -> if (isId) "Malam Hening & Suasana Tahajud" else "سكون الليل ووقت التهجد"
        currentMinutes24h in subuhMins..terbitMins -> if (isId) "Fajar Merekah Menjelang Pagi" else "تباشير الفجر والصباح"
        currentMinutes24h in terbitMins..(terbitMins + 45f) -> if (isId) "Matahari Terbit di Ufuk Timur" else "إشراق الشمس من الشرق"
        currentMinutes24h in (terbitMins + 45f)..(dzuhurMins - 20f) -> if (isId) "Pagi Cerah & Suasana Dhuha" else "بهجة الصباح ووقت الضحى"
        currentMinutes24h in (dzuhurMins - 20f)..dzuhurMins -> if (isId) "Matahari Puncak Lengkung Langit" else "الشمس في كبد السماء"
        currentMinutes24h in dzuhurMins..asharMins -> if (isId) "Siang Hari Terang Benderang" else "ضياء النهار ونور الظهيرة"
        currentMinutes24h in asharMins..maghribMins -> if (isId) "Sore Hari & Bayangan Memanjang" else "وقت الأصيل وجمال الظلال"
        currentMinutes24h in maghribMins..isyaMins -> if (isId) "Indahnya Senja & Terbenam Mentari" else "روعة الغروب وشفق المساء"
        else -> if (isId) "Suasana Malam & Pesona Bulan" else "هدوء الليل ونور القمر"
    }

    val moonInfo = remember(nowMillis) {
        val c = Calendar.getInstance()
        c.timeInMillis = nowMillis
        MoonPhaseCalculator.calculateMoonPhase(c)
    }

    val transparentSkyGradientColors = remember(skyGradientColors) {
        skyGradientColors.map { color -> color.copy(alpha = color.alpha * 0.65f) }
    }

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color.Black.copy(alpha = 0.20f),
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.25f),
                shape = RoundedCornerShape(20.dp)
            )
            .testTag("day_night_cycle_simulation_24h")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.verticalGradient(transparentSkyGradientColors))
                .padding(14.dp)
        ) {
            // Header: Realtime Status & Clock
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = if (isDaytime) Icons.Default.WbSunny else Icons.Default.Brightness2,
                        contentDescription = "24h Celestial",
                        tint = accentColor,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isId) "Siklus Realtime 24 Jam" else "دورة الفلك ٢٤ ساعة",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            maxLines = 1,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                        )
                        val isolatedTime = "\u202A$currentTimeStr\u202C"
                        val isolatedPercent = "\u202A${moonInfo.illuminationPercent}%\u202C"
                        val phaseName = if (isId) moonInfo.phase.idName else moonInfo.phase.arName
                        Text(
                            text = "$currentPhaseTitle • $isolatedTime • ${moonInfo.phase.symbol} $phaseName ($isolatedPercent)",
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 11.sp,
                            color = accentColor,
                            maxLines = 1,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = cycleStatusColor.copy(alpha = 0.30f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Icon(
                            imageVector = cycleStatusIcon,
                            contentDescription = null,
                            tint = cycleStatusColor,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = cycleStatusText,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = cycleStatusColor,
                            maxLines = 1
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // CANVAS: 24-Hour Continuous Realtime Orbit Simulation
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxWidth().height(80.dp)) {
                    val width = size.width
                    val height = size.height

                    val horizonY = height * 0.70f
                    val arcMaxHeight = height * 0.50f

                    // 1. Draw Horizon Line
                    drawLine(
                        color = Color.White.copy(alpha = 0.3f),
                        start = Offset(0f, horizonY),
                        end = Offset(width, horizonY),
                        strokeWidth = 2f,
                        pathEffect = horizonDash
                    )

                    // 2. Draw 24-Hour Continuous Solar & Lunar Path Trajectory
                    val path24h = Path()
                    val steps = 100
                    val terbitRatio = terbitMins / 1440f
                    val dzuhurRatio = dzuhurMins / 1440f
                    val maghribRatio = maghribMins / 1440f

                    val nightSpan = (1f - maghribRatio + terbitRatio).let { if (it > 0f) it else 0.5f }

                    // High Precision Astronomical Altitude Y Calculation
                    fun calcY(tRatio: Float): Float {
                        return if (tRatio in terbitRatio..maghribRatio) {
                            val dayT = if (tRatio <= dzuhurRatio) {
                                val span = (dzuhurRatio - terbitRatio).coerceAtLeast(0.01f)
                                ((tRatio - terbitRatio) / span) * 0.5f
                            } else {
                                val span = (maghribRatio - dzuhurRatio).coerceAtLeast(0.01f)
                                0.5f + ((tRatio - dzuhurRatio) / span) * 0.5f
                            }
                            horizonY - (sin(dayT * PI) * arcMaxHeight).toFloat()
                        } else {
                            val nightT = (if (tRatio < terbitRatio) {
                                (tRatio + (1f - maghribRatio)) / nightSpan
                            } else {
                                (tRatio - maghribRatio) / nightSpan
                            }).coerceIn(0f, 1f)
                            horizonY + (sin(nightT * PI) * (arcMaxHeight * 0.5f)).toFloat()
                        }
                    }

                    for (i in 0..steps) {
                        val t = i / steps.toFloat() // 0.0 (00:00) to 1.0 (24:00)
                        val x = t * width
                        val y = calcY(t)

                        if (i == 0) path24h.moveTo(x, y) else path24h.lineTo(x, y)
                    }

                    drawPath(
                        path = path24h,
                        color = Color.White.copy(alpha = 0.4f),
                        style = Stroke(
                            width = 2.5f,
                            pathEffect = arcDash
                        )
                    )

                    // 3. Prayer Checkpoints Along 24h Timeline
                    val prayerPoints = listOf(
                        subuhMins / 1440f to "Subuh",
                        terbitMins / 1440f to "Syuruq",
                        dzuhurMins / 1440f to "Dzuhur",
                        asharMins / 1440f to "Ashar",
                        maghribMins / 1440f to "Maghrib",
                        isyaMins / 1440f to "Isya"
                    )

                    prayerPoints.forEach { (ratio, _) ->
                        val clampedRatio = ratio.coerceIn(0f, 1f)
                        val px = clampedRatio * width
                        val py = calcY(clampedRatio)

                        drawCircle(
                            color = Color.White.copy(alpha = 0.85f),
                            radius = 4.5f,
                            center = Offset(px, py)
                        )
                    }

                    // 0. Twinkling Stars in Night Sky
                    if (!isDaytime) {
                        val stars = listOf(
                            Offset(width * 0.08f, horizonY * 0.25f),
                            Offset(width * 0.20f, horizonY * 0.45f),
                            Offset(width * 0.32f, horizonY * 0.18f),
                            Offset(width * 0.50f, horizonY * 0.30f),
                            Offset(width * 0.65f, horizonY * 0.15f),
                            Offset(width * 0.82f, horizonY * 0.40f),
                            Offset(width * 0.92f, horizonY * 0.22f)
                        )
                        stars.forEach { star ->
                            drawCircle(
                                color = Color.White.copy(alpha = 0.85f),
                                radius = 2.2f,
                                center = star
                            )
                        }
                    }

                    // 4. Current Realtime Sun / Moon Position
                    val activeRatio = progress24h.coerceIn(0f, 1f)
                    val cx = activeRatio * width
                    val cy = calcY(activeRatio)

                    if (isDaytime) {
                        // SUN RENDERING: Dynamic Elevation-Based Light Spread & Breathing Glow (No Rotation)
                        val sunHeightFactor = ((horizonY - cy) / arcMaxHeight).coerceIn(0f, 1f)
                        
                        // Gentle breathing pulse animation for sun light ignition ("sulut sinar")
                        val sulutPulse = (kotlin.math.sin(nowMillis / 400.0) * 0.08f + 0.92f).toFloat()

                        // Dynamic ray length: higher sun = rays spread wider out
                        val innerR = 12f * (0.8f + 0.2f * sunHeightFactor)
                        val outerR = (16f + 18f * sunHeightFactor) * sulutPulse

                        // Ray color & alpha: dims near horizon (meredup), intense & bright at peak
                        val rayColor = lerpColor(Color(0xFFFF9E00), Color(0xFFFFE082), sunHeightFactor)
                        val rayAlpha = (0.35f + 0.65f * sunHeightFactor) * sulutPulse

                        // Fixed angles (no rotation as requested)
                        for (angleDeg in 0 until 360 step 45) {
                            val rad = Math.toRadians(angleDeg.toDouble())
                            val startX = cx + (innerR * kotlin.math.cos(rad)).toFloat()
                            val startY = cy + (innerR * kotlin.math.sin(rad)).toFloat()
                            val endX = cx + (outerR * kotlin.math.cos(rad)).toFloat()
                            val endY = cy + (outerR * kotlin.math.sin(rad)).toFloat()
                            drawLine(
                                color = rayColor.copy(alpha = rayAlpha),
                                start = Offset(startX, startY),
                                end = Offset(endX, endY),
                                strokeWidth = 2.5f + 1.5f * sunHeightFactor
                            )
                        }

                        // Sun Halo: Spreads wider and brightens as sun rises higher
                        val haloRadius = (16f + 20f * sunHeightFactor) * sulutPulse
                        val haloAlpha = (0.20f + 0.45f * sunHeightFactor) * sulutPulse
                        val haloColor = lerpColor(Color(0xFFFF7043), Color(0xFFFFD54F), sunHeightFactor)

                        drawCircle(
                            color = haloColor.copy(alpha = haloAlpha),
                            radius = haloRadius,
                            center = Offset(cx, cy)
                        )

                        // Sun Core: Dimmer orange near horizon, intense bright white-yellow at peak
                        val coreRadius = 9f + 3.5f * sunHeightFactor
                        val coreColor = lerpColor(Color(0xFFFFB74D), Color(0xFFFFFDE7), sunHeightFactor)

                        drawCircle(
                            color = coreColor,
                            radius = coreRadius,
                            center = Offset(cx, cy)
                        )
                    } else {
                        // ASTRONOMICAL REALTIME MOON RENDERING
                        val moonRadius = 12f
                        val cycleFraction = moonInfo.cycleFraction
                        val illuminationPercent = moonInfo.illuminationPercent

                        // 1. Halo Silver Glow
                        val glowAlpha = (illuminationPercent / 100f * 0.45f).coerceAtLeast(0.10f)
                        drawCircle(
                            color = Color(0xFFFFF9C4).copy(alpha = glowAlpha),
                            radius = moonRadius * 1.8f,
                            center = Offset(cx, cy)
                        )

                        // 2. Base Dark Lunar Disc (Shadow side with Craters)
                        drawCircle(
                            color = Color(0xFF1E293B),
                            radius = moonRadius,
                            center = Offset(cx, cy)
                        )

                        // Dark Craters on base disc
                        drawCircle(
                            color = Color(0xFF0F172A).copy(alpha = 0.5f),
                            radius = moonRadius * 0.22f,
                            center = Offset(cx - moonRadius * 0.25f, cy - moonRadius * 0.2f)
                        )
                        drawCircle(
                            color = Color(0xFF0F172A).copy(alpha = 0.5f),
                            radius = moonRadius * 0.18f,
                            center = Offset(cx + moonRadius * 0.2f, cy + moonRadius * 0.3f)
                        )

                        // 3. Illuminated Part Rendering
                        val brightMoonColor = Color(0xFFFFF9C4)

                        if (illuminationPercent >= 96) {
                            // FULL MOON: 100% Lit Disc
                            drawCircle(
                                color = brightMoonColor,
                                radius = moonRadius,
                                center = Offset(cx, cy)
                            )
                            // Full Moon Craters
                            drawCircle(
                                color = Color(0xFFE0E0E0).copy(alpha = 0.35f),
                                radius = moonRadius * 0.25f,
                                center = Offset(cx - moonRadius * 0.2f, cy - moonRadius * 0.15f)
                            )
                            drawCircle(
                                color = Color(0xFFE0E0E0).copy(alpha = 0.25f),
                                radius = moonRadius * 0.20f,
                                center = Offset(cx + moonRadius * 0.25f, cy + moonRadius * 0.2f)
                            )
                        } else if (illuminationPercent <= 3) {
                            // NEW MOON: Faint Silver Ring
                            drawCircle(
                                color = Color(0xFF90A4AE).copy(alpha = 0.40f),
                                radius = moonRadius,
                                center = Offset(cx, cy),
                                style = Stroke(width = 1.2f)
                            )
                        } else {
                            // DYNAMIC MOON PHASE GEOMETRY (Lightweight vector path)
                            val isWaxing = cycleFraction <= 0.5
                            val xPhase = -kotlin.math.cos(2 * Math.PI * cycleFraction).toFloat()
                            val ovalWidth = (moonRadius * kotlin.math.abs(xPhase)).coerceAtLeast(0.1f)

                            val litPath = Path().apply {
                                val fullRect = androidx.compose.ui.geometry.Rect(cx - moonRadius, cy - moonRadius, cx + moonRadius, cy + moonRadius)
                                val ovalRect = androidx.compose.ui.geometry.Rect(cx - ovalWidth, cy - moonRadius, cx + ovalWidth, cy + moonRadius)

                                if (isWaxing) {
                                    moveTo(cx, cy - moonRadius)
                                    arcTo(fullRect, -90f, 180f, false)
                                    if (xPhase < 0) {
                                        arcTo(ovalRect, 90f, -180f, false)
                                    } else {
                                        arcTo(ovalRect, 90f, 180f, false)
                                    }
                                } else {
                                    moveTo(cx, cy + moonRadius)
                                    arcTo(fullRect, 90f, 180f, false)
                                    if (xPhase < 0) {
                                        arcTo(ovalRect, -90f, -180f, false)
                                    } else {
                                        arcTo(ovalRect, -90f, 180f, false)
                                    }
                                }
                                close()
                            }

                            drawPath(litPath, color = brightMoonColor)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Footer Timeline Markers (evenly distributed across screen width)
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "00:00",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 8.sp,
                    color = Color.White.copy(alpha = 0.7f),
                    maxLines = 1
                )
                Text(
                    text = "Subuh ${todaySchedule.subuh}",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 8.sp,
                    color = Color.White.copy(alpha = 0.7f),
                    maxLines = 1,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Text(
                    text = "Dzuhur ${todaySchedule.dzuhur}",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 8.sp,
                    color = Color.White.copy(alpha = 0.7f),
                    maxLines = 1,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Text(
                    text = "Maghrib ${todaySchedule.maghrib}",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 8.sp,
                    color = Color.White.copy(alpha = 0.7f),
                    maxLines = 1,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Text(
                    text = "24:00",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 8.sp,
                    color = Color.White.copy(alpha = 0.7f),
                    maxLines = 1
                )
            }
        }
    }
}
