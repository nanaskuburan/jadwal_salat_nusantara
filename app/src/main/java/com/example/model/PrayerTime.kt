package com.example.model

enum class AppLanguage(val code: String, val displayName: String, val nativeName: String) {
    INDONESIAN("id", "Bahasa Indonesia", "Bahasa Indonesia"),
    ARABIC("ar", "Bahasa Arab", "العربية")
}

enum class AppThemeMode {
    SYSTEM, LIGHT, DARK
}

data class NgawiKecamatan(
    val name: String,
    val lat: Double,
    val lon: Double,
    val minuteOffset: Int = 0 // Minute adjustment relative to Ngawi Kota
) {
    companion object {
        val ALL = listOf(
            NgawiKecamatan("Ngawi Kota", -7.4039, 111.4461, 0),
            NgawiKecamatan("Paron", -7.4333, 111.3833, 0),
            NgawiKecamatan("Geneng", -7.4833, 111.4333, 0),
            NgawiKecamatan("Ngrambe", -7.5333, 111.2000, 1),
            NgawiKecamatan("Jogorogo", -7.5167, 111.2667, 1),
            NgawiKecamatan("Sine", -7.5167, 111.1500, 1),
            NgawiKecamatan("Kedunggalar", -7.3833, 111.3167, 0),
            NgawiKecamatan("Widodaren", -7.3667, 111.1833, 1),
            NgawiKecamatan("Mantingan", -7.3500, 111.1000, 1),
            NgawiKecamatan("Karanganyar", -7.3167, 111.1333, 1),
            NgawiKecamatan("Pitu", -7.3500, 111.3833, 0),
            NgawiKecamatan("Bringin", -7.3333, 111.5333, -1),
            NgawiKecamatan("Padas", -7.4167, 111.5167, -1),
            NgawiKecamatan("Kasreman", -7.3833, 111.4833, 0),
            NgawiKecamatan("Pangkur", -7.4500, 111.5167, -1),
            NgawiKecamatan("Kwadungan", -7.5000, 111.5000, -1),
            NgawiKecamatan("Kendal", -7.5500, 111.3167, 0),
            NgawiKecamatan("Gerih", -7.5167, 111.4167, 0),
            NgawiKecamatan("Karangjati", -7.4667, 111.6000, -1)
        )

        fun findNearest(lat: Double, lon: Double): NgawiKecamatan {
            return ALL.minByOrNull { kec ->
                val dLat = Math.toRadians(kec.lat - lat)
                val dLon = Math.toRadians(kec.lon - lon)
                val a = kotlin.math.sin(dLat / 2) * kotlin.math.sin(dLat / 2) +
                        kotlin.math.cos(Math.toRadians(lat)) * kotlin.math.cos(Math.toRadians(kec.lat)) *
                        kotlin.math.sin(dLon / 2) * kotlin.math.sin(dLon / 2)
                2 * kotlin.math.atan2(kotlin.math.sqrt(a), kotlin.math.sqrt(1 - a))
            } ?: ALL[0]
        }
    }
}

enum class PrayerType(val idName: String, val arName: String) {
    IMSAK("Imsak", "الإمساك"),
    SUBUH("Subuh", "الفجر"),
    TERBIT("Terbit", "الشروق"),
    DHUHA("Dhuha", "الضحى"),
    DZUHUR("Dzuhur", "الظهر"),
    ASHAR("Ashar", "العصر"),
    MAGHRIB("Maghrib", "المغرب"),
    ISYA("Isya", "العشاء")
}

data class PrayerTimeItem(
    val type: PrayerType,
    val timeFormatted: String, // HH:mm
    val timestampMillis: Long,
    val isPassed: Boolean = false,
    val isNext: Boolean = false
)

data class DayPrayerSchedule(
    val year: Int,
    val month: Int,
    val day: Int,
    val dayOfWeekNameId: String,
    val dayOfWeekNameAr: String,
    val gregorianFormattedId: String,
    val gregorianFormattedAr: String,
    val hijriFormattedId: String,
    val hijriFormattedAr: String,
    val imsak: String,
    val subuh: String,
    val terbit: String,
    val dhuha: String,
    val dzuhur: String,
    val ashar: String,
    val maghrib: String,
    val isya: String,
    val prayerList: List<PrayerTimeItem>,
    val locationName: String = "Ngawi Kota",
    val province: String = "Jawa Timur",
    val timeZoneName: String = "WIB",
    val timeZoneId: String = "Asia/Jakarta",
    val isNgawiRegion: Boolean = true,
    val isDownloadedOffline: Boolean = true
)

data class IslamicHoliday(
    val nameId: String,
    val nameAr: String,
    val hijriDateStr: String,
    val gregorianDateStr: String,
    val year: Int,
    val month: Int,
    val day: Int
)
