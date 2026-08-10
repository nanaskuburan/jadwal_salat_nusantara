package com.example.ui.components

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.LinearEasing
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.launch
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DayPrayerSchedule
import com.example.model.IndonesiaLocation
import com.example.receiver.PrayerWidgetProvider

@Composable
fun WidgetHomeScreenCard(
    location: IndonesiaLocation,
    todaySchedule: DayPrayerSchedule,
    isOnline: Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val rotation = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize()
            .testTag("widget_home_screen_card")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Widgets,
                        contentDescription = "Widget Icon",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Widget Android Home Screen",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Tampilkan jadwal 5 waktu di menu HP & dapat di-resize",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // LIVE WIDGET PREVIEW CARD
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF1B4332),
                                Color(0xFF082A1D)
                            )
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = Color(0xFF2D6A4F),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(12.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Preview Top Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "📍 ${location.name}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = if (isOnline) "Online Internet Sync" else "Offline • Disimpan",
                                fontSize = 10.sp,
                                color = if (isOnline) Color(0xFF80CBC4) else Color(0xFFFFCC80)
                            )
                        }

                        // Refresh Button
                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    PrayerWidgetProvider.updateAllWidgets(context)
                                    rotation.animateTo(
                                        targetValue = rotation.value + 360f,
                                        animationSpec = tween(durationMillis = 800, easing = LinearEasing)
                                    )
                                }
                            },
                            modifier = Modifier
                                .size(32.dp)
                                .background(Color(0xFF2D6A4F), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Refresh Widget",
                                tint = Color.White,
                                modifier = Modifier
                                    .size(18.dp)
                                    .graphicsLayer { rotationZ = rotation.value }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // 5 Prayer Times Preview Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        WidgetPrayerTimeColumn("Subuh", todaySchedule.subuh)
                        WidgetPrayerTimeColumn("Dzuhur", todaySchedule.dzuhur)
                        WidgetPrayerTimeColumn("Ashar", todaySchedule.ashar)
                        WidgetPrayerTimeColumn("Maghrib", todaySchedule.maghrib)
                        WidgetPrayerTimeColumn("Isya", todaySchedule.isya)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Button: Request Pin App Widget
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        try {
                            val appWidgetManager = AppWidgetManager.getInstance(context)
                            val myProvider = ComponentName(context, PrayerWidgetProvider::class.java)

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && appWidgetManager.isRequestPinAppWidgetSupported) {
                                appWidgetManager.requestPinAppWidget(myProvider, null, null)
                            } else {
                                Toast.makeText(
                                    context,
                                    "Tekan lama layar utama HP Anda -> Pilih 'Widget' -> Tambahkan 'Jadwal Salat 5 Waktu'",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(
                                context,
                                "Tekan lama layar utama HP Anda -> Pilih 'Widget' -> Tambahkan 'Jadwal Salat 5 Waktu'",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Pasang ke HP", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = {
                        coroutineScope.launch {
                            PrayerWidgetProvider.updateAllWidgets(context)
                            rotation.animateTo(
                                targetValue = rotation.value + 360f,
                                animationSpec = tween(durationMillis = 800, easing = LinearEasing)
                            )
                        }
                    },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh",
                        modifier = Modifier
                            .size(18.dp)
                            .graphicsLayer { rotationZ = rotation.value }
                    )
                }
            }
        }
    }
}

@Composable
private fun WidgetPrayerTimeColumn(
    label: String,
    time: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFA3E635)
        )
        Text(
            text = time,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}
