package com.example.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.MainActivity

object PrayerNotificationHelper {
    private const val CHANNEL_ID = "prayer_alarms_channel_v2"
    private const val ONGOING_CHANNEL_ID = "prayer_ongoing_channel"
    private const val NOTIFICATION_ID = 1001
    private const val ONGOING_NOTIFICATION_ID = 1002

    fun createNotificationChannel(context: Context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
                
                // Alarm channel
                val name = "Waktu Salat (Pengingat)"
                val descriptionText = "Notifikasi saat masuk waktu salat"
                val importance = NotificationManager.IMPORTANCE_HIGH
                val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                    description = descriptionText
                    enableLights(true)
                    enableVibration(true)
                    val alarmSound = android.net.Uri.parse("android.resource://" + context.packageName + "/" + com.example.R.raw.azan_nabawi)
                    setSound(alarmSound, AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build())
                }
                notificationManager?.createNotificationChannel(channel)

                // Ongoing channel (silent status bar display)
                val ongoingName = "Jadwal Salat Menetap (Status Bar)"
                val ongoingDesc = "Menampilkan jadwal salat hari ini secara menetap di kolom notifikasi"
                val ongoingImportance = NotificationManager.IMPORTANCE_LOW
                val ongoingChannel = NotificationChannel(ONGOING_CHANNEL_ID, ongoingName, ongoingImportance).apply {
                    description = ongoingDesc
                    setShowBadge(false)
                    enableLights(false)
                    enableVibration(false)
                    setSound(null, null)
                }
                notificationManager?.createNotificationChannel(ongoingChannel)
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    fun updateOngoingNotification(
        context: Context,
        locationName: String,
        subuh: String,
        dzuhur: String,
        ashar: String,
        maghrib: String,
        isya: String,
        isId: Boolean
    ) {
        try {
            val prefs = context.getSharedPreferences("salat_prefs", Context.MODE_PRIVATE)
            val isEnabled = prefs.getBoolean("is_ongoing_notification_enabled", true)
            
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
            if (!isEnabled) {
                notificationManager?.cancel(ONGOING_NOTIFICATION_ID)
                return
            }

            createNotificationChannel(context)

            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent: PendingIntent = PendingIntent.getActivity(
                context, 1, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val title = if (isId) "📍 Jadwal Salat • $locationName" else "📍 مواقيت الصلاة • $locationName"
            val text = if (isId) {
                "Subuh $subuh  |  Dzuhur $dzuhur  |  Ashar $ashar  |  Maghrib $maghrib  |  Isya $isya"
            } else {
                "الفجر $subuh  |  الظهر $dzuhur  |  العصر $ashar  |  المغرب $maghrib  |  العشاء $isya"
            }

            val builder = NotificationCompat.Builder(context, ONGOING_CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setShowWhen(false)
                .setAutoCancel(false)

            notificationManager?.notify(ONGOING_NOTIFICATION_ID, builder.build())
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    fun cancelOngoingNotification(context: Context) {
        try {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
            notificationManager?.cancel(ONGOING_NOTIFICATION_ID)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    fun showPrayerNotification(context: Context, prayerNameId: String, prayerNameAr: String, timeStr: String) {
        try {
            createNotificationChannel(context)

            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent: PendingIntent = PendingIntent.getActivity(
                context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val soundUri = android.net.Uri.parse("android.resource://" + context.packageName + "/" + com.example.R.raw.azan_nabawi)

            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Waktunya $prayerNameId - $prayerNameAr")
                .setContentText("Sudah masuk waktu $prayerNameId ($timeStr). Mari tunaikan ibadah salat.")
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
            notificationManager?.notify(NOTIFICATION_ID, builder.build())
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}
