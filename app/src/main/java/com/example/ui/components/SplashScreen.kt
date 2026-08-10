package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.AppLanguage

@Composable
fun SplashScreen(
    language: AppLanguage,
    onSplashFinished: () -> Unit
) {
    val scale = remember { Animatable(0.9f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
        )
        scale.animateTo(
            targetValue = 1.0f,
            animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
        )
        kotlinx.coroutines.delay(1800)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAF9F6)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(24.dp)
                .alpha(alpha.value)
                .scale(scale.value)
        ) {
            // User's exact logo image
            Image(
                painter = painterResource(id = R.drawable.mr_nashrun_logo_1786075386622),
                contentDescription = "Logo",
                modifier = Modifier
                    .fillMaxWidth(0.65f)
                    .widthIn(max = 240.dp)
                    .clip(RoundedCornerShape(20.dp)),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Subtle Loading Progress Indicator
            CircularProgressIndicator(
                modifier = Modifier.size(26.dp),
                color = Color(0xFF2E7D32),
                strokeWidth = 2.5.dp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Subtitle
            Text(
                text = if (language == AppLanguage.INDONESIAN) "Memuat Jadwal Salat & Fiqih..." else "يتم الآن تحميل مواقيت الصلاة والأحكام الفقهية...",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                ),
                color = Color(0xFF2C3E35)
            )
        }
    }
}
