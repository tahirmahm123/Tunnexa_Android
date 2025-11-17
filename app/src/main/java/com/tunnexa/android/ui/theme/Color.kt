package com.tunnexa.android.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Brand Colors
val PrimaryGreen = Color(0xFF22FF7E)
val SecondaryBlue = Color(0xFF2D88FF)
val AccentCoral = Color(0xFFFF705D)

// Background Gradients
val BackgroundDark = Color(0xFF2D3971)
val BackgroundMedium = Color(0xFF15182C)
val BottomNavBackground = Color(0xFF2F3052)

// App Gradient Brush
val appGradientBrush = Brush.linearGradient(
    colors = listOf(
        BackgroundDark,
        BackgroundMedium
    )
)

// Logo Gradient Colors (matching logo.xml)
val LogoGreen = Color(0xFF15E953)
val LogoPurple = Color(0xFF5C67FF)

// Logo Gradient Brush
val logoGradientBrush = Brush.linearGradient(
    colors = listOf(
        LogoGreen,
        LogoPurple
    )
)

// Neutral Colors
val TextWhite = Color(0xFFFFFFFF)
val TextGray = Color(0xFFC7C7C7)
val TextBlueGray = Color(0xFF889DB8)
val BorderLight = Color(0xFFD2D2EB)
val BorderWhite = Color(0xFFEBEBEB)

// Status Colors
val StatusDisconnected = Color(0xFFFF705D)
val StatusConnected = Color(0xFF38D338)
