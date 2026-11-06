package com.deckjoist.spacer.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightScheme = lightColorScheme(
    primary = CedarPrimary,
    onPrimary = MossBackground,
    primaryContainer = CedarDeep,
    onPrimaryContainer = MossBackground,
    secondary = SecondarySage,
    onSecondary = TextPrimary,
    tertiary = RustAccent,
    onTertiary = MossBackground,
    background = MossBackground,
    onBackground = TextPrimary,
    surface = PaperSurface,
    onSurface = TextPrimary,
    surfaceVariant = PaperSurfaceAlt,
    onSurfaceVariant = TextSecondary,
    outline = SecondarySage,
    outlineVariant = SecondarySage,
    error = RustAccent,
    onError = MossBackground
)

private val DarkScheme = darkColorScheme(
    primary = CedarPrimary,
    onPrimary = MossBackground,
    primaryContainer = CedarDeep,
    onPrimaryContainer = MossBackground,
    secondary = SecondarySage,
    onSecondary = SplashInk,
    tertiary = RustAccent,
    onTertiary = MossBackground,
    background = SplashInk,
    onBackground = MossBackground,
    surface = CedarDeep,
    onSurface = MossBackground,
    surfaceVariant = CedarDeep,
    onSurfaceVariant = SplashMist,
    outline = SecondarySage,
    outlineVariant = SecondarySage,
    error = RustAccent,
    onError = MossBackground
)

@Composable
fun AppTheme(useDarkScheme: Boolean = false, content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (useDarkScheme) DarkScheme else LightScheme,
        typography = AppTypography,
        content = content
    )
}
