package com.example.sqlite.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFD4A5A5),           // Rosa pastel
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFE4E8),  // Rosa muy claro
    onPrimaryContainer = Color(0xFF5E4140), // Marrón oscuro
    secondary = Color(0xFFF5E6D3),          // Beige
    onSecondary = Color(0xFF5E4140),
    background = Color(0xFFFFFBF5),         // Crema
    onBackground = Color(0xFF1F1B16),
    surface = Color.White,
    onSurface = Color(0xFF1F1B16),
    error = Color(0xFFBA1A1A),
    onError = Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFD4A5A5),           // Rosa pastel (mismo)
    onPrimary = Color(0xFF2D1514),
    primaryContainer = Color(0xFF5E4140),  // Marrón oscuro
    onPrimaryContainer = Color(0xFFFFE4E8),
    secondary = Color(0xFF7B6B61),
    onSecondary = Color(0xFF2D1514),
    background = Color(0xFF1F1B16),        // Marrón muy oscuro
    onBackground = Color(0xFFEAE1D9),
    surface = Color(0xFF2A2420),           // Marrón oscuro
    onSurface = Color(0xFFEAE1D9),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005)
)

@Composable
fun SQLiteTheme(
    darkTheme: Boolean = ThemeManager.isDarkMode.value, //ThemeManager
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
