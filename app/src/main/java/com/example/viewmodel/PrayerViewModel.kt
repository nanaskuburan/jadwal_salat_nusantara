package com.example.viewmodel

import android.app.Application
import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.R
import com.example.calculator.HijriCalendar
import com.example.calculator.PrayerTimeCalculator
import com.example.data.AppDatabase
import com.example.data.WorshipRecord
import com.example.data.WorshipRepository
import com.example.model.AppLanguage
import com.example.model.DayPrayerSchedule
import com.example.model.IslamicHoliday
import com.example.model.NgawiKecamatan
import com.example.model.PrayerTimeItem
import com.example.model.PrayerType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.ui.graphics.toArgb
import kotlinx.coroutines.launch
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.Locale
import java.util.TimeZone

data class CountdownState(
    val nextPrayerType: PrayerType? = null,
    val nextPrayerTimeStr: String = "",
    val hoursRemaining: Long = 0,
    val minutesRemaining: Long = 0,
    val secondsRemaining: Long = 0,
    val totalSecondsRemaining: Long = 0,
    val progress: Float = 0f
)

class PrayerViewModel(application: Application) : AndroidViewModel(application) {

    private val wibTimeZone = TimeZone.getTimeZone("Asia/Jakarta")

    private val repository: WorshipRepository

    // Settings & State
    private val _isOnline = MutableStateFlow(false)
    val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    init {
        val dao = AppDatabase.getDatabase(application).worshipDao()
        repository = WorshipRepository(dao)
        registerNetworkCallback()
    }

    private val prefs = application.getSharedPreferences("salat_prefs", android.content.Context.MODE_PRIVATE)

    private fun loadSavedLanguage(): AppLanguage {
        return try {
            val langStr = prefs.getString("app_language", AppLanguage.INDONESIAN.name) ?: AppLanguage.INDONESIAN.name
            AppLanguage.valueOf(langStr)
        } catch (e: Throwable) {
            AppLanguage.INDONESIAN
        }
    }

    private fun loadSavedThemeMode(): com.example.model.AppThemeMode {
        return try {
            val modeStr = prefs.getString("theme_mode", com.example.model.AppThemeMode.SYSTEM.name) ?: com.example.model.AppThemeMode.SYSTEM.name
            com.example.model.AppThemeMode.valueOf(modeStr)
        } catch (e: Throwable) {
            com.example.model.AppThemeMode.SYSTEM
        }
    }

    private fun loadSavedPrimaryColor(): androidx.compose.ui.graphics.Color {
        val defaultColor = androidx.compose.ui.graphics.Color(0xFF2E7D32)
        return try {
            if (prefs.contains("primary_theme_color_argb")) {
                val argb = prefs.getInt("primary_theme_color_argb", 0xFF2E7D32.toInt())
                androidx.compose.ui.graphics.Color(argb)
            } else {
                val colorLong = prefs.getLong("primary_theme_color", 0xFF2E7D32L)
                val c = androidx.compose.ui.graphics.Color(colorLong.toULong())
                c.toArgb()
                c
            }
        } catch (e: Throwable) {
            defaultColor
        }
    }

    private fun loadSavedDefaultLocation(): com.example.model.IndonesiaLocation {
        return try {
            val id = prefs.getString("default_location_id", "ngawi_kota") ?: "ngawi_kota"
            com.example.model.IndonesiaLocation.ALL_INDONESIA_LOCATIONS.firstOrNull { it.id == id }
                ?: com.example.model.IndonesiaLocation.NGAWI_KOTA
        } catch (e: Throwable) {
            com.example.model.IndonesiaLocation.NGAWI_KOTA
        }
    }

    private fun loadDownloadedLocations(): Set<String> {
        return try {
            val saved = prefs.getStringSet("downloaded_locations", null)
            saved?.toSet() ?: setOf("ngawi_kota")
        } catch (e: Throwable) {
            setOf("ngawi_kota")
        }
    }

    private fun loadSavedIhtiyati(): Int {
        return try { prefs.getInt("ihtiyati_minutes", 2) } catch (e: Throwable) { 2 }
    }

    private fun loadSavedHijriOffset(): Int {
        return try { prefs.getInt("hijri_offset", 0) } catch (e: Throwable) { 0 }
    }

    private fun loadSavedIsAzanEnabled(): Boolean {
        return try { prefs.getBoolean("is_azan_enabled", true) } catch (e: Throwable) { true }
    }

    private fun loadSavedEnabledAzanPrayers(): Set<PrayerType> {
        val defaultSet = setOf("SUBUH", "DZUHUR", "ASHAR", "MAGHRIB", "ISYA")
        return try {
            val savedSet = prefs.getStringSet("enabled_azan_prayers", defaultSet) ?: defaultSet
            savedSet.mapNotNull {
                try { PrayerType.valueOf(it) } catch (e: Exception) { null }
            }.toSet()
        } catch (e: Throwable) {
            setOf(PrayerType.SUBUH, PrayerType.DZUHUR, PrayerType.ASHAR, PrayerType.MAGHRIB, PrayerType.ISYA)
        }
    }

    private val _language = MutableStateFlow(loadSavedLanguage())
    val language: StateFlow<AppLanguage> = _language.asStateFlow()

    private val _themeMode = MutableStateFlow(loadSavedThemeMode())
    val themeMode: StateFlow<com.example.model.AppThemeMode> = _themeMode.asStateFlow()

    private val _primaryThemeColor = MutableStateFlow(loadSavedPrimaryColor())
    val primaryThemeColor: StateFlow<androidx.compose.ui.graphics.Color> = _primaryThemeColor.asStateFlow()

    fun setThemeMode(mode: com.example.model.AppThemeMode) {
        _themeMode.value = mode
        prefs.edit().putString("theme_mode", mode.name).apply()
    }

    fun setPrimaryThemeColor(color: androidx.compose.ui.graphics.Color) {
        _primaryThemeColor.value = color
        prefs.edit().putInt("primary_theme_color_argb", color.toArgb()).apply()
    }

    private val _selectedKecamatan = MutableStateFlow(NgawiKecamatan.ALL[0])
    val selectedKecamatan: StateFlow<NgawiKecamatan> = _selectedKecamatan.asStateFlow()

    private val _defaultLocation = MutableStateFlow(loadSavedDefaultLocation())
    val defaultLocation: StateFlow<com.example.model.IndonesiaLocation> = _defaultLocation.asStateFlow()

    private val _selectedLocation = MutableStateFlow(_defaultLocation.value)
    val selectedLocation: StateFlow<com.example.model.IndonesiaLocation> = _selectedLocation.asStateFlow()

    private val _downloadedLocations = MutableStateFlow(loadDownloadedLocations())
    val downloadedLocations: StateFlow<Set<String>> = _downloadedLocations.asStateFlow()

    private val _isLocationInsideNgawi = MutableStateFlow(_selectedLocation.value.isNgawiRegion)
    val isLocationInsideNgawi: StateFlow<Boolean> = _isLocationInsideNgawi.asStateFlow()

    data class BoundaryAlert(
        val previousRegency: String,
        val currentRegency: String,
        val province: String,
        val timestamp: Long,
        val show: Boolean = false
    )

    private val _boundaryAlert = MutableStateFlow<BoundaryAlert?>(null)
    val boundaryAlert: StateFlow<BoundaryAlert?> = _boundaryAlert.asStateFlow()

    fun dismissBoundaryAlert() {
        _boundaryAlert.value = _boundaryAlert.value?.copy(show = false)
    }

    private var lastKnownRegency: String
        get() = prefs.getString("last_known_regency", "") ?: ""
        set(value) = prefs.edit().putString("last_known_regency", value).apply()

    private fun extractRegencyName(fullName: String): String {
        return fullName
            .replace("Kabupaten", "Kab.")
            .replace("KABUPATEN", "Kab.")
            .replace("KOTA", "Kota")
            .replace("Kota", "Kota")
            .split(",") // in case of "locality, subAdmin"
            .lastOrNull()?.trim() ?: fullName
    }

    private val _isAutoGpsEnabled = MutableStateFlow(prefs.getBoolean("auto_gps_enabled", true))
    val isAutoGpsEnabled: StateFlow<Boolean> = _isAutoGpsEnabled.asStateFlow()

    private val _isSimpleUiMode = MutableStateFlow(prefs.getBoolean("is_simple_ui_mode", false))
    val isSimpleUiMode: StateFlow<Boolean> = _isSimpleUiMode.asStateFlow()

    private val _isOngoingNotificationEnabled = MutableStateFlow(prefs.getBoolean("is_ongoing_notification_enabled", true))
    val isOngoingNotificationEnabled: StateFlow<Boolean> = _isOngoingNotificationEnabled.asStateFlow()

    fun setSimpleUiMode(enabled: Boolean) {
        _isSimpleUiMode.value = enabled
        prefs.edit().putBoolean("is_simple_ui_mode", enabled).apply()
    }

    fun toggleOngoingNotification(enabled: Boolean) {
        _isOngoingNotificationEnabled.value = enabled
        prefs.edit().putBoolean("is_ongoing_notification_enabled", enabled).apply()
        if (enabled) {
            updateOngoingNotificationIfNeeded()
        } else {
            val appCtx = getApplication<Application>()
            com.example.receiver.PrayerNotificationHelper.cancelOngoingNotification(appCtx)
        }
    }

    fun updateOngoingNotificationIfNeeded() {
        val appCtx = getApplication<Application>()
        val sched = _todaySchedule.value ?: return
        val loc = _selectedLocation.value
        val subuh = sched.subuh
        val dzuhur = sched.dzuhur
        val ashar = sched.ashar
        val maghrib = sched.maghrib
        val isya = sched.isya
        val isId = _language.value == AppLanguage.INDONESIAN
        
        com.example.receiver.PrayerNotificationHelper.updateOngoingNotification(
            appCtx,
            loc.name,
            subuh,
            dzuhur,
            ashar,
            maghrib,
            isya,
            isId
        )
    }

    private val _userGpsCoords = MutableStateFlow<Pair<Double, Double>?>(null)
    val userGpsCoords: StateFlow<Pair<Double, Double>?> = _userGpsCoords.asStateFlow()

    fun setAutoGpsEnabled(enabled: Boolean) {
        _isAutoGpsEnabled.value = enabled
        prefs.edit().putBoolean("auto_gps_enabled", enabled).apply()
        if (enabled && _userGpsCoords.value != null) {
            val (lat, lon) = _userGpsCoords.value!!
            updateLocationFromGps(lat, lon)
        } else if (!enabled) {
            setLocation(_defaultLocation.value)
        }
    }

    fun setDefaultLocation(location: com.example.model.IndonesiaLocation) {
        _defaultLocation.value = location
        prefs.edit().putString("default_location_id", location.id).apply()
        downloadScheduleForLocation(location)
        if (!_isAutoGpsEnabled.value) {
            setLocation(location)
        }
        com.example.receiver.PrayerWidgetProvider.updateAllWidgets(getApplication())
    }

    fun updateLocationFromGps(lat: Double, lon: Double) {
        if (lat == 0.0 && lon == 0.0) return

        _userGpsCoords.value = Pair(lat, lon)

        if (_isAutoGpsEnabled.value) {
            viewModelScope.launch {
                val appCtx = getApplication<Application>()
                var resolvedName: String? = null
                var resolvedProvince: String? = null
                var subAdminAreaDetected: String? = null
                
                // Try using Android Geocoder for precise Google Maps boundary detection
                try {
                    kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
                        val geocoder = android.location.Geocoder(appCtx, java.util.Locale("id", "ID"))
                        @Suppress("DEPRECATION")
                        val addresses = geocoder.getFromLocation(lat, lon, 1)
                        if (!addresses.isNullOrEmpty()) {
                            val addr = addresses[0]
                            val subAdmin = addr.subAdminArea // Regency/City (e.g. "Kabupaten Ngawi", "Kabupaten Sragen")
                            subAdminAreaDetected = subAdmin
                            val admin = addr.adminArea // Province (e.g. "Jawa Timur")
                            val locality = addr.locality ?: addr.subLocality // Kecamatan/District
                            
                            resolvedProvince = admin
                            resolvedName = when {
                                !locality.isNullOrBlank() && !subAdmin.isNullOrBlank() -> {
                                    val formattedSubAdmin = subAdmin
                                        .replace("Kabupaten", "Kab.")
                                        .replace("KABUPATEN", "Kab.")
                                        .replace("KOTA", "Kota")
                                        .replace("Kota", "Kota")
                                    "$locality, $formattedSubAdmin"
                                }
                                !subAdmin.isNullOrBlank() -> {
                                    subAdmin.replace("Kabupaten", "Kab.")
                                        .replace("KABUPATEN", "Kab.")
                                        .replace("KOTA", "Kota")
                                        .replace("Kota", "Kota")
                                }
                                else -> null
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                // Get nearest standard city as safe reference for timezone and baseline
                val nearest = com.example.model.IndonesiaLocation.findNearestCity(lat, lon)
                
                val finalName = resolvedName ?: "${nearest.name} (GPS)"
                val finalProvince = resolvedProvince ?: nearest.province
                val finalIsNgawi = finalName.lowercase().contains("ngawi") || nearest.isNgawiRegion

                val currentRegencyRaw = subAdminAreaDetected ?: nearest.name
                val cleanCurrentRegency = extractRegencyName(currentRegencyRaw)
                val cleanPrevRegency = lastKnownRegency

                if (cleanPrevRegency.isNotEmpty() && cleanCurrentRegency.lowercase() != cleanPrevRegency.lowercase()) {
                    _boundaryAlert.value = BoundaryAlert(
                        previousRegency = cleanPrevRegency,
                        currentRegency = cleanCurrentRegency,
                        province = finalProvince,
                        timestamp = System.currentTimeMillis(),
                        show = true
                    )
                }
                lastKnownRegency = cleanCurrentRegency

                val exactGpsLocation = com.example.model.IndonesiaLocation(
                    id = "gps_exact_loc",
                    name = finalName,
                    province = finalProvince,
                    lat = lat,
                    lon = lon,
                    timeZoneId = nearest.timeZoneId,
                    timeZoneName = nearest.timeZoneName,
                    timeZoneOffsetHours = nearest.timeZoneOffsetHours,
                    isNgawiRegion = finalIsNgawi,
                    minuteOffset = 0
                )
                
                _selectedLocation.value = exactGpsLocation
                _isLocationInsideNgawi.value = finalIsNgawi
                
                if (finalIsNgawi) {
                    val kec = NgawiKecamatan.findNearest(lat, lon)
                    _selectedKecamatan.value = kec
                }
                
                downloadScheduleForLocation(exactGpsLocation)
                recalculateAll()
            }
        } else {
            // If GPS auto is off, still update boundary state for UI notifications
            val nearest = com.example.model.IndonesiaLocation.findNearestCity(lat, lon)
            val finalIsNgawi = nearest.isNgawiRegion
            _isLocationInsideNgawi.value = finalIsNgawi
        }
    }

    fun setLocation(location: com.example.model.IndonesiaLocation) {
        _isAutoGpsEnabled.value = false
        prefs.edit().putBoolean("auto_gps_enabled", false).apply()
        _selectedLocation.value = location
        _isLocationInsideNgawi.value = location.isNgawiRegion
        if (location.isNgawiRegion) {
            val matchedKec = NgawiKecamatan.ALL.firstOrNull { it.name.lowercase() == location.name.lowercase() }
            if (matchedKec != null) {
                _selectedKecamatan.value = matchedKec
            }
        }
        downloadScheduleForLocation(location)
        recalculateAll()
        com.example.receiver.PrayerWidgetProvider.updateAllWidgets(getApplication())
    }

    fun downloadScheduleForLocation(location: com.example.model.IndonesiaLocation) {
        val current = _downloadedLocations.value.toMutableSet()
        current.add(location.id)
        _downloadedLocations.value = current
        prefs.edit().putStringSet("downloaded_locations", current).apply()
        recalculateAll()
    }

    private val _ihtiyatiMinutes = MutableStateFlow(loadSavedIhtiyati())
    val ihtiyatiMinutes: StateFlow<Int> = _ihtiyatiMinutes.asStateFlow()

    private val _hijriOffset = MutableStateFlow(loadSavedHijriOffset())
    val hijriOffset: StateFlow<Int> = _hijriOffset.asStateFlow()

    // Azan Alarm Settings
    private val _isAzanEnabled = MutableStateFlow(loadSavedIsAzanEnabled())
    val isAzanEnabled: StateFlow<Boolean> = _isAzanEnabled.asStateFlow()

    private val _isAzanPlaying = MutableStateFlow(false)
    val isAzanPlaying: StateFlow<Boolean> = _isAzanPlaying.asStateFlow()

    private val _enabledAzanPrayers = MutableStateFlow(loadSavedEnabledAzanPrayers())
    val enabledAzanPrayers: StateFlow<Set<PrayerType>> = _enabledAzanPrayers.asStateFlow()

    private var mediaPlayer: MediaPlayer? = null
    private var lastTriggeredPrayerKey: String? = null

    // Date Selection
    private val todayCal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Jakarta"))
    private val _selectedYear = MutableStateFlow(todayCal.get(Calendar.YEAR))
    val selectedYear: StateFlow<Int> = _selectedYear.asStateFlow()

    private val _selectedMonth = MutableStateFlow(todayCal.get(Calendar.MONTH) + 1)
    val selectedMonth: StateFlow<Int> = _selectedMonth.asStateFlow()

    private val _selectedDay = MutableStateFlow(todayCal.get(Calendar.DAY_OF_MONTH))
    val selectedDay: StateFlow<Int> = _selectedDay.asStateFlow()

    // Today's & Selected Day Schedules
    private val _todaySchedule = MutableStateFlow<DayPrayerSchedule?>(null)
    val todaySchedule: StateFlow<DayPrayerSchedule?> = _todaySchedule.asStateFlow()

    private val _selectedSchedule = MutableStateFlow<DayPrayerSchedule?>(null)
    val selectedSchedule: StateFlow<DayPrayerSchedule?> = _selectedSchedule.asStateFlow()

    private val _monthlyScheduleList = MutableStateFlow<List<DayPrayerSchedule>>(emptyList())
    val monthlyScheduleList: StateFlow<List<DayPrayerSchedule>> = _monthlyScheduleList.asStateFlow()

    // Countdown State
    private val _countdownState = MutableStateFlow(CountdownState())
    val countdownState: StateFlow<CountdownState> = _countdownState.asStateFlow()

    // Worship Record State
    private val _todayRecord = MutableStateFlow(WorshipRecord(date = getTodayDateString()))
    val todayRecord: StateFlow<WorshipRecord> = _todayRecord.asStateFlow()

    // Islamic Holidays
    private val _holidays = MutableStateFlow<List<IslamicHoliday>>(emptyList())
    val holidays: StateFlow<List<IslamicHoliday>> = _holidays.asStateFlow()

    // TTS Audio
    init {
        // Observe Room DB for today's record
        viewModelScope.launch {
            repository.getRecordByDate(getTodayDateString()).collectLatest { record ->
                if (record != null) {
                    _todayRecord.value = record
                } else {
                    _todayRecord.value = WorshipRecord(date = getTodayDateString())
                }
            }
        }

        recalculateAll()
        startCountdownTimer()
    }

    fun setAzanEnabled(enabled: Boolean) {
        _isAzanEnabled.value = enabled
        prefs.edit().putBoolean("is_azan_enabled", enabled).apply()
        if (!enabled) {
            stopAzanNabawi()
        }
    }

    fun togglePrayerAzan(type: PrayerType) {
        val current = _enabledAzanPrayers.value.toMutableSet()
        if (current.contains(type)) {
            current.remove(type)
        } else {
            current.add(type)
        }
        _enabledAzanPrayers.value = current
        prefs.edit().putStringSet("enabled_azan_prayers", current.map { it.name }.toSet()).apply()
    }

    private var fallbackJob: kotlinx.coroutines.Job? = null

    fun playAzanNabawi() {
        stopAzanNabawi(showToast = false)
        val appCtx = getApplication<Application>()
        android.util.Log.d("PrayerViewModel", "Starting playAzanNabawi...")
        val isId = _language.value == AppLanguage.INDONESIAN
        
        try {
            // Force device volume to maximum for STREAM_MUSIC
            try {
                val audioManager = appCtx.getSystemService(android.content.Context.AUDIO_SERVICE) as android.media.AudioManager
                val maxVolume = audioManager.getStreamMaxVolume(android.media.AudioManager.STREAM_MUSIC)
                audioManager.setStreamVolume(android.media.AudioManager.STREAM_MUSIC, maxVolume, 0)
            } catch (e: Exception) {
                android.util.Log.e("PrayerViewModel", "Failed to set audio volume", e)
            }

            var mp: MediaPlayer? = null

            // Strategy 1: Standard Android MediaPlayer.create helper
            try {
                mp = MediaPlayer.create(appCtx, R.raw.azan_nabawi)
                if (mp != null) {
                    android.util.Log.d("PrayerViewModel", "Initialized via MediaPlayer.create successfully.")
                }
            } catch (e: Exception) {
                android.util.Log.e("PrayerViewModel", "Strategy 1 (MediaPlayer.create) failed", e)
            }

            // Strategy 2: openRawResourceFd (keeping AssetFileDescriptor open until after prepare)
            if (mp == null) {
                try {
                    val afd = appCtx.resources.openRawResourceFd(R.raw.azan_nabawi)
                    if (afd != null) {
                        val tempMp = MediaPlayer()
                        tempMp.setAudioAttributes(
                            AudioAttributes.Builder()
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .build()
                        )
                        tempMp.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                        tempMp.prepare()
                        afd.close()
                        mp = tempMp
                        android.util.Log.d("PrayerViewModel", "Initialized via openRawResourceFd successfully.")
                    }
                } catch (e: Exception) {
                    android.util.Log.e("PrayerViewModel", "Strategy 2 (openRawResourceFd) failed", e)
                }
            }

            // Strategy 3: Uri fallback
            if (mp == null) {
                try {
                    val tempMp = MediaPlayer()
                    val uri = android.net.Uri.parse("android.resource://${appCtx.packageName}/${R.raw.azan_nabawi}")
                    tempMp.setDataSource(appCtx, uri)
                    tempMp.setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
                    )
                    tempMp.prepare()
                    mp = tempMp
                    android.util.Log.d("PrayerViewModel", "Initialized via Uri fallback successfully.")
                } catch (e: Exception) {
                    android.util.Log.e("PrayerViewModel", "Strategy 3 (Uri fallback) failed", e)
                }
            }

            if (mp != null) {
                mp.setVolume(1.0f, 1.0f)

                mp.setOnCompletionListener { player ->
                    android.util.Log.d("PrayerViewModel", "MediaPlayer playback completed.")
                    _isAzanPlaying.value = false
                    try {
                        player.reset()
                        player.release()
                    } catch (_: Exception) {}
                    if (mediaPlayer == player) {
                        mediaPlayer = null
                    }
                }

                mp.setOnErrorListener { player, what, extra ->
                    android.util.Log.w("PrayerViewModel", "MediaPlayer error: what=$what, extra=$extra. Executing tone fallback...")
                    _isAzanPlaying.value = false
                    try {
                        player.reset()
                        player.release()
                    } catch (_: Exception) {}
                    if (mediaPlayer == player) {
                        mediaPlayer = null
                    }
                    playFallbackChime()
                    true
                }

                mediaPlayer = mp
                mp.start()
                _isAzanPlaying.value = true

                android.widget.Toast.makeText(
                    appCtx,
                    if (isId) "Memutar Azan Nabawi..." else "جاري تشغيل الأذان النبوي...",
                    android.widget.Toast.LENGTH_SHORT
                ).show()

            } else {
                android.util.Log.w("PrayerViewModel", "MediaPlayer could not be initialized, playing fallback chime.")
                playFallbackChime()
            }

        } catch (e: Exception) {
            android.util.Log.e("PrayerViewModel", "Error playing Azan", e)
            _isAzanPlaying.value = false
            mediaPlayer = null
            playFallbackChime()
        }
    }

    private fun playFallbackChime() {
        fallbackJob?.cancel()
        val appCtx = getApplication<Application>()
        val isId = _language.value == AppLanguage.INDONESIAN
        _isAzanPlaying.value = true
        
        android.widget.Toast.makeText(
            appCtx,
            if (isId) "Memutar Nada Pengingat Salat..." else "جاري تشغيل تنبيه Salat...",
            android.widget.Toast.LENGTH_SHORT
        ).show()

        fallbackJob = viewModelScope.launch(kotlinx.coroutines.Dispatchers.Default) {
            try {
                val toneGen = android.media.ToneGenerator(android.media.AudioManager.STREAM_MUSIC, 100)
                val tones = listOf(
                    android.media.ToneGenerator.TONE_PROP_BEEP,
                    android.media.ToneGenerator.TONE_PROP_BEEP2,
                    android.media.ToneGenerator.TONE_CDMA_HIGH_L,
                    android.media.ToneGenerator.TONE_PROP_BEEP
                )
                for (i in 0 until 4) {
                    if (!_isAzanPlaying.value) break
                    for (tone in tones) {
                        if (!_isAzanPlaying.value) break
                        toneGen.startTone(tone, 350)
                        kotlinx.coroutines.delay(450)
                    }
                    kotlinx.coroutines.delay(500)
                }
                toneGen.release()
            } catch (e: Exception) {
                android.util.Log.e("PrayerViewModel", "Fallback chime error", e)
            } finally {
                _isAzanPlaying.value = false
            }
        }
    }

    fun stopAzanNabawi(showToast: Boolean = true) {
        fallbackJob?.cancel()
        fallbackJob = null
        val mp = mediaPlayer
        mediaPlayer = null
        _isAzanPlaying.value = false
        if (mp != null) {
            try {
                mp.setOnCompletionListener(null)
                mp.setOnErrorListener(null)
                mp.setOnPreparedListener(null)
                try {
                    mp.stop()
                } catch (_: Exception) {}
                try {
                    mp.reset()
                } catch (_: Exception) {}
                try {
                    mp.release()
                } catch (_: Exception) {}
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if (showToast) {
            val appCtx = getApplication<Application>()
            val isId = _language.value == AppLanguage.INDONESIAN
            android.widget.Toast.makeText(
                appCtx,
                if (isId) "Azan dihentikan" else "تم إيقاف الأذان",
                android.widget.Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopAzanNabawi()
    }

    fun setLanguage(lang: AppLanguage) {
        _language.value = lang
        prefs.edit().putString("app_language", lang.name).apply()
    }

    fun setKecamatan(kecamatan: NgawiKecamatan) {
        _selectedKecamatan.value = kecamatan
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
        setLocation(loc)
    }

    fun setIhtiyati(minutes: Int) {
        _ihtiyatiMinutes.value = minutes
        prefs.edit().putInt("ihtiyati_minutes", minutes).apply()
        recalculateAll()
    }

    fun setHijriOffset(offset: Int) {
        _hijriOffset.value = offset
        prefs.edit().putInt("hijri_offset", offset).apply()
        recalculateAll()
    }

    fun setYearMonthDay(year: Int, month: Int, day: Int) {
        _selectedYear.value = year
        _selectedMonth.value = month
        _selectedDay.value = day
        recalculateAll()
    }

    fun setSelectedMonth(month: Int) {
        _selectedMonth.value = month
        recalculateAll()
    }

    fun setSelectedYear(year: Int) {
        _selectedYear.value = year
        recalculateAll()
    }

    private fun recalculateAll() {
        val loc = _selectedLocation.value
        val locTz = TimeZone.getTimeZone(loc.timeZoneId)
        val now = Calendar.getInstance(locTz)
        val tYear = now.get(Calendar.YEAR)
        val tMonth = now.get(Calendar.MONTH) + 1
        val tDay = now.get(Calendar.DAY_OF_MONTH)

        val isDownloaded = loc.isNgawiRegion || _downloadedLocations.value.contains(loc.id)

        val tSchedule = PrayerTimeCalculator.calculateForLocation(
            tYear, tMonth, tDay,
            loc,
            _ihtiyatiMinutes.value,
            _hijriOffset.value
        ).copy(isDownloadedOffline = isDownloaded)
        _todaySchedule.value = tSchedule

        val selSchedule = PrayerTimeCalculator.calculateForLocation(
            _selectedYear.value, _selectedMonth.value, _selectedDay.value,
            loc,
            _ihtiyatiMinutes.value,
            _hijriOffset.value
        ).copy(isDownloadedOffline = isDownloaded)
        _selectedSchedule.value = selSchedule

        // Monthly schedule
        val maxDays = GregorianCalendar(locTz).apply {
            set(Calendar.YEAR, _selectedYear.value)
            set(Calendar.MONTH, _selectedMonth.value - 1)
            set(Calendar.DAY_OF_MONTH, 1)
        }.getActualMaximum(Calendar.DAY_OF_MONTH)

        val monthlyList = mutableListOf<DayPrayerSchedule>()
        for (d in 1..maxDays) {
            monthlyList.add(
                PrayerTimeCalculator.calculateForLocation(
                    _selectedYear.value, _selectedMonth.value, d,
                    loc,
                    _ihtiyatiMinutes.value,
                    _hijriOffset.value
                ).copy(isDownloadedOffline = isDownloaded)
            )
        }
        _monthlyScheduleList.value = monthlyList

        // Holidays for selected year
        _holidays.value = HijriCalendar.getImportantEvents(_selectedYear.value)
        
        // Update alarms
        val appCtx = getApplication<Application>()
        val updateIntent = android.content.Intent(appCtx, com.example.receiver.PrayerAlarmReceiver::class.java).apply {
            action = "com.example.ACTION_UPDATE_ALARMS"
        }
        appCtx.sendBroadcast(updateIntent)
        updateOngoingNotificationIfNeeded()
    }

    private fun startCountdownTimer() {
        viewModelScope.launch {
            while (true) {
                updateCountdown()
                delay(1000)
            }
        }
    }

    private fun updateCountdown() {
        val loc = _selectedLocation.value
        val locTz = TimeZone.getTimeZone(loc.timeZoneId)
        val now = Calendar.getInstance(locTz)
        val curYear = now.get(Calendar.YEAR)
        val curMonth = now.get(Calendar.MONTH) + 1
        val curDay = now.get(Calendar.DAY_OF_MONTH)

        // Resync today's schedule if device date changes or schedule is missing
        var tSchedule = _todaySchedule.value
        if (tSchedule == null || tSchedule.year != curYear || tSchedule.month != curMonth || tSchedule.day != curDay) {
            val isDownloaded = loc.isNgawiRegion || _downloadedLocations.value.contains(loc.id)
            tSchedule = PrayerTimeCalculator.calculateForLocation(
                curYear, curMonth, curDay,
                loc,
                _ihtiyatiMinutes.value,
                _hijriOffset.value
            ).copy(isDownloadedOffline = isDownloaded)
            _todaySchedule.value = tSchedule
        }

        val nowMillis = System.currentTimeMillis()

        // Find next prayer today
        var nextItem: PrayerTimeItem? = null
        val corePrayers = tSchedule.prayerList.filter {
            it.type in listOf(PrayerType.SUBUH, PrayerType.DZUHUR, PrayerType.ASHAR, PrayerType.MAGHRIB, PrayerType.ISYA)
        }

        for (item in corePrayers) {
            if (item.timestampMillis > nowMillis) {
                nextItem = item
                break
            }
        }

        var targetMillis = nextItem?.timestampMillis
        var nextType = nextItem?.type

        if (targetMillis == null) {
            // All prayers passed today, target Subuh tomorrow
            val tomorrowCal = Calendar.getInstance(locTz)
            tomorrowCal.add(Calendar.DAY_OF_MONTH, 1)
            val isDownloaded = loc.isNgawiRegion || _downloadedLocations.value.contains(loc.id)
            val tomorrowSched = PrayerTimeCalculator.calculateForLocation(
                tomorrowCal.get(Calendar.YEAR),
                tomorrowCal.get(Calendar.MONTH) + 1,
                tomorrowCal.get(Calendar.DAY_OF_MONTH),
                loc,
                _ihtiyatiMinutes.value,
                _hijriOffset.value
            ).copy(isDownloadedOffline = isDownloaded)
            val subuhItem = tomorrowSched.prayerList.firstOrNull { it.type == PrayerType.SUBUH }
            targetMillis = subuhItem?.timestampMillis
            nextType = PrayerType.SUBUH
            nextItem = subuhItem
        }

        if (targetMillis != null) {
            val diffSec = ((targetMillis - nowMillis) / 1000).coerceAtLeast(0)
            val hours = diffSec / 3600
            val mins = (diffSec % 3600) / 60
            val secs = diffSec % 60

            // Trigger Azan Alarm when prayer time arrives (0 to 3 seconds)
            if (diffSec <= 3 && nextType != null && _isAzanEnabled.value && _enabledAzanPrayers.value.contains(nextType)) {
                val triggerKey = "${nextType.name}_${tSchedule.year}_${tSchedule.month}_${tSchedule.day}"
                if (lastTriggeredPrayerKey != triggerKey) {
                    lastTriggeredPrayerKey = triggerKey
                    playAzanNabawi()
                }
            }

            // Max interval for prayer transition is approx 4-6 hours (18000 sec)
            val progress = (1.0f - (diffSec.toFloat() / 18000f)).coerceIn(0f, 1f)

            _countdownState.value = CountdownState(
                nextPrayerType = nextType,
                nextPrayerTimeStr = nextItem?.timeFormatted ?: tSchedule.subuh,
                hoursRemaining = hours,
                minutesRemaining = mins,
                secondsRemaining = secs,
                totalSecondsRemaining = diffSec,
                progress = progress
            )
        }
    }

    fun togglePrayerCompleted(type: PrayerType) {
        viewModelScope.launch {
            val current = _todayRecord.value
            val updated = when (type) {
                PrayerType.SUBUH -> current.copy(subuh = !current.subuh)
                PrayerType.DZUHUR -> current.copy(dzuhur = !current.dzuhur)
                PrayerType.ASHAR -> current.copy(ashar = !current.ashar)
                PrayerType.MAGHRIB -> current.copy(maghrib = !current.maghrib)
                PrayerType.ISYA -> current.copy(isya = !current.isya)
                PrayerType.DHUHA -> current.copy(dhuha = !current.dhuha)
                else -> current
            }
            _todayRecord.value = updated
            repository.saveRecord(updated)
        }
    }

    private fun getTodayDateString(): String {
        val loc = _selectedLocation.value
        val locTz = TimeZone.getTimeZone(loc.timeZoneId)
        val cal = Calendar.getInstance(locTz)
        val y = cal.get(Calendar.YEAR)
        val m = cal.get(Calendar.MONTH) + 1
        val d = cal.get(Calendar.DAY_OF_MONTH)
        return String.format(Locale.US, "%04d-%02d-%02d", y, m, d)
    }

    private fun checkCurrentNetworkStatus(connectivityManager: ConnectivityManager) {
        try {
            val activeNetwork = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
            _isOnline.value = capabilities != null && (
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            )
        } catch (e: Exception) {
            _isOnline.value = false
        }
    }

    private fun registerNetworkCallback() {
        try {
            val connectivityManager = getApplication<Application>().getSystemService(android.content.Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            checkCurrentNetworkStatus(connectivityManager)
            
            val request = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()
            
            connectivityManager.registerNetworkCallback(request, object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    _isOnline.value = true
                }
                override fun onLost(network: Network) {
                    _isOnline.value = false
                }
            })
        } catch (e: Exception) {
            android.util.Log.e("PrayerViewModel", "Failed to register network callback", e)
        }
    }
}
