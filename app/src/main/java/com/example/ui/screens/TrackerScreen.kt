package com.example.ui.screens

import android.media.AudioManager
import android.media.ToneGenerator
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.NgawiKecamatan
import com.example.model.PrayerType

@Composable
fun TrackerScreen(
    language: AppLanguage,
    kecamatan: NgawiKecamatan,
    ihtiyatiMinutes: Int,
    hijriOffset: Int,
    isAzanEnabled: Boolean,
    isAzanPlaying: Boolean,
    enabledAzanPrayers: Set<PrayerType>,
    isSimpleUiMode: Boolean = false,
    isOngoingNotificationEnabled: Boolean = true,
    currentThemeMode: com.example.model.AppThemeMode = com.example.model.AppThemeMode.SYSTEM,
    currentPrimaryColor: Color = Color(0xFF2E7D32),
    onThemeModeChange: (com.example.model.AppThemeMode) -> Unit = {},
    onPrimaryColorChange: (Color) -> Unit = {},
    onLanguageChange: (AppLanguage) -> Unit,
    onKecamatanChange: (NgawiKecamatan) -> Unit,
    onIhtiyatiChange: (Int) -> Unit,
    onHijriOffsetChange: (Int) -> Unit,
    onToggleAzanEnabled: (Boolean) -> Unit,
    onTogglePrayerAzan: (PrayerType) -> Unit,
    onPlayAzan: () -> Unit,
    onStopAzan: () -> Unit,
    onToggleSimpleUiMode: (Boolean) -> Unit = {},
    onToggleOngoingNotification: (Boolean) -> Unit = {}
) {
    val isId = language == AppLanguage.INDONESIAN
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))

            // SECTION 1: APP SETTINGS
            Text(
                text = if (isId) "Pengaturan Tampilan & Aplikasi" else "إعدادات العرض والتطبيق",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        // UI MODE SETTINGS CARD
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSimpleUiMode) MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier.fillMaxWidth().testTag("ui_mode_settings_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isId) "Mode Tampilan Aplikasi" else "وضع عرض التطبيق",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if (isSimpleUiMode)
                                    (if (isId) "⚡ Mode Simpel (Teks besar, ramah lansia & bebas rumit)" else "⚡ الوضع البسيط (خط كبير وسهل)")
                                else
                                    (if (isId) "✨ Mode Lengkap (Fitur lengkap, statistik & widget)" else "✨ الوضع الكامل (مميزات كاملة)"),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Switch(
                            checked = isSimpleUiMode,
                            onCheckedChange = { onToggleSimpleUiMode(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.primary,
                                checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    }
                }
            }
        }

        // THEME CUSTOMIZER CARD IN SETTINGS
        item {
            com.example.ui.components.ThemeCustomizerCard(
                language = language,
                currentThemeMode = currentThemeMode,
                currentPrimaryColor = currentPrimaryColor,
                onThemeModeChange = onThemeModeChange,
                onPrimaryColorChange = onPrimaryColorChange
            )
        }

        // AZAN NABAWI ALARM SETTINGS CARD
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isAzanEnabled) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f) else MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier.fillMaxWidth().testTag("azan_settings_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                            Icon(
                                imageVector = if (isAzanEnabled) Icons.Default.NotificationsActive else Icons.Default.NotificationsOff,
                                contentDescription = "Azan Alarm",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (isId) "Alarm Azan Nabawi" else "أذان التنبيه النبوي",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = if (isAzanEnabled)
                                        (if (isId) "Aktif - Memutar audio Azan saat masuk waktu salat" else "مفعل - تشغيل الأذان عند دخول وقت الصلاة")
                                    else
                                        (if (isId) "Nonaktif (Hanya notifikasi visual)" else "غير مفعل"),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (isAzanEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = if (isAzanEnabled) FontWeight.SemiBold else FontWeight.Normal
                                )
                            }
                        }

                        Switch(
                            checked = isAzanEnabled,
                            onCheckedChange = { onToggleAzanEnabled(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                                checkedTrackColor = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier.testTag("azan_master_switch")
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = if (isId)
                            "Audio Azan Madinah berkumandang otomatis saat masuk waktu salat (offline)."
                        else
                            "أذان المسجد النبوي يعمل تلقائياً عند دخول وقت الصلاة بدون إنترنت.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // AUDIO PREVIEW BUTTON
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                if (isAzanPlaying) {
                                    onStopAzan()
                                } else {
                                    onPlayAzan()
                                    Toast.makeText(
                                        context,
                                        if (isId) "Memutar Audio Azan Madinah..." else "تشغيل الأذان النبوي...",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isAzanPlaying) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f).testTag("play_azan_preview_button")
                        ) {
                            Icon(
                                imageVector = if (isAzanPlaying) Icons.AutoMirrored.Filled.VolumeOff else Icons.AutoMirrored.Filled.VolumeUp,
                                contentDescription = "Play/Stop",
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isAzanPlaying)
                                    (if (isId) "Hentikan Audio Azan" else "إيقاف الأذان")
                                else
                                    (if (isId) "Putar Preview Azan Nabawi" else "تجربة الأذان النبوي"),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    if (isAzanEnabled) {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                        // PER-PRAYER TOGGLES
                        Text(
                            text = if (isId) "Pilih Salat yang Mengaktifkan Azan:" else "اختر الصلوات لتفعيل الأذان:",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        val prayers = listOf(
                            PrayerType.SUBUH to (if (isId) "Subuh" else "الفجر"),
                            PrayerType.DZUHUR to (if (isId) "Dzuhur" else "الظهر"),
                            PrayerType.ASHAR to (if (isId) "Ashar" else "العصر"),
                            PrayerType.MAGHRIB to (if (isId) "Maghrib" else "المغرب"),
                            PrayerType.ISYA to (if (isId) "Isya" else "العشاء")
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            prayers.forEach { (type, label) ->
                                val selected = enabledAzanPrayers.contains(type)
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { onTogglePrayerAzan(type) }
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 2.dp)
                                    ) {
                                        Text(
                                            text = label,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = if (selected) "ON" else "OFF",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontSize = 9.sp,
                                            color = if (selected) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f) else Color.Gray
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // ONGOING NOTIFICATION (STATUS BAR) CARD
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isOngoingNotificationEnabled) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f) else MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier.fillMaxWidth().testTag("ongoing_notification_settings_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                            Icon(
                                imageVector = if (isOngoingNotificationEnabled) Icons.Default.NotificationsActive else Icons.Default.NotificationsOff,
                                contentDescription = "Ongoing Notification",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (isId) "Jadwal di Kolom Notifikasi" else "مواقيت الصلاة في التنبيهات",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = if (isOngoingNotificationEnabled)
                                        (if (isId) "Aktif - Menampilkan jadwal salat menetap di kolom notifikasi HP" else "مفعل - عرض مواقيت الصلاة بشكل دائم في التنبيهات")
                                    else
                                        (if (isId) "Nonaktif" else "غير مفعل"),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (isOngoingNotificationEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = if (isOngoingNotificationEnabled) FontWeight.SemiBold else FontWeight.Normal
                                )
                            }
                        }

                        Switch(
                            checked = isOngoingNotificationEnabled,
                            onCheckedChange = { onToggleOngoingNotification(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                                checkedTrackColor = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier.testTag("ongoing_notification_switch")
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = if (isId)
                            "Menampilkan ringkasan jadwal salat hari ini secara tenang, hening, dan menetap di kolom status/notifikasi sistem, memudahkan pemantauan seperti halnya WhatsApp tanpa membuka aplikasi."
                        else
                            "يعرض ملخصاً صامتاً لمواقيت الصلاة اليومية بشكل دائم في شريط التنبيهات للوصول السريع والسهل.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // LANGUAGE SETTING CARD
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Language, contentDescription = "Bahasa", tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isId) "Bahasa Utama Aplikasi" else "لغة التطبيق الرئيسية",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onLanguageChange(AppLanguage.INDONESIAN) }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = language == AppLanguage.INDONESIAN,
                            onClick = { onLanguageChange(AppLanguage.INDONESIAN) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Bahasa Indonesia (Bahasa Utama)", style = MaterialTheme.typography.bodyMedium)
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onLanguageChange(AppLanguage.ARABIC) }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = language == AppLanguage.ARABIC,
                            onClick = { onLanguageChange(AppLanguage.ARABIC) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("اللغة العربية (Bahasa Arab)", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }

        // IHTIYATI & HIJRI OFFSET CARD
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Timer, contentDescription = "Ihtiyati", tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isId) "Koreksi Waktu & Hijriah" else "ضبط الوقت والتقويم الهجري",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Ihtiyati Adjustment
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isId) "Ihtiyati (Menit Pengaman Salat)" else "احتياطي وقت الصلاة",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = if (isId) "Standar Kemenag RI adalah +2 menit" else "الافتراضي +٢ دقيقة",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { if (ihtiyatiMinutes > 0) onIhtiyatiChange(ihtiyatiMinutes - 1) }) {
                                Icon(Icons.Default.Remove, contentDescription = "Kurang")
                            }
                            Text(
                                text = "+$ihtiyatiMinutes m",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            IconButton(onClick = { if (ihtiyatiMinutes < 5) onIhtiyatiChange(ihtiyatiMinutes + 1) }) {
                                Icon(Icons.Default.Add, contentDescription = "Tambah")
                            }
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

                    // Hijri Offset Adjustment
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isId) "Koreksi Tanggal Hijriah" else "تعديل التاريخ الهجري",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = if (isId) "Penyelarasan Ru'yatul Hilal lokal" else "محاذاة لرؤية الهلال",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { if (hijriOffset > -2) onHijriOffsetChange(hijriOffset - 1) }) {
                                Icon(Icons.Default.Remove, contentDescription = "Kurang")
                            }
                            Text(
                                text = if (hijriOffset >= 0) "+$hijriOffset h" else "$hijriOffset h",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            IconButton(onClick = { if (hijriOffset < 2) onHijriOffsetChange(hijriOffset + 1) }) {
                                Icon(Icons.Default.Add, contentDescription = "Tambah")
                            }
                        }
                    }
                }
            }
        }

        // AZAN SOUND ALARM TEST
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.MusicNote, contentDescription = "Azan", tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isId) "Uji Suara Notifikasi Azan" else "تجربة صوت أذان التنبيه",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = if (isId)
                            "Aplikasi dapat mengeluarkan suara pengingat saat masuk waktu salat secara offline."
                        else
                            "يعمل التنبيه صوتيا عند دخول وقت الصلاة بدون إنترنت.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            try {
                                val toneG = ToneGenerator(AudioManager.STREAM_ALARM, 80)
                                toneG.startTone(ToneGenerator.TONE_PROP_BEEP, 1500)
                                Toast.makeText(
                                    context,
                                    if (isId) "Pengingat Azan Berbunyi (Offline Test)!" else "تم تشغيل صوت التنبيه بنجاح!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("test_azan_sound_button")
                    ) {
                        Text(if (isId) "Tes Suara Pengingat Salat" else "تشغيل صوت التجربة")
                    }
                }
            }
        }

        // WIDGET INFORMATION CARD (MOVED FROM HOME SCREEN)
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.25f)
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)),
                modifier = Modifier.fillMaxWidth().testTag("widget_info_card")
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.NotificationsActive,
                        contentDescription = "Widget Info",
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (isId) "Tersedia Widget Layar Utama!" else "يتوفر ويدجت للشاشة الرئيسية!",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isId) 
                                "Aplikasi ini dilengkapi Widget cantik untuk memantau jadwal salat secara instan dari Layar Utama HP Anda. Tekan lama ruang kosong di Layar Utama HP Anda, lalu pilih 'Widget' -> 'Jadwal Salat Ngawi' untuk memasangnya."
                            else
                                "يأتي التطبيق مع ويدجت مميز لمتابعة مواقيت الصلاة مباشرة من شاشتك الرئيسية. اضغط مطولاً على أي مساحة فارغة في شاشة هاتفك الرئيسية، ثم اختر 'Widgets' -> 'Jadwal Salat Ngawi' لتثبيته.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }

        // APP METADATA & COMPATIBILITY INFO
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, contentDescription = "Info", tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isId) "Tentang Jadwal Salat Ngawi" else "حول التطبيق",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (isId)
                            "• Perhitungan Astronomi Kemenag RI 10 Tahun (2026–2036+)\n• Wilayah: Kabupaten Ngawi & Seluruh Indonesia (WIB)\n• Bahasa: Indonesia & Arab\n• Database Lokal Room (Offline)"
                        else
                            "• حسابات أوفلاين لمحافظة نغاوي وإندونيسيا لـ ١٠ سنوات (٢٠٢٦ - ٢٠٣٦)\n• دعم اللغة الإندونيسية والعربية\n• يتوافق مع أجهزة أندرويد (API 24+)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
