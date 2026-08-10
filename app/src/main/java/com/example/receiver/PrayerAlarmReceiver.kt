package com.example.receiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.model.PrayerTimeItem
import java.util.Calendar

class PrayerAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        Log.d("PrayerAlarmReceiver", "Received action: $action")
        
        if (action == "com.example.ACTION_PRAYER_ALARM") {
            val prayerNameId = intent.getStringExtra("EXTRA_PRAYER_NAME_ID") ?: "Waktu Salat"
            val prayerNameAr = intent.getStringExtra("EXTRA_PRAYER_NAME_AR") ?: "وقت الصلاة"
            val timeStr = intent.getStringExtra("EXTRA_PRAYER_TIME") ?: ""
            
            // Show Notification
            PrayerNotificationHelper.showPrayerNotification(context, prayerNameId, prayerNameAr, timeStr)
            
            // Schedule next prayer (trigger an update)
            val updateIntent = Intent(context, PrayerAlarmReceiver::class.java).apply {
                this.action = "com.example.ACTION_UPDATE_ALARMS"
            }
            context.sendBroadcast(updateIntent)
        } else if (action == Intent.ACTION_BOOT_COMPLETED || 
                   action == Intent.ACTION_LOCKED_BOOT_COMPLETED ||
                   action == Intent.ACTION_MY_PACKAGE_REPLACED ||
                   action == Intent.ACTION_TIME_CHANGED ||
                   action == Intent.ACTION_TIMEZONE_CHANGED ||
                   action == "com.example.ACTION_UPDATE_ALARMS") {
            // Re-calculate and schedule from a background thread or using WorkManager
            // For simplicity here, we can rely on the ViewModel to reschedule when app opens, 
            // but we really should fetch the stored prayer time and reschedule if the app is in the background.
            // Since this is a simple local app, we'll delegate scheduling to a helper method that computes the next prayer.
            AlarmScheduler.rescheduleNextPrayer(context)
        }
    }
}
