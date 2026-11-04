package com.example.projekakhirpam.presentation.theme

import androidx.compose.ui.graphics.Color

/** AKSA's shared Yellow & Black palette. Keep screen colors in MaterialTheme roles. */
object AksaPalette {
    val AksaBlack = Color(0xFF121212)
    val AksaYellow = Color(0xFFFFD600)
    val AksaBrightYellow = Color(0xFFFFEA00)
    val AksaWhite = Color(0xFFFFFFFF)
    val AksaSoftGray = Color(0xFFF5F5F5)
    val AksaDarkGray = Color(0xFF333333)

    // Compatibility names keep existing screens on the same centralized palette.
    val PrimaryYellow = AksaYellow
    val WarmYellow = AksaBrightYellow
    val DeepBlack = AksaBlack
    val PureWhite = AksaWhite
    val SoftGray = AksaSoftGray
    val MutedGray = Color(0xFFBDBDBD)

    val SurfaceLight = AksaWhite
    val TextSecondary = AksaDarkGray
    val BorderLight = AksaDarkGray.copy(alpha = 0.28f)
    val DarkSurface = Color(0xFF202020)

    val Success = Color(0xFF1B7A45)
    val SuccessDark = Color(0xFF72D49A)
    val Warning = Color(0xFF8A5A00)
    val Error = Color(0xFFB3261E)
    val ErrorDark = Color(0xFFFFB4AB)
}
