package com.tunnexa.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// Design System Tokens
object DesignTokens {
    // Corner Radius
    val CornerRadius = 12.dp
    val CornerRadiusSmall = 8.dp
    val CornerRadiusLarge = 16.dp
    val CornerRadiusFull = 999.dp // For pills/rounded buttons
    
    // Elevation
    val DefaultElevation = 4.dp
    
    // Spacing Scale
    val SpaceXXSmall = 4.dp
    val SpaceXSmall = 8.dp
    val SpaceSmall = 16.dp
    val SpaceMedium = 24.dp
    val SpaceLarge = 32.dp
    
    // Default Padding
    val HorizontalPadding = 20.dp
    val VerticalPadding = 16.dp
    
    // Button Sizes
    val ButtonHeight = 48.dp
    val IconSize = 24.dp
    val IconSizeSmall = 16.dp
}
object AppColors {
    val ConnectColor1 = Color(0xFF20D46E)
    val ConnectColor2 = Color(0xFF527AE7)
    val TitleColor = Color(0xFFFFFFFF)
    val SubtitleColor = Color(0xB3BBBABA)
}
private val DarkColorScheme = darkColorScheme(
    primary = PrimaryGreen,
    secondary = SecondaryBlue,
    tertiary = AccentCoral,
    background = BackgroundDark,
    surface = BackgroundMedium,
    onPrimary = TextWhite,
    onSecondary = TextWhite,
    onBackground = TextWhite,
    onSurface = TextWhite,
    error = StatusDisconnected,
    outline = BorderLight
)

@Composable
fun TunnexaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = AppTypography,
        content = content
    )
}
