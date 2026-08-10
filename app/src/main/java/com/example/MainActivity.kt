package com.example

import android.Manifest
import android.app.AppOpsManager
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.receiver.AlarmScheduler
import com.example.ui.components.AppBottomNavigationBar
import com.example.ui.components.AppHeader
import com.example.ui.components.AppNavigationRail
import com.example.ui.screens.CalendarScreen
import com.example.ui.screens.DuaScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.QiblaScreen
import com.example.ui.screens.TrackerScreen
import com.example.ui.theme.SalatNgawiTheme
import com.example.model.AppLanguage
import com.example.viewmodel.PrayerViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: PrayerViewModel by viewModels()
    private var activeLocationListener: LocationListener? = null

    override fun onResume() {
        super.onResume()
        if (viewModel.isAutoGpsEnabled.value) {
            initLocationUpdates(this)
        }
    }

    override fun onPause() {
        super.onPause()
        activeLocationListener?.let { listener ->
            try {
                val locationManager = getSystemService(Context.LOCATION_SERVICE) as? LocationManager
                locationManager?.removeUpdates(listener)
            } catch (e: Throwable) {
                // Ignored
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        enableHighRefreshRate()

        setContent {
            val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
            val primaryThemeColor by viewModel.primaryThemeColor.collectAsStateWithLifecycle()
            val language by viewModel.language.collectAsStateWithLifecycle()
            val isOnline by viewModel.isOnline.collectAsStateWithLifecycle()
            val context = LocalContext.current

            // Notification Permission
            val notificationPermissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission()
            ) { isGranted ->
                if (isGranted) {
                    AlarmScheduler.rescheduleNextPrayer(context)
                }
            }
            
            LaunchedEffect(Unit) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    } else {
                        AlarmScheduler.rescheduleNextPrayer(context)
                    }
                } else {
                    AlarmScheduler.rescheduleNextPrayer(context)
                }
            }

            // Auto Location Setup
            val locationPermissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestMultiplePermissions()
            ) { permissions ->
                val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                        permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
                if (granted) {
                    initLocationUpdates(context)
                }
            }

            LaunchedEffect(Unit) {
                val hasFine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                val hasCoarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                if (hasFine || hasCoarse) {
                    initLocationUpdates(context)
                } else {
                    locationPermissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }
            }

            SalatNgawiTheme(
                themeMode = themeMode,
                primaryColor = primaryThemeColor,
                language = language
            ) {
                val selectedKecamatan by viewModel.selectedKecamatan.collectAsStateWithLifecycle()
                val defaultLocation by viewModel.defaultLocation.collectAsStateWithLifecycle()
                val selectedLocation by viewModel.selectedLocation.collectAsStateWithLifecycle()
                val downloadedLocations by viewModel.downloadedLocations.collectAsStateWithLifecycle()
                val isAutoGpsEnabled by viewModel.isAutoGpsEnabled.collectAsStateWithLifecycle()
                val todaySchedule by viewModel.todaySchedule.collectAsStateWithLifecycle()
                val selectedSchedule by viewModel.selectedSchedule.collectAsStateWithLifecycle()
                val monthlySchedules by viewModel.monthlyScheduleList.collectAsStateWithLifecycle()
                val countdownState by viewModel.countdownState.collectAsStateWithLifecycle()
                val todayRecord by viewModel.todayRecord.collectAsStateWithLifecycle()
                val holidays by viewModel.holidays.collectAsStateWithLifecycle()
                val selectedYear by viewModel.selectedYear.collectAsStateWithLifecycle()
                val selectedMonth by viewModel.selectedMonth.collectAsStateWithLifecycle()
                val ihtiyatiMinutes by viewModel.ihtiyatiMinutes.collectAsStateWithLifecycle()
                val hijriOffset by viewModel.hijriOffset.collectAsStateWithLifecycle()
                val isAzanEnabled by viewModel.isAzanEnabled.collectAsStateWithLifecycle()
                val isAzanPlaying by viewModel.isAzanPlaying.collectAsStateWithLifecycle()
                val enabledAzanPrayers by viewModel.enabledAzanPrayers.collectAsStateWithLifecycle()
                val isSimpleUiMode by viewModel.isSimpleUiMode.collectAsStateWithLifecycle()
                val isOngoingNotificationEnabled by viewModel.isOngoingNotificationEnabled.collectAsStateWithLifecycle()
                val userGpsCoords by viewModel.userGpsCoords.collectAsStateWithLifecycle()
                val boundaryAlert by viewModel.boundaryAlert.collectAsStateWithLifecycle()

                LaunchedEffect(isAutoGpsEnabled) {
                    if (isAutoGpsEnabled) {
                        val hasFine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        val hasCoarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        if (hasFine || hasCoarse) {
                            initLocationUpdates(context)
                        }
                    }
                }

                var currentTab by remember { mutableIntStateOf(0) }
                var showLocationSheet by remember { androidx.compose.runtime.mutableStateOf(false) }
                var showSplash by remember { androidx.compose.runtime.mutableStateOf(true) }

                val configuration = LocalConfiguration.current
                val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

                val layoutDirection = if (language == AppLanguage.ARABIC) LayoutDirection.Rtl else LayoutDirection.Ltr

                CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        if (showLocationSheet) {
                            com.example.ui.components.LocationSelectorSheet(
                                selectedLocation = selectedLocation,
                                defaultLocation = defaultLocation,
                                downloadedLocations = downloadedLocations,
                                isAutoGpsEnabled = isAutoGpsEnabled,
                                userGpsCoords = userGpsCoords,
                                language = language,
                                onSelectLocation = { viewModel.setLocation(it) },
                                onSetDefaultLocation = { viewModel.setDefaultLocation(it) },
                                onDownloadLocation = { viewModel.downloadScheduleForLocation(it) },
                                onToggleAutoGps = { viewModel.setAutoGpsEnabled(it) },
                                onRequestGpsRefresh = {
                                    viewModel.setAutoGpsEnabled(true)
                                    val hasFine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                                    val hasCoarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                                    if (!hasFine && !hasCoarse) {
                                        locationPermissionLauncher.launch(
                                            arrayOf(
                                                Manifest.permission.ACCESS_FINE_LOCATION,
                                                Manifest.permission.ACCESS_COARSE_LOCATION
                                            )
                                        )
                                    } else {
                                        initLocationUpdates(context)
                                    }
                                    Toast.makeText(
                                        context,
                                        if (language == AppLanguage.INDONESIAN) "Mendeteksi koordinat GPS presisi..." else "يتم الآن تحديد إحداثيات GPS الدقيقة...",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                onDismiss = { showLocationSheet = false }
                            )
                        }

                        Scaffold(
                    topBar = {
                        AppHeader(
                            language = language,
                            selectedKecamatan = selectedKecamatan,
                            selectedLocation = selectedLocation,
                            isAutoGpsEnabled = isAutoGpsEnabled,
                            isSimpleUiMode = isSimpleUiMode,
                            onLanguageChange = { viewModel.setLanguage(it) },
                            onKecamatanChange = { viewModel.setKecamatan(it) },
                            onOpenLocationPicker = { showLocationSheet = true },
                            onAutoGpsToggle = { viewModel.setAutoGpsEnabled(it) },
                            onToggleSimpleUiMode = { viewModel.setSimpleUiMode(it) }
                        )
                    },
                    bottomBar = {
                        if (!isLandscape) {
                            AppBottomNavigationBar(
                                selectedTab = currentTab,
                                language = language,
                                onTabSelected = { currentTab = it }
                            )
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        if (isLandscape) {
                            AppNavigationRail(
                                selectedTab = currentTab,
                                language = language,
                                onTabSelected = { currentTab = it }
                            )
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            Box(
                                modifier = Modifier
                                    .widthIn(max = 640.dp)
                                    .fillMaxHeight()
                            ) {
                                when (currentTab) {
                                    0 -> HomeScreen(
                                    language = language,
                                    kecamatan = selectedKecamatan,
                                    selectedLocation = selectedLocation,
                                    downloadedLocations = downloadedLocations,
                                    isAutoGpsEnabled = isAutoGpsEnabled,
                                    todaySchedule = todaySchedule,
                                    countdownState = countdownState,
                                    todayRecord = todayRecord,
                                    isAzanEnabled = isAzanEnabled,
                                    isAzanPlaying = isAzanPlaying,
                                    isOnline = isOnline,
                                    isSimpleUiMode = isSimpleUiMode,
                                    currentThemeMode = themeMode,
                                    currentPrimaryColor = primaryThemeColor,
                                    boundaryAlert = boundaryAlert,
                                    onDismissBoundaryAlert = { viewModel.dismissBoundaryAlert() },
                                    onThemeModeChange = { viewModel.setThemeMode(it) },
                                    onPrimaryColorChange = { viewModel.setPrimaryThemeColor(it) },
                                    onToggleAzanEnabled = { viewModel.setAzanEnabled(it) },
                                    onPlayAzan = { viewModel.playAzanNabawi() },
                                    onStopAzan = { viewModel.stopAzanNabawi() },
                                    onTogglePrayer = { viewModel.togglePrayerCompleted(it) },
                                    onOpenLocationPicker = { showLocationSheet = true },
                                    onDownloadLocation = { viewModel.downloadScheduleForLocation(it) },
                                    onToggleSimpleUiMode = { viewModel.setSimpleUiMode(it) },
                                    onNavigateToTab = { currentTab = it }
                                )

                                1 -> CalendarScreen(
                                    language = language,
                                    selectedYear = selectedYear,
                                    selectedMonth = selectedMonth,
                                    monthlySchedules = monthlySchedules,
                                    holidays = holidays,
                                    selectedLocation = selectedLocation,
                                    isSimpleUiMode = isSimpleUiMode,
                                    onMonthYearChange = { y, m -> viewModel.setYearMonthDay(y, m, 1) }
                                )

                                2 -> QiblaScreen(
                                    language = language,
                                    kecamatan = selectedKecamatan
                                )

                                3 -> DuaScreen(
                                    language = language
                                )

                                4 -> TrackerScreen(
                                    language = language,
                                    kecamatan = selectedKecamatan,
                                    ihtiyatiMinutes = ihtiyatiMinutes,
                                    hijriOffset = hijriOffset,
                                    isAzanEnabled = isAzanEnabled,
                                    isAzanPlaying = isAzanPlaying,
                                    enabledAzanPrayers = enabledAzanPrayers,
                                    isSimpleUiMode = isSimpleUiMode,
                                    isOngoingNotificationEnabled = isOngoingNotificationEnabled,
                                    currentThemeMode = themeMode,
                                    currentPrimaryColor = primaryThemeColor,
                                    onThemeModeChange = { viewModel.setThemeMode(it) },
                                    onPrimaryColorChange = { viewModel.setPrimaryThemeColor(it) },
                                    onLanguageChange = { viewModel.setLanguage(it) },
                                    onKecamatanChange = { viewModel.setKecamatan(it) },
                                    onIhtiyatiChange = { viewModel.setIhtiyati(it) },
                                    onHijriOffsetChange = { viewModel.setHijriOffset(it) },
                                    onToggleAzanEnabled = { viewModel.setAzanEnabled(it) },
                                    onTogglePrayerAzan = { viewModel.togglePrayerAzan(it) },
                                    onPlayAzan = { viewModel.playAzanNabawi() },
                                    onStopAzan = { viewModel.stopAzanNabawi() },
                                    onToggleSimpleUiMode = { viewModel.setSimpleUiMode(it) },
                                    onToggleOngoingNotification = { viewModel.toggleOngoingNotification(it) }
                                )
                            }
                        }
                    }
                }

                androidx.compose.animation.AnimatedVisibility(
                    visible = showSplash,
                    enter = androidx.compose.animation.fadeIn(),
                    exit = androidx.compose.animation.fadeOut(animationSpec = androidx.compose.animation.core.tween(durationMillis = 500))
                ) {
                    com.example.ui.components.SplashScreen(
                        language = language,
                        onSplashFinished = { showSplash = false }
                    )
                }
            }
            }
            }
        }
    }
}

    private fun isAppOpsLocationAllowed(context: Context): Boolean {
        return try {
            val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as? AppOpsManager ?: return false
            val pkg = context.packageName
            val uid = Process.myUid()
            val fineMode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                appOps.unsafeCheckOpNoThrow(AppOpsManager.OPSTR_FINE_LOCATION, uid, pkg)
            } else {
                @Suppress("DEPRECATION")
                appOps.checkOpNoThrow(AppOpsManager.OPSTR_FINE_LOCATION, uid, pkg)
            }
            val coarseMode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                appOps.unsafeCheckOpNoThrow(AppOpsManager.OPSTR_COARSE_LOCATION, uid, pkg)
            } else {
                @Suppress("DEPRECATION")
                appOps.checkOpNoThrow(AppOpsManager.OPSTR_COARSE_LOCATION, uid, pkg)
            }
            fineMode == AppOpsManager.MODE_ALLOWED || coarseMode == AppOpsManager.MODE_ALLOWED
        } catch (e: Throwable) {
            false
        }
    }

    private fun initLocationUpdates(context: Context) {
        try {
            val attrContext = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                context.createAttributionContext("default")
            } else {
                context
            }

            val hasFine = ContextCompat.checkSelfPermission(attrContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            val hasCoarse = ContextCompat.checkSelfPermission(attrContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

            if (!hasFine && !hasCoarse) return

            val locationManager = attrContext.getSystemService(Context.LOCATION_SERVICE) as? LocationManager ?: return

            if (!LocationManagerCompat.isLocationEnabled(locationManager)) return
            if (!isAppOpsLocationAllowed(attrContext)) return

            // Safely cancel any existing listener before requesting new updates to avoid duplicates and leaks
            activeLocationListener?.let {
                try {
                    locationManager.removeUpdates(it)
                } catch (e: Throwable) {
                    // Ignored
                }
            }

            // Check Last Known Location safely from available providers
            val lastNetworkLoc = try {
                if (hasCoarse && locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                } else null
            } catch (e: Throwable) { null }

            val lastGpsLoc = try {
                if (hasFine && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                } else null
            } catch (e: Throwable) { null }

            val lastPassiveLoc = try {
                if (locationManager.allProviders.contains(LocationManager.PASSIVE_PROVIDER)) {
                    locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)
                } else null
            } catch (e: Throwable) { null }

            val bestLoc = listOfNotNull(lastGpsLoc, lastNetworkLoc, lastPassiveLoc)
                .maxByOrNull { it.time }

            bestLoc?.let {
                viewModel.updateLocationFromGps(it.latitude, it.longitude)
            }

            // Request periodic updates safely with 1 second / 0 meters interval for instant responsiveness
            val locationListener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    viewModel.updateLocationFromGps(location.latitude, location.longitude)
                }
                @Deprecated("Deprecated in Java")
                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
                override fun onProviderEnabled(provider: String) {}
                override fun onProviderDisabled(provider: String) {}
            }

            activeLocationListener = locationListener

            if (hasFine && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                try {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 0f, locationListener)
                } catch (e: Throwable) {
                    // Ignored gracefully if GPS provider fails in emulated env
                }
            }
            if ((hasFine || hasCoarse) && locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                try {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000L, 0f, locationListener)
                } catch (e: Throwable) {
                    // Ignored gracefully if Network provider fails in emulated env
                }
            }
        } catch (e: Throwable) {
            // Ignored gracefully
        }
    }

    private fun enableHighRefreshRate() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                @Suppress("DEPRECATION")
                val display = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) display else windowManager.defaultDisplay
                val modes = display?.supportedModes ?: emptyArray()
                val maxRefreshMode = modes.maxByOrNull { it.refreshRate }
                if (maxRefreshMode != null) {
                    val lp = window.attributes
                    lp.preferredDisplayModeId = maxRefreshMode.modeId
                    window.attributes = lp
                }
            }
        } catch (e: Throwable) {
            // Ignored if device doesn't support display mode selection
        }
    }
}
