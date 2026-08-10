package com.example.receiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.calculator.PrayerTimeCalculator
import com.example.model.IndonesiaLocation
import com.example.model.PrayerType
import java.util.Calendar
import java.util.TimeZone

object AlarmScheduler {
    fun rescheduleNextPrayer(context: Context) {
        try {
            // Read preferences directly
            val prefs = context.getSharedPreferences("salat_prefs", Context.MODE_PRIVATE)
            val defaultLocationId = prefs.getString("default_location_id", "ngawi_kota") ?: "ngawi_kota"
            val ihtiyati = 2
            val hijriOffset = 0
            
            val loc = IndonesiaLocation.ALL_INDONESIA_LOCATIONS.firstOrNull { it.id == defaultLocationId }
                ?: IndonesiaLocation.NGAWI_KOTA

            val locTz = TimeZone.getTimeZone(loc.timeZoneId)
            val now = Calendar.getInstance(locTz)
            val curYear = now.get(Calendar.YEAR)
            val curMonth = now.get(Calendar.MONTH) + 1
            val curDay = now.get(Calendar.DAY_OF_MONTH)

            val tSchedule = PrayerTimeCalculator.calculateForLocation(
                curYear, curMonth, curDay,
                loc,
                ihtiyati,
                hijriOffset
            )

            val nowMillis = System.currentTimeMillis()
            val corePrayers = tSchedule.prayerList.filter {
                it.type in listOf(PrayerType.SUBUH, PrayerType.DZUHUR, PrayerType.ASHAR, PrayerType.MAGHRIB, PrayerType.ISYA)
            }

            var nextItem = corePrayers.firstOrNull { it.timestampMillis > nowMillis }

            if (nextItem == null) {
                val tomorrowCal = Calendar.getInstance(locTz)
                tomorrowCal.add(Calendar.DAY_OF_MONTH, 1)
                val tomorrowSched = PrayerTimeCalculator.calculateForLocation(
                    tomorrowCal.get(Calendar.YEAR),
                    tomorrowCal.get(Calendar.MONTH) + 1,
                    tomorrowCal.get(Calendar.DAY_OF_MONTH),
                    loc,
                    ihtiyati,
                    hijriOffset
                )
                nextItem = tomorrowSched.prayerList.firstOrNull { it.type == PrayerType.SUBUH }
            }

            if (nextItem != null) {
                scheduleAlarm(context, nextItem.timestampMillis, nextItem.type.idName, nextItem.type.arName, nextItem.timeFormatted)
            }
        } catch (e: Throwable) {
            Log.e("AlarmScheduler", "Failed to reschedule prayer alarm safely", e)
        }
    }

    private fun scheduleAlarm(context: Context, timeInMillis: Long, nameId: String, nameAr: String, timeStr: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val intent = Intent(context, PrayerAlarmReceiver::class.java).apply {
            action = "com.example.ACTION_PRAYER_ALARM"
            putExtra("EXTRA_PRAYER_NAME_ID", nameId)
            putExtra("EXTRA_PRAYER_NAME_AR", nameAr)
            putExtra("EXTRA_PRAYER_TIME", timeStr)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        Log.d("AlarmScheduler", "Scheduling alarm for $nameId at $timeStr")
        
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
                } else {
                    alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
                }
            } else {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            try {
                alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
            } catch (e2: Throwable) {
                e2.printStackTrace()
            }
        }
    }
}
