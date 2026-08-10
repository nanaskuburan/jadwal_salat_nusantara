package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.model.AppLanguage
import com.example.model.AppThemeMode

fun createCustomColorScheme(primaryColor: Color, isDark: Boolean): ColorScheme {
    val safePrimary = if (primaryColor == Color.Unspecified || primaryColor.alpha == 0f) Color(0xFF2E7D32) else primaryColor
    val hsv = FloatArray(3)
    android.graphics.Color.colorToHSV(safePrimary.toArgb(), hsv)
    val hue = hsv[0]
    val sat = hsv[1]
    val valVal = hsv[2]

    return if (isDark) {
        val darkPrimary = Color.hsv(hue, (sat * 0.65f).coerceIn(0.15f, 0.85f), (valVal * 1.25f).coerceIn(0.7f, 1.0f))
        val darkContainer = Color.hsv(hue, 0.35f, 0.22f)
        val darkBg = Color(0xFF121614)
        val darkSurface = Color(0xFF1B221E)
        val darkSurfaceVariant = Color(0xFF26302B)

        darkColorScheme(
            primary = darkPrimary,
            onPrimary = Color.Black,
            primaryContainer = darkContainer,
            onPrimaryContainer = Color.White,
            secondary = darkPrimary.copy(alpha = 0.85f),
            onSecondary = Color.Black,
            secondaryContainer = darkContainer,
            onSecondaryContainer = Color.White,
            background = darkBg,
            surface = darkSurface,
            surfaceVariant = darkSurfaceVariant,
            onBackground = Color(0xFFE2E8E4),
            onSurface = Color(0xFFE2E8E4),
            onSurfaceVariant = Color(0xFFA2B0A6)
        )
    } else {
        val lightContainer = Color.hsv(hue, 0.12f, 0.96f)
        val lightSecondary = Color.hsv(hue, (sat * 0.75f).coerceIn(0.2f, 0.75f), (valVal * 0.75f).coerceIn(0.3f, 0.65f))

        lightColorScheme(
            primary = safePrimary,
            onPrimary = Color.White,
            primaryContainer = lightContainer,
            onPrimaryContainer = safePrimary,
            secondary = lightSecondary,
            onSecondary = Color.White,
            secondaryContainer = lightContainer,
            onSecondaryContainer = safePrimary,
            background = Color(0xFFF7F9F7),
            surface = Color.White,
            surfaceVariant = Color(0xFFEEF2EE),
            onBackground = Color(0xFF1A211D),
            onSurface = Color(0xFF1A211D),
            onSurfaceVariant = Color(0xFF6B7A70)
        )
    }
}

@Composable
fun SalatNgawiTheme(
    themeMode: AppThemeMode = AppThemeMode.SYSTEM,
    primaryColor: Color = Color(0xFF5A6B5D),
    language: AppLanguage = AppLanguage.INDONESIAN,
    content: @Composable () -> Unit,
) {
    val isDark = when (themeMode) {
        AppThemeMode.SYSTEM -> isSystemInDarkTheme()
        AppThemeMode.LIGHT -> false
        AppThemeMode.DARK -> true
    }

    val colorScheme = createCustomColorScheme(primaryColor, isDark)
    val typography = getAppTypography(language == AppLanguage.ARABIC)

    MaterialTheme(colorScheme = colorScheme, typography = typography, content = content)
}
