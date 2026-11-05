package com.example.projekakhirpam.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import android.app.Activity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val AksaLightColors = lightColorScheme(
    primary = AksaPalette.AksaYellow,
    onPrimary = AksaPalette.DeepBlack,
    primaryContainer = AksaPalette.AksaBrightYellow,
    onPrimaryContainer = AksaPalette.DeepBlack,
    secondary = AksaPalette.AksaBrightYellow,
    onSecondary = AksaPalette.DeepBlack,
    tertiary = AksaPalette.Success,
    onTertiary = AksaPalette.PureWhite,
    background = AksaPalette.SurfaceLight,
    onBackground = AksaPalette.DeepBlack,
    surface = AksaPalette.PureWhite,
    onSurface = AksaPalette.DeepBlack,
    surfaceVariant = AksaPalette.SoftGray,
    onSurfaceVariant = AksaPalette.TextSecondary,
    outline = AksaPalette.DeepBlack,
    outlineVariant = AksaPalette.AksaDarkGray,
    error = AksaPalette.Error,
    onError = AksaPalette.PureWhite
)

private val AksaDarkColors = darkColorScheme(
    primary = AksaPalette.AksaYellow,
    onPrimary = AksaPalette.DeepBlack,
    primaryContainer = Color(0xFF514700),
    onPrimaryContainer = Color(0xFFFFF2A8),
    secondary = AksaPalette.AksaBrightYellow,
    onSecondary = AksaPalette.DeepBlack,
    tertiary = AksaPalette.SuccessDark,
    onTertiary = AksaPalette.DeepBlack,
    background = AksaPalette.DeepBlack,
    onBackground = AksaPalette.PureWhite,
    surface = AksaPalette.DarkSurface,
    onSurface = AksaPalette.PureWhite,
    surfaceVariant = Color(0xFF333333),
    onSurfaceVariant = AksaPalette.MutedGray,
    outline = AksaPalette.MutedGray,
    outlineVariant = AksaPalette.AksaDarkGray,
    error = AksaPalette.ErrorDark,
    onError = AksaPalette.DeepBlack
)

private fun Typography.withAksaSansSerif() = copy(
    displayLarge = displayLarge.copy(fontFamily = FontFamily.SansSerif),
    displayMedium = displayMedium.copy(fontFamily = FontFamily.SansSerif),
    displaySmall = displaySmall.copy(fontFamily = FontFamily.SansSerif),
    headlineLarge = headlineLarge.copy(fontFamily = FontFamily.SansSerif),
    headlineMedium = headlineMedium.copy(fontFamily = FontFamily.SansSerif),
    headlineSmall = headlineSmall.copy(fontFamily = FontFamily.SansSerif),
    titleLarge = titleLarge.copy(fontFamily = FontFamily.SansSerif),
    titleMedium = titleMedium.copy(fontFamily = FontFamily.SansSerif),
    titleSmall = titleSmall.copy(fontFamily = FontFamily.SansSerif),
    bodyLarge = bodyLarge.copy(fontFamily = FontFamily.SansSerif),
    bodyMedium = bodyMedium.copy(fontFamily = FontFamily.SansSerif),
    bodySmall = bodySmall.copy(fontFamily = FontFamily.SansSerif),
    labelLarge = labelLarge.copy(fontFamily = FontFamily.SansSerif),
    labelMedium = labelMedium.copy(fontFamily = FontFamily.SansSerif),
    labelSmall = labelSmall.copy(fontFamily = FontFamily.SansSerif)
)

private val AksaType = Typography().withAksaSansSerif().copy(
    headlineLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 28.sp,
        lineHeight = 34.sp,
        fontWeight = FontWeight.Bold
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 24.sp,
        lineHeight = 30.sp,
        fontWeight = FontWeight.Bold
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        fontWeight = FontWeight.SemiBold
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        fontWeight = FontWeight.SemiBold
    ),
    bodyLarge = TextStyle(fontFamily = FontFamily.SansSerif, fontSize = 16.sp, lineHeight = 24.sp),
    bodyMedium = TextStyle(fontFamily = FontFamily.SansSerif, fontSize = 14.sp, lineHeight = 20.sp),
    bodySmall = TextStyle(fontFamily = FontFamily.SansSerif, fontSize = 12.sp, lineHeight = 18.sp)
)

private val AksaShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(20.dp)
)

@Composable
fun AksaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) AksaDarkColors else AksaLightColors
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = colorScheme.background.toArgb()
                window.navigationBarColor = colorScheme.background.toArgb()
                WindowCompat.getInsetsController(window, view).apply {
                    isAppearanceLightStatusBars = !darkTheme
                    isAppearanceLightNavigationBars = !darkTheme
                }
            }
        }
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = AksaType,
        shapes = AksaShapes,
        content = content
    )
}
