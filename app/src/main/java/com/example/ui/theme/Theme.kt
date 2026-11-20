package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val RiwaayatLightColorScheme =
  lightColorScheme(
    primary = DeepMaroon,
    onPrimary = Color.White,
    primaryContainer = DeepMaroonSoft,
    onPrimaryContainer = DeepMaroonDark,
    secondary = MutedGold,
    onSecondary = Color.White,
    secondaryContainer = MutedGoldSoft,
    onSecondaryContainer = MutedGoldDark,
    tertiary = CharcoalText,
    onTertiary = Color.White,
    background = WarmIvory,
    onBackground = CharcoalText,
    surface = WarmIvory,
    onSurface = CharcoalText,
    surfaceVariant = MutedSurface,
    onSurfaceVariant = MutedCharcoalText,
    surfaceContainerLowest = ElevatedCardSurface,
    surfaceContainerLow = SoftCream,
    surfaceContainer = MutedSurface,
    surfaceContainerHigh = Color(0xFFE8DFD5),
    outline = SoftBorder,
    outlineVariant = Color(0xFFEBE2D7),
    error = ErrorCrimson,
    onError = Color.White,
    errorContainer = ErrorCrimsonSoft,
    onErrorContainer = ErrorCrimson,
  )

val RiwaayatShapes =
  Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(22.dp),
    extraLarge = RoundedCornerShape(28.dp),
  )

@Composable
fun RiwaayatTheme(content: @Composable () -> Unit) {
  MaterialTheme(
    colorScheme = RiwaayatLightColorScheme,
    typography = RiwaayatTypography,
    shapes = RiwaayatShapes,
    content = content,
  )
}
