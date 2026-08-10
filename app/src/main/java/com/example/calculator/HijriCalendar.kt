package com.example.calculator

import com.example.model.IslamicHoliday
import java.util.Calendar
import java.util.GregorianCalendar
import kotlin.math.floor

data class HijriDate(
    val day: Int,
    val month: Int,
    val year: Int,
    val monthNameId: String,
    val monthNameAr: String
)

object HijriCalendar {

    private val HIJRI_MONTHS_ID = listOf(
        "Muharram", "Safar", "Rabi'ul Awal", "Rabi'ul Akhir",
        "Jumadil Awal", "Jumadil Akhir", "Rajab", "Sya'ban",
        "Ramadhan", "Syawal", "Zulqa'dah", "Zulhijjah"
    )

    private val HIJRI_MONTHS_AR = listOf(
        "محرم", "صفر", "ربيع الأول", "ربيع الثاني",
        "جمادى الأولى", "جمادى الآخرة", "رجب", "شعبان",
        "رمضان", "شوال", "ذو القعدة", "ذو الحجة"
    )

    /**
     * Converts Gregorian date to Hijri date using Kuwaiti algorithm
     * with an optional day adjustment offset (-2..+2)
     */
    fun getHijriDate(gYear: Int, gMonth: Int, gDay: Int, offsetDays: Int = 0): HijriDate {
        val cal = GregorianCalendar(gYear, gMonth - 1, gDay)
        cal.add(Calendar.DAY_OF_MONTH, offsetDays)

        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH) + 1
        val day = cal.get(Calendar.DAY_OF_MONTH)

        var m = month
        var y = year
        if (m < 3) {
            y -= 1
            m += 12
        }

        val a = floor(y / 100.0)
        val b = 2 - a + floor(a / 4.0)
        val jd = floor(365.25 * (y + 4716)) + floor(30.6001 * (m + 1)) + day + b - 1524.5

        val z = jd - 1948440 + 10632
        val n = floor((z - 1) / 10631.0)
        val z1 = z - 10631 * n + 354
        val j = (floor((10982 - z1) / 5505.0)) * (floor((397 * z1 - 2486) / 30925.0)) +
                (floor(z1 / 7092.0)) * (floor((7091 - z1) / 30914.0))
        val z2 = z1 - (floor((30 - j) / 15.0)) * (floor((17719 * j / 50.0))) -
                (floor(j / 16.0)) * (floor((17700 * j / 50.0))) + 29
        val hMonth = floor((24 * z2 / 709.0)).toInt()
        val hDay = (z2 - floor((709 * hMonth / 24.0))).toInt()
        val hYear = (30 * n + j - 30).toInt()

        val validMonth = (hMonth - 1).coerceIn(0, 11)

        return HijriDate(
            day = hDay,
            month = validMonth + 1,
            year = hYear,
            monthNameId = HIJRI_MONTHS_ID[validMonth],
            monthNameAr = HIJRI_MONTHS_AR[validMonth]
        )
    }

    /**
     * Important Islamic Events for 2026 - 2036
     */
    fun getImportantEvents(year: Int): List<IslamicHoliday> {
        val events = mutableListOf<IslamicHoliday>()

        // Find key dates by iterating key months or mapping known accurate astronomical estimates
        // Sample major holidays mapping for 2026 to 2036
        when (year) {
            2026 -> {
                events.add(IslamicHoliday("Isra Mi'raj 1447 H", "إسراء ومعراج", "27 Rajab 1447 H", "16 Januari 2026", 2026, 1, 16))
                events.add(IslamicHoliday("Awal Ramadhan 1447 H", "بداية رمضان", "1 Ramadhan 1447 H", "18 Februari 2026", 2026, 2, 18))
                events.add(IslamicHoliday("Nuzulul Qur'an 1447 H", "نزول القرآن", "17 Ramadhan 1447 H", "6 Maret 2026", 2026, 3, 6))
                events.add(IslamicHoliday("Hari Raya Idul Fitri 1447 H", "عيد الفطر", "1 Syawal 1447 H", "20 Maret 2026", 2026, 3, 20))
                events.add(IslamicHoliday("Hari Raya Idul Adha 1447 H", "عيد الأضحى", "10 Zulhijjah 1447 H", "27 Mei 2026", 2026, 5, 27))
                events.add(IslamicHoliday("Tahun Baru Islam 1448 H", "رأس السنة الهجرية", "1 Muharram 1448 H", "16 Juni 2026", 2026, 6, 16))
                events.add(IslamicHoliday("Maulid Nabi Muhammad shallallahu 'alaihi wa sallam", "المولد النبوي", "12 Rabi'ul Awal 1448 H", "25 Agustus 2026", 2026, 8, 25))
            }
            2027 -> {
                events.add(IslamicHoliday("Isra Mi'raj 1448 H", "إسراء ومعراج", "27 Rajab 1448 H", "5 Januari 2027", 2027, 1, 5))
                events.add(IslamicHoliday("Awal Ramadhan 1448 H", "بداية رمضان", "1 Ramadhan 1448 H", "8 Februari 2027", 2027, 2, 8))
                events.add(IslamicHoliday("Hari Raya Idul Fitri 1448 H", "عيد الفطر", "1 Syawal 1448 H", "10 Maret 2027", 2027, 3, 10))
                events.add(IslamicHoliday("Hari Raya Idul Adha 1448 H", "عيد الأضحى", "10 Zulhijjah 1448 H", "17 Mei 2027", 2027, 5, 17))
                events.add(IslamicHoliday("Tahun Baru Islam 1449 H", "رأس السنة الهجرية", "1 Muharram 1449 H", "6 Juni 2027", 2027, 6, 6))
            }
            2028 -> {
                events.add(IslamicHoliday("Awal Ramadhan 1449 H", "بداية رمضان", "1 Ramadhan 1449 H", "28 Januari 2028", 2028, 1, 28))
                events.add(IslamicHoliday("Hari Raya Idul Fitri 1449 H", "عيد الفطر", "1 Syawal 1449 H", "27 Februari 2028", 2028, 2, 27))
                events.add(IslamicHoliday("Hari Raya Idul Adha 1449 H", "عيد الأضحى", "10 Zulhijjah 1449 H", "5 Mei 2028", 2028, 5, 5))
                events.add(IslamicHoliday("Tahun Baru Islam 1450 H", "رأس السنة الهجرية", "1 Muharram 1450 H", "25 Mei 2028", 2028, 5, 25))
            }
            2029 -> {
                events.add(IslamicHoliday("Awal Ramadhan 1450 H", "بداية رمضان", "1 Ramadhan 1450 H", "16 Januari 2029", 2029, 1, 16))
                events.add(IslamicHoliday("Hari Raya Idul Fitri 1450 H", "عيد الفطر", "1 Syawal 1450 H", "15 Februari 2029", 2029, 2, 15))
                events.add(IslamicHoliday("Hari Raya Idul Adha 1450 H", "عيد الأضحى", "10 Zulhijjah 1450 H", "24 April 2029", 2029, 4, 24))
                events.add(IslamicHoliday("Tahun Baru Islam 1451 H", "رأس السنة الهجرية", "1 Muharram 1451 H", "14 Mei 2029", 2029, 5, 14))
            }
            2030 -> {
                events.add(IslamicHoliday("Awal Ramadhan 1451 H", "بداية رمضان", "1 Ramadhan 1451 H", "5 Januari 2030", 2030, 1, 5))
                events.add(IslamicHoliday("Hari Raya Idul Fitri 1451 H", "عيد الفطر", "1 Syawal 1451 H", "4 Februari 2030", 2030, 2, 4))
                events.add(IslamicHoliday("Hari Raya Idul Adha 1451 H", "عيد الأضحى", "10 Zulhijjah 1451 H", "13 April 2030", 2030, 4, 13))
                events.add(IslamicHoliday("Tahun Baru Islam 1452 H", "رأس السنة الهجرية", "1 Muharram 1452 H", "3 Mei 2030", 2030, 5, 3))
                events.add(IslamicHoliday("Awal Ramadhan 1452 H (Ramadhan Ke-2)", "بداية رمضان الثاني", "1 Ramadhan 1452 H", "26 Desember 2030", 2030, 12, 26))
            }
            else -> {
                // Dynamically calculate Ramadhan & Idul Fitri estimates for 2031-2036
                for (m in 1..12) {
                    val h = getHijriDate(year, m, 1)
                    if (h.month == 9 && h.day == 1) {
                        events.add(IslamicHoliday("Awal Ramadhan ${h.year} H", "بداية رمضان", "1 Ramadhan ${h.year} H", "1 ${PrayerTimeCalculator.getIndonesianMonthName(m)} $year", year, m, 1))
                    } else if (h.month == 10 && h.day == 1) {
                        events.add(IslamicHoliday("Hari Raya Idul Fitri ${h.year} H", "عيد الفطر", "1 Syawal ${h.year} H", "1 ${PrayerTimeCalculator.getIndonesianMonthName(m)} $year", year, m, 1))
                    } else if (h.month == 12 && h.day == 10) {
                        events.add(IslamicHoliday("Hari Raya Idul Adha ${h.year} H", "عيد الأضحى", "10 Zulhijjah ${h.year} H", "10 ${PrayerTimeCalculator.getIndonesianMonthName(m)} $year", year, m, 10))
                    } else if (h.month == 1 && h.day == 1) {
                        events.add(IslamicHoliday("Tahun Baru Islam ${h.year} H", "رأس السنة الهجرية", "1 Muharram ${h.year} H", "1 ${PrayerTimeCalculator.getIndonesianMonthName(m)} $year", year, m, 1))
                    }
                }
            }
        }
        return events
    }
}
