package com.example.sqlite.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = WarmBrown,
    onPrimary = SoftWhite,
    primaryContainer = LightPink,
    onPrimaryContainer = DarkBrown,

    secondary = AccentBrown,
    onSecondary = SoftWhite,
    secondaryContainer = LightCream,
    onSecondaryContainer = DarkBrown,

    background = CreamBackground,
    onBackground = DarkBrown,

    surface = SoftWhite,
    onSurface = DarkBrown,
    surfaceVariant = LightCream,
    onSurfaceVariant = AccentBrown,

    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF)
)

private val DarkColorScheme = darkColorScheme(
    primary = PastelPink,
    onPrimary = DarkBrown,
    primaryContainer = WarmBrown,
    onPrimaryContainer = LightPink,

    secondary = AccentBrown,
    onSecondary = SoftWhite,
    secondaryContainer = DarkBrown,
    onSecondaryContainer = LightCream,

    background = DarkBrown,
    onBackground = LightCream,

    surface = Color(0xFF2D1F1F),
    onSurface = LightCream,
    surfaceVariant = Color(0xFF3D2F2F),
    onSurfaceVariant = PastelPink
)

@Composable
fun SQLiteTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
