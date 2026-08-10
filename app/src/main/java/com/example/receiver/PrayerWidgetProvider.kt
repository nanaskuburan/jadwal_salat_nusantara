package com.example.receiver

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.RemoteViews
import android.widget.Toast
import com.example.MainActivity
import com.example.R
import com.example.calculator.PrayerTimeCalculator
import com.example.model.IndonesiaLocation
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class PrayerWidgetProvider : AppWidgetProvider() {

    companion object {
        const val ACTION_REFRESH_PRAYER_WIDGET = "com.example.action.REFRESH_PRAYER_WIDGET"

        fun updateAllWidgets(context: Context) {
            try {
                val appWidgetManager = AppWidgetManager.getInstance(context)
                val componentName = ComponentName(context, PrayerWidgetProvider::class.java)
                val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)
                if (appWidgetIds != null && appWidgetIds.isNotEmpty()) {
                    for (appWidgetId in appWidgetIds) {
                        updateAppWidget(context, appWidgetManager, appWidgetId, isManualRefresh = false)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        private fun isNetworkAvailable(context: Context): Boolean {
            return try {
                val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
                    ?: return false
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val network = connectivityManager.activeNetwork ?: return false
                    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
                    capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                } else {
                    @Suppress("DEPRECATION")
                    val networkInfo = connectivityManager.activeNetworkInfo
                    @Suppress("DEPRECATION")
                    networkInfo != null && networkInfo.isConnected
                }
            } catch (e: Exception) {
                false
            }
        }

        fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int,
            isManualRefresh: Boolean = false
        ) {
            try {
                val views = RemoteViews(context.packageName, R.layout.widget_prayer_times)

                // Intent when tapping the main widget body -> Open MainActivity
                val appIntent = Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
                val pendingAppIntent = PendingIntent.getActivity(
                    context,
                    0,
                    appIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                views.setOnClickPendingIntent(R.id.widget_root_layout, pendingAppIntent)

                // Intent when tapping the Refresh button -> Trigger refresh broadcast
                val refreshIntent = Intent(context, PrayerWidgetProvider::class.java).apply {
                    action = ACTION_REFRESH_PRAYER_WIDGET
                    putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                }
                val pendingRefreshIntent = PendingIntent.getBroadcast(
                    context,
                    appWidgetId,
                    refreshIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                views.setOnClickPendingIntent(R.id.btn_refresh_widget, pendingRefreshIntent)

                // Check network state if manual refresh was requested
                val online = isNetworkAvailable(context)

                // Load saved location from preferences
                val prefs = context.getSharedPreferences("salat_prefs", Context.MODE_PRIVATE)
                val locationId = prefs.getString("default_location_id", "ngawi_kota") ?: "ngawi_kota"
                val ihtiyati = prefs.getInt("ihtiyati_minutes", 2)
                val hijriOffset = prefs.getInt("hijri_offset", 0)

                val location = IndonesiaLocation.ALL_INDONESIA_LOCATIONS.firstOrNull { it.id == locationId }
                    ?: IndonesiaLocation.NGAWI_KOTA

                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH) + 1
                val day = calendar.get(Calendar.DAY_OF_MONTH)

                val schedule = PrayerTimeCalculator.calculateForLocation(
                    year = year,
                    month = month,
                    day = day,
                    location = location,
                    ihtiyatiMinutes = ihtiyati,
                    hijriOffset = hijriOffset
                )

                // Format date string
                val dateFormat = SimpleDateFormat("EEEE, d MMM yyyy", Locale("id", "ID"))
                val dateStr = dateFormat.format(calendar.time)

                // Format timestamp string
                val timeFormat = SimpleDateFormat("HH:mm", Locale("id", "ID"))
                val lastSyncTime = timeFormat.format(calendar.time)

                val statusText = if (online) {
                    "Online Sync • $lastSyncTime ${location.timeZoneName}"
                } else {
                    "Offline • Disimpan $lastSyncTime ${location.timeZoneName}"
                }

                // --- ADAPTIVE / RESPONSIVE WIDGET SCALING ---
                val options = appWidgetManager.getAppWidgetOptions(appWidgetId)
                val minWidth = options?.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH) ?: 0
                val minHeight = options?.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT) ?: 0

                // Default responsive properties
                var paddingDp = 12
                var locationTextSizeSp = 13f
                var dateTextSizeSp = 11f
                var labelTextSizeSp = 11f
                var timeTextSizeSp = 13f
                var isDividerVisible = true
                var isStatusVisible = true

                if (minHeight in 1..89) {
                    // Very compact vertically (e.g., 1-row widget)
                    paddingDp = 6
                    locationTextSizeSp = 11f
                    dateTextSizeSp = 9f
                    labelTextSizeSp = 9f
                    timeTextSizeSp = 10f
                    isDividerVisible = false
                    isStatusVisible = false
                } else if (minHeight >= 120) {
                    // Generous vertical space
                    paddingDp = 16
                    locationTextSizeSp = 15f
                    dateTextSizeSp = 12f
                    labelTextSizeSp = 12f
                    timeTextSizeSp = 15f
                }

                // Width-based adjustments to prevent text wrapping on narrow screens
                if (minWidth in 1..219) {
                    locationTextSizeSp = Math.min(locationTextSizeSp, 11f)
                    dateTextSizeSp = Math.min(dateTextSizeSp, 9f)
                    labelTextSizeSp = Math.min(labelTextSizeSp, 9f)
                    timeTextSizeSp = Math.min(timeTextSizeSp, 10f)
                } else if (minWidth >= 280 && minHeight >= 110) {
                    locationTextSizeSp = Math.max(locationTextSizeSp, 15f)
                    dateTextSizeSp = Math.max(dateTextSizeSp, 12f)
                    labelTextSizeSp = Math.max(labelTextSizeSp, 12f)
                    timeTextSizeSp = Math.max(timeTextSizeSp, 15f)
                }

                // Apply padding
                val density = context.resources.displayMetrics.density
                val paddingPx = (paddingDp * density).toInt()
                views.setViewPadding(R.id.widget_root_layout, paddingPx, paddingPx, paddingPx, paddingPx)

                // Apply text sizes
                views.setTextViewTextSize(R.id.tv_widget_location, TypedValue.COMPLEX_UNIT_SP, locationTextSizeSp)
                views.setTextViewTextSize(R.id.tv_widget_date, TypedValue.COMPLEX_UNIT_SP, dateTextSizeSp)
                
                views.setTextViewTextSize(R.id.tv_widget_subuh_label, TypedValue.COMPLEX_UNIT_SP, labelTextSizeSp)
                views.setTextViewTextSize(R.id.tv_widget_subuh, TypedValue.COMPLEX_UNIT_SP, timeTextSizeSp)

                views.setTextViewTextSize(R.id.tv_widget_dzuhur_label, TypedValue.COMPLEX_UNIT_SP, labelTextSizeSp)
                views.setTextViewTextSize(R.id.tv_widget_dzuhur, TypedValue.COMPLEX_UNIT_SP, timeTextSizeSp)

                views.setTextViewTextSize(R.id.tv_widget_ashar_label, TypedValue.COMPLEX_UNIT_SP, labelTextSizeSp)
                views.setTextViewTextSize(R.id.tv_widget_ashar, TypedValue.COMPLEX_UNIT_SP, timeTextSizeSp)

                views.setTextViewTextSize(R.id.tv_widget_maghrib_label, TypedValue.COMPLEX_UNIT_SP, labelTextSizeSp)
                views.setTextViewTextSize(R.id.tv_widget_maghrib, TypedValue.COMPLEX_UNIT_SP, timeTextSizeSp)

                views.setTextViewTextSize(R.id.tv_widget_isya_label, TypedValue.COMPLEX_UNIT_SP, labelTextSizeSp)
                views.setTextViewTextSize(R.id.tv_widget_isya, TypedValue.COMPLEX_UNIT_SP, timeTextSizeSp)

                views.setTextViewTextSize(R.id.tv_widget_status, TypedValue.COMPLEX_UNIT_SP, 9f)

                // Apply visibilities
                views.setViewVisibility(R.id.widget_divider, if (isDividerVisible) View.VISIBLE else View.GONE)
                views.setViewVisibility(R.id.tv_widget_status, if (isStatusVisible) View.VISIBLE else View.GONE)

                // Populate RemoteViews text content
                views.setTextViewText(R.id.tv_widget_location, "📍 ${location.name}")
                views.setTextViewText(R.id.tv_widget_date, dateStr)
                views.setTextViewText(R.id.tv_widget_subuh, schedule.subuh)
                views.setTextViewText(R.id.tv_widget_dzuhur, schedule.dzuhur)
                views.setTextViewText(R.id.tv_widget_ashar, schedule.ashar)
                views.setTextViewText(R.id.tv_widget_maghrib, schedule.maghrib)
                views.setTextViewText(R.id.tv_widget_isya, schedule.isya)
                views.setTextViewText(R.id.tv_widget_status, statusText)

                appWidgetManager.updateAppWidget(appWidgetId, views)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, isManualRefresh = false)
        }
    }

    override fun onAppWidgetOptionsChanged(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        newOptions: Bundle?
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
        updateAppWidget(context, appWidgetManager, appWidgetId, isManualRefresh = false)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == ACTION_REFRESH_PRAYER_WIDGET) {
            val appWidgetId = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
            )
            if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                val appWidgetManager = AppWidgetManager.getInstance(context)
                updateAppWidget(context, appWidgetManager, appWidgetId, isManualRefresh = true)
            } else {
                updateAllWidgets(context)
            }
        }
    }
}
