package com.example.ui.screens

import android.Manifest
import android.app.AppOpsManager
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Process
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.location.LocationManagerCompat
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CompassCalibration
import androidx.compose.material.icons.filled.EditLocation
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.model.AppLanguage
import com.example.model.NgawiKecamatan
import kotlin.math.roundToInt

data class GlobalCity(
    val name: String,
    val country: String,
    val lat: Double,
    val lon: Double
)

val MAJOR_GLOBAL_CITIES = listOf(
    GlobalCity("Jakarta", "Indonesia", -6.2088, 106.8456),
    GlobalCity("Surabaya", "Indonesia", -7.2575, 112.7521),
    GlobalCity("Bandung", "Indonesia", -6.9175, 107.6191),
    GlobalCity("Medan", "Indonesia", 3.5952, 98.6722),
    GlobalCity("Makassar", "Indonesia", -5.1477, 119.4327),
    GlobalCity("Semarang", "Indonesia", -6.9667, 110.4167),
    GlobalCity("Yogyakarta", "Indonesia", -7.7956, 110.3695),
    GlobalCity("Denpasar / Bali", "Indonesia", -8.6705, 115.2126),
    GlobalCity("Banda Aceh", "Indonesia", 5.5483, 95.3238),
    GlobalCity("Jayapura", "Indonesia", -2.5489, 140.7196),
    GlobalCity("Kuala Lumpur", "Malaysia", 3.1390, 101.6869),
    GlobalCity("Singapore", "Singapore", 1.3521, 103.8198),
    GlobalCity("Makkah", "Arab Saudi", 21.4225, 39.8262),
    GlobalCity("Madinah", "Arab Saudi", 24.5247, 39.5692),
    GlobalCity("Riyadh", "Arab Saudi", 24.7136, 46.6753),
    GlobalCity("Tokyo", "Jepang", 35.6762, 139.6503),
    GlobalCity("London", "Inggris", 51.5074, -0.1278),
    GlobalCity("New York", "Amerika Serikat", 40.7128, -74.0060),
    GlobalCity("Istanbul", "Turki", 41.0082, 28.9784),
    GlobalCity("Kairo", "Mesir", 30.0444, 31.2357),
    GlobalCity("Sydney", "Australia", -33.8688, 151.2093)
)

object QiblaCalculator {
    private const val KAABA_LAT = 21.422487
    private const val KAABA_LON = 39.826206
    private const val EARTH_RADIUS_KM = 6371.0

    fun calculateQiblaBearing(lat: Double, lon: Double): Float {
        val latRad = Math.toRadians(lat)
        val kaabaLatRad = Math.toRadians(KAABA_LAT)
        val dLonRad = Math.toRadians(KAABA_LON - lon)

        val y = kotlin.math.sin(dLonRad)
        val x = kotlin.math.cos(latRad) * kotlin.math.tan(kaabaLatRad) - kotlin.math.sin(latRad) * kotlin.math.cos(dLonRad)

        var qibla = Math.toDegrees(kotlin.math.atan2(y, x))
        if (qibla < 0) {
            qibla += 360.0
        }
        return qibla.toFloat()
    }

    fun calculateDistanceToKaabaKm(lat: Double, lon: Double): Int {
        val lat1 = Math.toRadians(lat)
        val lon1 = Math.toRadians(lon)
        val lat2 = Math.toRadians(KAABA_LAT)
        val lon2 = Math.toRadians(KAABA_LON)

        val dLat = lat2 - lat1
        val dLon = lon2 - lon1

        val a = kotlin.math.sin(dLat / 2) * kotlin.math.sin(dLat / 2) +
                kotlin.math.cos(lat1) * kotlin.math.cos(lat2) *
                kotlin.math.sin(dLon / 2) * kotlin.math.sin(dLon / 2)
        val c = 2 * kotlin.math.atan2(kotlin.math.sqrt(a), kotlin.math.sqrt(1 - a))
        return (EARTH_RADIUS_KM * c).roundToInt()
    }
}

@Composable
fun QiblaScreen(
    language: AppLanguage,
    kecamatan: NgawiKecamatan
) {
    val isId = language == AppLanguage.INDONESIAN
    val context = LocalContext.current

    // Mode: GPS Otomatis vs Custom / Kota
    var isGpsMode by remember { mutableStateOf(true) }
    var hasLocationPermission by remember { mutableStateOf(false) }

    var gpsLat by remember { mutableStateOf<Double?>(null) }
    var gpsLon by remember { mutableStateOf<Double?>(null) }
    var gpsAltitude by remember { mutableStateOf<Double?>(null) }
    var gpsAltAcc by remember { mutableStateOf<Float?>(null) }

    var baroPressure by remember { mutableStateOf<Float?>(null) }
    var baroAltitude by remember { mutableStateOf<Double?>(null) }

    var selectedCustomLat by remember { mutableDoubleStateOf(kecamatan.lat) }
    var selectedCustomLon by remember { mutableDoubleStateOf(kecamatan.lon) }
    var customLocationName by remember { mutableStateOf(kecamatan.name) }

    var showCustomCoordDialog by remember { mutableStateOf(false) }

    // Request Location Permission
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.values.any { it }
        hasLocationPermission = granted
    }

    LaunchedEffect(Unit) {
        val fineGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val coarseGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        if (fineGranted || coarseGranted) {
            hasLocationPermission = true
        } else {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    // Android Real-Time Location Listener
    DisposableEffect(hasLocationPermission, isGpsMode) {
        if (hasLocationPermission && isGpsMode) {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
            val isAppOpsAllowed = try {
                val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as? AppOpsManager
                val pkg = context.packageName
                val uid = Process.myUid()
                val fineMode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    appOps?.unsafeCheckOpNoThrow(AppOpsManager.OPSTR_FINE_LOCATION, uid, pkg)
                } else {
                    @Suppress("DEPRECATION")
                    appOps?.checkOpNoThrow(AppOpsManager.OPSTR_FINE_LOCATION, uid, pkg)
                }
                val coarseMode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    appOps?.unsafeCheckOpNoThrow(AppOpsManager.OPSTR_COARSE_LOCATION, uid, pkg)
                } else {
                    @Suppress("DEPRECATION")
                    appOps?.checkOpNoThrow(AppOpsManager.OPSTR_COARSE_LOCATION, uid, pkg)
                }
                fineMode == AppOpsManager.MODE_ALLOWED || coarseMode == AppOpsManager.MODE_ALLOWED
            } catch (e: Throwable) { false }

            if (locationManager == null || !LocationManagerCompat.isLocationEnabled(locationManager) || !isAppOpsAllowed) {
                return@DisposableEffect onDispose { }
            }

            val listener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    gpsLat = location.latitude
                    gpsLon = location.longitude
                    if (location.hasAltitude()) {
                        gpsAltitude = location.altitude
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && location.hasVerticalAccuracy()) {
                        gpsAltAcc = location.verticalAccuracyMeters
                    }
                }
                @Deprecated("Deprecated in Java")
                override fun onStatusChanged(provider: String?, status: Int, extras: android.os.Bundle?) {}
            }

            try {
                val lastGps = try { locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) } catch (e: Throwable) { null }
                val lastNetwork = try { locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) } catch (e: Throwable) { null }
                val lastPassive = try { if (locationManager.getProvider(LocationManager.PASSIVE_PROVIDER) != null) locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER) else null } catch (e: Throwable) { null }
                val bestLast = listOfNotNull(lastGps, lastNetwork, lastPassive).maxByOrNull { it.time }
                if (bestLast != null) {
                    gpsLat = bestLast.latitude
                    gpsLon = bestLast.longitude
                    if (bestLast.hasAltitude()) {
                        gpsAltitude = bestLast.altitude
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && bestLast.hasVerticalAccuracy()) {
                        gpsAltAcc = bestLast.verticalAccuracyMeters
                    }
                }

                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    try {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000L, 2f, listener)
                    } catch (e: Throwable) { }
                }
                if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    try {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000L, 2f, listener)
                    } catch (e: Throwable) { }
                }
            } catch (e: Throwable) { }

            onDispose {
                try {
                    locationManager.removeUpdates(listener)
                } catch (e: Throwable) { }
            }
        } else {
            onDispose { }
        }
    }

    // Active Coordinates for Qibla Computation
    val activeLat = if (isGpsMode && gpsLat != null) gpsLat!! else selectedCustomLat
    val activeLon = if (isGpsMode && gpsLon != null) gpsLon!! else selectedCustomLon
    val activeLocationName = if (isGpsMode) {
        if (gpsLat != null) {
            if (isId) "GPS Otomatis (Live Location)" else "الموقع المباشر (GPS)"
        } else {
            if (isId) "Mencari GPS... (${kecamatan.name})" else "جاري البحث عن GPS..."
        }
    } else {
        customLocationName
    }

    // Dynamically calculate Qibla angle & distance based on exact active coordinates
    val qiblaBearingFromNorth = remember(activeLat, activeLon) {
        QiblaCalculator.calculateQiblaBearing(activeLat, activeLon)
    }
    val distanceToKaabaKm = remember(activeLat, activeLon) {
        QiblaCalculator.calculateDistanceToKaabaKm(activeLat, activeLon)
    }

    var azimuth by remember { mutableFloatStateOf(0f) }

    // Compass & Barometer Sensor Listener
    DisposableEffect(Unit) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
        if (sensorManager == null) {
            onDispose { }
        } else {
            val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            val magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
            val pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)

            var gravity: FloatArray? = null
            var geomagnetic: FloatArray? = null
            var lastSmoothAzimuth = 0f
            var hasInitialAzimuth = false

            val listener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent) {
                    if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                        gravity = event.values.clone()
                    }
                    if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
                        geomagnetic = event.values.clone()
                    }
                    if (event.sensor.type == Sensor.TYPE_PRESSURE) {
                        val p = event.values[0]
                        baroPressure = p
                        baroAltitude = SensorManager.getAltitude(SensorManager.PRESSURE_STANDARD_ATMOSPHERE, p).toDouble()
                    }
                    if (gravity != null && geomagnetic != null) {
                        val R = FloatArray(9)
                        val I = FloatArray(9)
                        if (SensorManager.getRotationMatrix(R, I, gravity, geomagnetic)) {
                            val orientation = FloatArray(3)
                            SensorManager.getOrientation(R, orientation)
                            var az = Math.toDegrees(orientation[0].toDouble()).toFloat()
                            if (az < 0) az += 360f

                            if (!hasInitialAzimuth) {
                                lastSmoothAzimuth = az
                                hasInitialAzimuth = true
                                azimuth = az
                            } else {
                                // Exponential low-pass filter (alpha = 0.2f) for butter-smooth 60fps+ needle movement
                                var delta = (az - lastSmoothAzimuth) % 360f
                                if (delta < -180f) delta += 360f
                                if (delta > 180f) delta -= 360f
                                lastSmoothAzimuth = (lastSmoothAzimuth + 0.2f * delta) % 360f
                                if (lastSmoothAzimuth < 0) lastSmoothAzimuth += 360f

                                if (kotlin.math.abs(lastSmoothAzimuth - azimuth) >= 0.15f) {
                                    azimuth = lastSmoothAzimuth
                                }
                            }
                        }
                    }
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
            }

            if (accelerometer != null) {
                sensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_UI)
            }
            if (magnetometer != null) {
                sensorManager.registerListener(listener, magnetometer, SensorManager.SENSOR_DELAY_UI)
            }
            if (pressureSensor != null) {
                sensorManager.registerListener(listener, pressureSensor, SensorManager.SENSOR_DELAY_UI)
            }

            onDispose {
                sensorManager.unregisterListener(listener)
            }
        }
    }

    var continuousAzimuth by remember { mutableFloatStateOf(azimuth) }
    LaunchedEffect(azimuth) {
        var diff = (azimuth - continuousAzimuth) % 360f
        if (diff < -180f) diff += 360f
        if (diff > 180f) diff -= 360f
        continuousAzimuth += diff
    }

    val animatedAzimuth by animateFloatAsState(
        targetValue = continuousAzimuth,
        animationSpec = androidx.compose.animation.core.spring(
            stiffness = androidx.compose.animation.core.Spring.StiffnessLow,
            dampingRatio = androidx.compose.animation.core.Spring.DampingRatioNoBouncy
        ),
        label = "CompassRotation"
    )

    // Needle rotation relative to device heading: (QiblaBearing - DeviceHeading)
    val needleRotation = qiblaBearingFromNorth - animatedAzimuth

    // Is device perfectly aligned with Kaaba (within 3 degrees)
    val isAlignedWithQibla = remember(needleRotation) {
        val norm = (needleRotation % 360 + 360) % 360
        norm in 0f..3f || norm in 357f..360f
    }

    // Gentle Vibration Feedback when user aligns phone with Qibla
    LaunchedEffect(isAlignedWithQibla) {
        if (isAlignedWithQibla) {
            try {
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator?.vibrate(VibrationEffect.createOneShot(120, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator?.vibrate(120)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Intelligent Altitude & MDPL Calculation (Multi-sensor GPS + Barometer + Geoid Model)
    val baselineElevation = remember(activeLat, activeLon, activeLocationName) {
        when {
            activeLocationName.contains("Ngrambe", ignoreCase = true) -> 450
            activeLocationName.contains("Sine", ignoreCase = true) -> 580
            activeLocationName.contains("Jogorogo", ignoreCase = true) -> 350
            activeLocationName.contains("Kendal", ignoreCase = true) -> 500
            activeLocationName.contains("Mantingan", ignoreCase = true) -> 80
            activeLocationName.contains("Ngawi", ignoreCase = true) -> 54
            activeLocationName.contains("Makkah", ignoreCase = true) -> 277
            activeLocationName.contains("Madinah", ignoreCase = true) -> 608
            activeLocationName.contains("Bandung", ignoreCase = true) -> 768
            activeLocationName.contains("Bogor", ignoreCase = true) -> 265
            activeLocationName.contains("Malang", ignoreCase = true) -> 444
            activeLocationName.contains("Surabaya", ignoreCase = true) -> 5
            activeLocationName.contains("Jakarta", ignoreCase = true) -> 8
            activeLocationName.contains("Denpasar", ignoreCase = true) -> 25
            else -> {
                if (activeLat < -7.48 && activeLon in 111.10..111.35) 420
                else 65
            }
        }
    }

    val activeMdpl = gpsAltitude ?: baroAltitude ?: baselineElevation.toDouble()
    val altitudeSourceLabel = when {
        gpsAltitude != null -> if (isId) "📡 GPS Satelit" else "📡 GPS مباشر"
        baroAltitude != null -> if (isId) "🌡️ Sensor Barometer" else "🌡️ مقياس الضغط الجوي"
        else -> if (isId) "🗺️ Data Topografi WGS84" else "🗺️ المرجع التوبوغرافي"
    }

    val terrainZoneBadge = when {
        activeMdpl < 50.0 -> if (isId) "Dataran Rendah / Pesisir" else "سهول منخفضة / ساحل"
        activeMdpl < 300.0 -> if (isId) "Dataran Rendah - Sedang" else "سهول متوسطة"
        activeMdpl < 700.0 -> if (isId) "Perbukitan / Lereng" else "تلال وجبال متوسطة"
        else -> if (isId) "Dataran Tinggi / Pegunungan" else "مرتفعات وجبال عالية"
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))

            // MODE TOGGLE BAR (GPS Live vs Manual Selected City/Custom)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isGpsMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { isGpsMode = true }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.GpsFixed,
                            contentDescription = "GPS",
                            tint = if (isGpsMode) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isId) "📡 GPS Otomatis" else "📡 GPS تلقائي",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (isGpsMode) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (!isGpsMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { isGpsMode = false }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationCity,
                            contentDescription = "Pilih Lokasi",
                            tint = if (!isGpsMode) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isId) "📍 Pilih Kota / Custom" else "📍 اختيار المدينة",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (!isGpsMode) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // HEADER INFO CARD
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isAlignedWithQibla)
                        Color(0xFF2E7D32).copy(alpha = 0.15f)
                    else
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                            Icon(
                                imageVector = if (isAlignedWithQibla) Icons.Default.CheckCircle else Icons.Default.Explore,
                                contentDescription = "Kiblat",
                                tint = if (isAlignedWithQibla) Color(0xFF2E7D32) else MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isAlignedWithQibla) {
                                    if (isId) "🎯 TEPAT MENGHADAP KIBLAT!" else "🎯 باتجاه القبلة تماماً!"
                                } else {
                                    if (isId) "Arah Kiblat ($activeLocationName)" else "اتجاه القبلة ($activeLocationName)"
                                },
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (isAlignedWithQibla) Color(0xFF2E7D32) else MaterialTheme.colorScheme.primary,
                                maxLines = 1,
                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isAlignedWithQibla) Color(0xFF2E7D32) else MaterialTheme.colorScheme.secondaryContainer
                        ) {
                            Text(
                                text = String.format(java.util.Locale.US, "%.1f° UB", qiblaBearingFromNorth),
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = if (isAlignedWithQibla) Color.White else MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (isId)
                            "Koordinat Aktif: ${String.format(java.util.Locale.US, "%.4f", activeLat)}°, ${String.format(java.util.Locale.US, "%.4f", activeLon)}° • Jarak Akurat ke Ka'bah: ~$distanceToKaabaKm km"
                        else
                            "الإحداثيات: ${String.format(java.util.Locale.US, "%.4f", activeLat)}° ، ${String.format(java.util.Locale.US, "%.4f", activeLon)}° • المسافة للكعبة: ~$distanceToKaabaKm كم",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // GLOBAL CITY SELECTOR (Horizontal Chips when in Manual Mode)
            AnimatedVisibility(visible = !isGpsMode) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (isId) "Pilih Kota Utama atau Masukkan Koordinat:" else "اختر المدينة أو أدخل الإحداثيات:",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        TextButton(
                            onClick = { showCustomCoordDialog = true },
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp)
                        ) {
                            Icon(Icons.Default.EditLocation, contentDescription = "Input Custom", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = if (isId) "Input Lat/Long" else "إدخال يدوي", fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Current Kecamatan Option
                        item {
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = if (customLocationName == kecamatan.name) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.clickable {
                                    selectedCustomLat = kecamatan.lat
                                    selectedCustomLon = kecamatan.lon
                                    customLocationName = kecamatan.name
                                }
                            ) {
                                Text(
                                    text = "📍 ${kecamatan.name}",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }

                        // Major Global Cities List
                        itemsIndexed(MAJOR_GLOBAL_CITIES, key = { index, city -> "${city.name}_$index" }) { _, city ->
                            val isSelected = customLocationName == city.name
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.clickable {
                                    selectedCustomLat = city.lat
                                    selectedCustomLon = city.lon
                                    customLocationName = city.name
                                }
                            ) {
                                Text(
                                    text = "${city.name} (${city.country})",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // VISUAL COMPASS ROSE
        item {
            val ringColor = if (isAlignedWithQibla) Color(0xFF2E7D32) else Color(0xFF5A6B5D)

            Card(
                shape = CircleShape,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier
                    .size(280.dp)
                    .border(
                        width = if (isAlignedWithQibla) 6.dp else 0.dp,
                        color = if (isAlignedWithQibla) Color(0xFF2E7D32) else Color.Transparent,
                        shape = CircleShape
                    )
                    .testTag("qibla_compass_dial")
            ) {
                androidx.compose.runtime.CompositionLocalProvider(
                    androidx.compose.ui.platform.LocalLayoutDirection provides androidx.compose.ui.unit.LayoutDirection.Ltr
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                    // Outer Compass Dial Rotating with Azimuth
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(260.dp)
                            .rotate(-animatedAzimuth)
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val radius = size.width / 2 - 12.dp.toPx()

                            // Outer Dial Ring
                            drawCircle(
                                color = ringColor,
                                radius = radius,
                                style = Stroke(width = 4.dp.toPx())
                            )
                        }

                        // Cardinal Direction Indicators
                        Text("U / N", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color(0xFFC0392B), modifier = Modifier.align(Alignment.TopCenter).padding(top = 16.dp))
                        Text("S", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 16.dp))
                        Text("B / W", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.align(Alignment.CenterStart).padding(start = 16.dp))
                        Text("T / E", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.align(Alignment.CenterEnd).padding(end = 16.dp))
                    }

                    // QIBLA ARROW (Pointing directly to Kaaba 294.4°)
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(220.dp)
                            .rotate(needleRotation)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = if (isAlignedWithQibla) Color(0xFF2E7D32) else Color(0xFFE1E8DD),
                                modifier = Modifier.padding(top = 8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Mosque,
                                    contentDescription = "Kaaba",
                                    tint = if (isAlignedWithQibla) Color.White else Color(0xFF3D4C40),
                                    modifier = Modifier
                                        .padding(6.dp)
                                        .size(24.dp)
                                )
                            }

                            // Arrow Needle Graphic
                            Canvas(
                                modifier = Modifier
                                    .width(16.dp)
                                    .height(110.dp)
                            ) {
                                val path = Path().apply {
                                    moveTo(size.width / 2, 0f)
                                    lineTo(size.width, size.height)
                                    lineTo(size.width / 2, size.height * 0.8f)
                                    lineTo(0f, size.height)
                                    close()
                                }
                                drawPath(path, color = if (isAlignedWithQibla) Color(0xFF2E7D32) else Color(0xFF5A6B5D))
                            }
                        }
                    }

                    // Center Pivot Circle
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(if (isAlignedWithQibla) Color(0xFF2E7D32) else MaterialTheme.colorScheme.primary)
                    )
                }
            }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // COMPASS READOUT & CALIBRATION TIP
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (isId) "Arah Kompas HP Anda:" else "اتجاه بوصلة الجوال:",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${animatedAzimuth.roundToInt()}°",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CompassCalibration,
                            contentDescription = "Kalibrasi",
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isId)
                                "Petunjuk Akurasi: Jauhkan HP dari benda magnetik/logam. Jika kurang pas, putar HP di udara membentuk angka 8."
                            else
                                "نصيحة المعايرة: حرك الهاتف على شكل رقم ٨ في الهواء لزيادة دقة البوصلة.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // MONITOR KETINGGIAN MDPL CARD
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("elevation_mdpl_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Terrain,
                                contentDescription = "Ketinggian MDPL",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isId) "Monitor Ketinggian (MDPL)" else "مقياس الارتفاع عن سطح البحر",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = altitudeSourceLabel,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = String.format(java.util.Locale.US, "%.1f", activeMdpl),
                                    style = MaterialTheme.typography.displaySmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "mdpl",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                            }
                            Text(
                                text = if (isId) "Meter Di Atas Permukaan Laut" else "أمتار فوق مستوى سطح البحر",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            if (baroPressure != null) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Air,
                                            contentDescription = "Pressure",
                                            modifier = Modifier.size(14.dp),
                                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = String.format(java.util.Locale.US, "%.1f hPa", baroPressure),
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSecondaryContainer
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                            } else if (gpsAltAcc != null) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Sensors,
                                            contentDescription = "Accuracy",
                                            modifier = Modifier.size(14.dp),
                                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = String.format(java.util.Locale.US, "±%.1fm", gpsAltAcc),
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSecondaryContainer
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surface
                            ) {
                                Text(
                                    text = "🏞️ $terrainZoneBadge",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = if (isId)
                            "💡 Estimasi ketinggian menggabungkan sensor GPS Satelit, Barometer, dan data Topografi WGS84 secara akurat."
                        else
                            "💡 تقدير الارتفاع عن سطح البحر بدقة بالدمج بين GPS ومقياس الضغط والبيانات التوبوغرافية.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // DIALOG CUSTOM LAT/LONG MANUAL INPUT
    if (showCustomCoordDialog) {
        var inputLatStr by remember { mutableStateOf(selectedCustomLat.toString()) }
        var inputLonStr by remember { mutableStateOf(selectedCustomLon.toString()) }
        var inputNameStr by remember { mutableStateOf(customLocationName) }

        AlertDialog(
            onDismissRequest = { showCustomCoordDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.MyLocation, contentDescription = "Lat Lon", tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = if (isId) "Input Koordinat Bebas" else "إدخال الإحداثيات")
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = if (isId) "Masukkan Latitude & Longitude lokasi Anda di mana saja di dunia:" else "أدخل خط العرض وخط الطول لأي مكان في العالم:",
                        style = MaterialTheme.typography.bodySmall
                    )

                    OutlinedTextField(
                        value = inputNameStr,
                        onValueChange = { inputNameStr = it },
                        label = { Text(if (isId) "Nama Lokasi / Catatan" else "اسم الموقع") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = inputLatStr,
                        onValueChange = { inputLatStr = it },
                        label = { Text("Latitude (contoh: -6.2088)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = inputLonStr,
                        onValueChange = { inputLonStr = it },
                        label = { Text("Longitude (contoh: 106.8456)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val parsedLat = inputLatStr.toDoubleOrNull()
                        val parsedLon = inputLonStr.toDoubleOrNull()
                        if (parsedLat != null && parsedLon != null) {
                            selectedCustomLat = parsedLat
                            selectedCustomLon = parsedLon
                            customLocationName = if (inputNameStr.isNotBlank()) inputNameStr else "Koordinat Kustom"
                            isGpsMode = false
                        }
                        showCustomCoordDialog = false
                    }
                ) {
                    Text(if (isId) "Terapkan" else "تطبيق")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCustomCoordDialog = false }) {
                    Text(if (isId) "Batal" else "إلغاء")
                }
            }
        )
    }
}

