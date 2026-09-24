package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = AutoPrimaryCyan,
    onPrimary = AutoDarkBackground,
    primaryContainer = AutoDarkSurfaceVariant,
    onPrimaryContainer = AutoPrimaryCyan,
    secondary = AutoSecondaryElectric,
    onSecondary = AutoDarkBackground,
    tertiary = AutoVehicleReadyGreen,
    onTertiary = AutoDarkBackground,
    background = AutoDarkBackground,
    onBackground = AutoTextPrimary,
    surface = AutoDarkSurface,
    onSurface = AutoTextPrimary,
    surfaceVariant = AutoDarkSurfaceVariant,
    onSurfaceVariant = AutoTextSecondary,
    outline = AutoCardBorder,
    error = AutoDangerRed,
    onError = AutoTextPrimary
)

private val LightColorScheme = lightColorScheme(
    primary = AutoCyanDim,
    onPrimary = AutoLightSurface,
    secondary = AutoSecondaryElectric,
    background = AutoLightBackground,
    onBackground = AutoLightTextPrimary,
    surface = AutoLightSurface,
    onSurface = AutoLightTextPrimary,
    surfaceVariant = AutoLightSurfaceVariant,
    onSurfaceVariant = AutoLightTextSecondary
)

@Composable
fun AutoConnectTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            window?.let {
                it.statusBarColor = colorScheme.background.toArgb()
                it.navigationBarColor = colorScheme.background.toArgb()
                WindowCompat.getInsetsController(it, view).isAppearanceLightStatusBars = !darkTheme
                WindowCompat.getInsetsController(it, view).isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    AutoConnectTheme(darkTheme = darkTheme, content = content)
}
