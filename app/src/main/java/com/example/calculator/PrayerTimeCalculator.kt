package com.example.calculator

import com.example.model.DayPrayerSchedule
import com.example.model.NgawiKecamatan
import com.example.model.PrayerTimeItem
import com.example.model.PrayerType
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.Locale
import java.util.TimeZone
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.atan
import kotlin.math.atan2
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin
import kotlin.math.tan

object PrayerTimeCalculator {

    // Timezone WIB = UTC+7
    private const val TIMEZONE = 7.0

    fun calculateForLocation(
        year: Int,
        month: Int,
        day: Int,
        location: com.example.model.IndonesiaLocation,
        ihtiyatiMinutes: Int = 2,
        hijriOffset: Int = 0
    ): DayPrayerSchedule {
        val lat = location.lat
        val lon = location.lon
        val tzOffset = location.timeZoneOffsetHours
        val tzId = location.timeZoneId

        // Julian Day at 12:00 local time
        val julianDay = getJulianDay(year, month, day) + (12.0 - tzOffset) / 24.0
        val d = julianDay - 2451545.0

        // Sun Mean Anomaly & Longitude
        val g = fixAngle(357.529 + 0.98560028 * d)
        val q = fixAngle(280.459 + 0.98564736 * d)

        val gRad = Math.toRadians(g)
        val L = fixAngle(q + 1.915 * sin(gRad) + 0.020 * sin(2.0 * gRad))
        val lRad = Math.toRadians(L)

        val obq = 23.439 - 0.00000036 * d
        val obqRad = Math.toRadians(obq)

        val declination = Math.toDegrees(asin(sin(obqRad) * sin(lRad)))

        var ra = Math.toDegrees(atan2(cos(obqRad) * sin(lRad), cos(lRad))) / 15.0
        ra = fixHour(ra)

        var eot = (q / 15.0) - ra
        if (eot > 20.0) eot -= 24.0
        if (eot < -20.0) eot += 24.0

        // Standard meridian for location's timezone (e.g. 105° for WIB, 120° for WITA, 135° for WIT)
        val standardMeridian = tzOffset * 15.0
        val solarNoon = 12.0 + (standardMeridian - lon) / 15.0 - eot

        fun hourAngle(angle: Double): Double {
            val latRad = Math.toRadians(lat)
            val declRad = Math.toRadians(declination)
            val angRad = Math.toRadians(angle)
            val cosH = (sin(angRad) - sin(latRad) * sin(declRad)) / (cos(latRad) * cos(declRad))
            return if (cosH < -1.0) 12.0 else if (cosH > 1.0) 0.0 else Math.toDegrees(acos(cosH)) / 15.0
        }

        val subuhHA = hourAngle(-20.0)
        val subuhRaw = solarNoon - subuhHA

        val terbitHA = hourAngle(-0.8333)
        val terbitRaw = solarNoon - terbitHA

        val dhuhaHA = hourAngle(4.5)
        val dhuhaRaw = solarNoon - dhuhaHA

        val asharAngle = Math.toDegrees(atan(1.0 / (1.0 + tan(Math.toRadians(abs(lat - declination))))))
        val asharHA = hourAngle(asharAngle)
        val asharRaw = solarNoon + asharHA

        val maghribHA = hourAngle(-0.8333)
        val maghribRaw = solarNoon + maghribHA

        val isyaHA = hourAngle(-18.0)
        val isyaRaw = solarNoon + isyaHA

        val safetyHours = ihtiyatiMinutes / 60.0
        val extraHours = location.minuteOffset / 60.0

        val dzuhurTime = formatHours(solarNoon + safetyHours + extraHours)
        val subuhTime = formatHours(subuhRaw + safetyHours + extraHours)
        val imsakTime = formatHours(subuhRaw + safetyHours + extraHours - (10.0 / 60.0))
        val terbitTime = formatHours(terbitRaw)
        val dhuhaTime = formatHours(dhuhaRaw + safetyHours + extraHours)
        val asharTime = formatHours(asharRaw + safetyHours + extraHours)
        val maghribTime = formatHours(maghribRaw + safetyHours + extraHours)
        val isyaTime = formatHours(isyaRaw + safetyHours + extraHours)

        val locTimeZone = TimeZone.getTimeZone(tzId)
        val calendar = GregorianCalendar(locTimeZone).apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month - 1)
            set(Calendar.DAY_OF_MONTH, day)
        }
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val (dayNameId, dayNameAr) = getDayNames(dayOfWeek)

        val monthNameId = getIndonesianMonthName(month)
        val monthNameAr = getArabicMonthName(month)
        val gregorianFormattedId = "$dayNameId, $day $monthNameId $year"
        val gregorianFormattedAr = "$dayNameAr، $day $monthNameAr $year"

        val hijriData = HijriCalendar.getHijriDate(year, month, day, hijriOffset)
        val hijriFormattedId = "${hijriData.day} ${hijriData.monthNameId} ${hijriData.year} H"
        val hijriFormattedAr = "${hijriData.day} ${hijriData.monthNameAr} ${hijriData.year} هـ"

        fun parseToMillis(timeStr: String): Long {
            val parts = timeStr.split(":")
            val h = parts[0].toIntOrNull() ?: 0
            val m = parts[1].toIntOrNull() ?: 0
            val c = Calendar.getInstance(locTimeZone)
            c.set(Calendar.YEAR, year)
            c.set(Calendar.MONTH, month - 1)
            c.set(Calendar.DAY_OF_MONTH, day)
            c.set(Calendar.HOUR_OF_DAY, h)
            c.set(Calendar.MINUTE, m)
            c.set(Calendar.SECOND, 0)
            c.set(Calendar.MILLISECOND, 0)
            return c.timeInMillis
        }

        val prayerItems = listOf(
            PrayerTimeItem(PrayerType.IMSAK, imsakTime, parseToMillis(imsakTime)),
            PrayerTimeItem(PrayerType.SUBUH, subuhTime, parseToMillis(subuhTime)),
            PrayerTimeItem(PrayerType.TERBIT, terbitTime, parseToMillis(terbitTime)),
            PrayerTimeItem(PrayerType.DHUHA, dhuhaTime, parseToMillis(dhuhaTime)),
            PrayerTimeItem(PrayerType.DZUHUR, dzuhurTime, parseToMillis(dzuhurTime)),
            PrayerTimeItem(PrayerType.ASHAR, asharTime, parseToMillis(asharTime)),
            PrayerTimeItem(PrayerType.MAGHRIB, maghribTime, parseToMillis(maghribTime)),
            PrayerTimeItem(PrayerType.ISYA, isyaTime, parseToMillis(isyaTime))
        )

        return DayPrayerSchedule(
            year = year,
            month = month,
            day = day,
            dayOfWeekNameId = dayNameId,
            dayOfWeekNameAr = dayNameAr,
            gregorianFormattedId = gregorianFormattedId,
            gregorianFormattedAr = gregorianFormattedAr,
            hijriFormattedId = hijriFormattedId,
            hijriFormattedAr = hijriFormattedAr,
            imsak = imsakTime,
            subuh = subuhTime,
            terbit = terbitTime,
            dhuha = dhuhaTime,
            dzuhur = dzuhurTime,
            ashar = asharTime,
            maghrib = maghribTime,
            isya = isyaTime,
            prayerList = prayerItems,
            locationName = location.name,
            province = location.province,
            timeZoneName = location.timeZoneName,
            timeZoneId = location.timeZoneId,
            isNgawiRegion = location.isNgawiRegion,
            isDownloadedOffline = true
        )
    }

    /**
     * High-precision astronomical calculation based on PrayTimes / Jean Meeus algorithms.
     * Strictly conforms to Kemenag RI standards:
     * - Subuh: Sun elevation -20.0°
     * - Isya: Sun elevation -18.0°
     * - Ashar: Shafi'i shadow ratio = 1
     * - Ihtiyati: Safety factor (+2 mins) added and rounded up (ceil) to prevent praying early.
     */
    fun calculate(
        year: Int,
        month: Int,
        day: Int,
        kecamatan: NgawiKecamatan = NgawiKecamatan.ALL[0],
        ihtiyatiMinutes: Int = 2,
        hijriOffset: Int = 0
    ): DayPrayerSchedule {
        val loc = com.example.model.IndonesiaLocation(
            id = kecamatan.name.lowercase().replace(" ", "_"),
            name = kecamatan.name,
            province = "Jawa Timur",
            lat = kecamatan.lat,
            lon = kecamatan.lon,
            timeZoneId = "Asia/Jakarta",
            timeZoneName = "WIB",
            timeZoneOffsetHours = 7.0,
            isNgawiRegion = true,
            minuteOffset = kecamatan.minuteOffset
        )
        return calculateForLocation(year, month, day, loc, ihtiyatiMinutes, hijriOffset)
    }

    private fun fixAngle(angle: Double): Double {
        var a = angle % 360.0
        if (a < 0) a += 360.0
        return a
    }

    private fun fixHour(hour: Double): Double {
        var h = hour % 24.0
        if (h < 0) h += 24.0
        return h
    }

    private fun getJulianDay(year: Int, month: Int, day: Int): Double {
        var y = year
        var m = month
        if (m <= 2) {
            y -= 1
            m += 12
        }
        val a = floor(y / 100.0)
        val b = 2 - a + floor(a / 4.0)
        return floor(365.25 * (y + 4716)) + floor(30.6001 * (m + 1)) + day + b - 1524.5
    }

    /**
     * Converts decimal hours to HH:mm string.
     * Uses ceil rounding to guarantee prayer times are rounded UP to the next minute
     * in accordance with Fiqh and Kemenag RI standards.
     */
    private fun formatHours(hours: Double): String {
        var h = hours % 24.0
        if (h < 0) h += 24.0

        val totalSeconds = ceil(h * 3600.0).toLong()
        var totalMinutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        if (seconds > 0) {
            totalMinutes += 1
        }

        val hr = ((totalMinutes / 60) % 24).toInt()
        val min = (totalMinutes % 60).toInt()
        return String.format(Locale.US, "%02d:%02d", hr, min)
    }

    private fun getDayNames(dayOfWeek: Int): Pair<String, String> {
        return when (dayOfWeek) {
            Calendar.SUNDAY -> "Ahad" to "الأحد"
            Calendar.MONDAY -> "Senin" to "الإثنين"
            Calendar.TUESDAY -> "Selasa" to "الثلاثاء"
            Calendar.WEDNESDAY -> "Rabu" to "الأربعاء"
            Calendar.THURSDAY -> "Kamis" to "الخميس"
            Calendar.FRIDAY -> "Jumat" to "الجمعة"
            Calendar.SATURDAY -> "Sabtu" to "السبت"
            else -> "" to ""
        }
    }

    fun getIndonesianMonthName(month: Int): String {
        return when (month) {
            1 -> "Januari"
            2 -> "Februari"
            3 -> "Maret"
            4 -> "April"
            5 -> "Mei"
            6 -> "Juni"
            7 -> "Juli"
            8 -> "Agustus"
            9 -> "September"
            10 -> "Oktober"
            11 -> "November"
            12 -> "Desember"
            else -> ""
        }
    }

    fun getArabicMonthName(month: Int): String {
        return when (month) {
            1 -> "يناير"
            2 -> "فبراير"
            3 -> "مارس"
            4 -> "أبريل"
            5 -> "مايو"
            6 -> "يونيو"
            7 -> "يوليو"
            8 -> "أغسطس"
            9 -> "سبتمبر"
            10 -> "أكتوبر"
            11 -> "نوفمبر"
            12 -> "ديسمبر"
            else -> ""
        }
    }
}

