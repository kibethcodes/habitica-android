package com.habitrpg.android.habitica.compose.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import com.google.android.material.composethemeadapter.createMdcTheme

private val DarkColorPalette = darkColors(
    primary = brand_300,
    primaryVariant = brand_100,
    secondary = brand_400
)

private val LightColorPalette = lightColors(
    primary = brand_300,
    primaryVariant = brand_100,
    secondary = brand_400
)

@Immutable
data class ExtendedColors(
    val brandNeon: Color,
    val windowBackground: Color,
    val accent: Color
)

val LocalExtendedColors = staticCompositionLocalOf {
    ExtendedColors(
        brandNeon = Color.Unspecified,
        windowBackground = Color.Unspecified,
        accent = Color.Unspecified
    )
}

private val ExtendedBaseDark = ExtendedColors(
    brandNeon = brand_500,
    windowBackground = gray_5,
    accent = brand_400
)

private val ExtendedBaseLight = ExtendedColors(
    brandNeon = brand_400,
    windowBackground = gray_700,
    accent = brand_400
)

private val ExtendedBlueDark = ExtendedBaseDark.copy(
    brandNeon = blue_500,
)

private val ExtendedBlueLight = ExtendedBaseLight.copy(
    brandNeon = blue_100,
)

private val ExtendedRedDark = ExtendedBaseDark.copy(
    brandNeon = red_500,
)

private val ExtendedRedLight = ExtendedBaseLight.copy(
    brandNeon = red_100,
)
private val ExtendedMaroonDark = ExtendedBaseDark.copy(
    brandNeon = maroon_500,
)

private val ExtendedMaroonLight = ExtendedBaseLight.copy(
    brandNeon = maroon_100,
)

private val ExtendedOrangeDark = ExtendedBaseDark.copy(
    brandNeon = orange_500,
)

private val ExtendedOrangeLight = ExtendedBaseLight.copy(
    brandNeon = orange_100,
)
private val ExtendedYellowDark = ExtendedBaseDark.copy(
    brandNeon = yellow_500,
)

private val ExtendedYellowLight = ExtendedBaseLight.copy(
    brandNeon = yellow_100,
)

private val ExtendedGreenDark = ExtendedBaseDark.copy(
    brandNeon = green_500,
)

private val ExtendedGreenLight = ExtendedBaseLight.copy(
    brandNeon = green_100,
)
private val ExtendedTealDark = ExtendedBaseDark.copy(
    brandNeon = teal_500,
)

private val ExtendedTealLight = ExtendedBaseLight.copy(
    brandNeon = teal_100,
)

enum class HabiticaPalette {
    DEFAULT, MAROON, RED, ORANGE, YELLOW, GREEN, TEAL, BLUE, PURPLE
}

@Composable
fun HabiticaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    colorPalette: HabiticaPalette = HabiticaPalette.DEFAULT,
    content: @Composable () -> Unit
) {
//    val context = LocalContext.current
//    val layoutDirection = LocalLayoutDirection.current
//    var (colors, typography, shapes) = createMdcTheme(
//        context = context,
//        layoutDirection = layoutDirection
//    )

    val fallbackColors = if (darkTheme) {
        when (colorPalette) {
            HabiticaPalette.BLUE -> DarkColorPalette
            else -> DarkColorPalette
        }

    } else {
        when (colorPalette) {
            HabiticaPalette.BLUE -> LightColorPalette
            else -> LightColorPalette
        }
    }
    val extendedColors = if (darkTheme) {
        when (colorPalette) {
            HabiticaPalette.MAROON -> ExtendedMaroonDark
            HabiticaPalette.RED -> ExtendedRedDark
            HabiticaPalette.ORANGE -> ExtendedOrangeDark
            HabiticaPalette.YELLOW -> ExtendedYellowDark
            HabiticaPalette.GREEN -> ExtendedGreenDark
            HabiticaPalette.TEAL -> ExtendedTealDark
            HabiticaPalette.BLUE -> ExtendedBlueDark
            else -> ExtendedBaseDark
        }

    } else {
        when (colorPalette) {
            HabiticaPalette.MAROON -> ExtendedMaroonLight
            HabiticaPalette.RED -> ExtendedRedLight
            HabiticaPalette.ORANGE -> ExtendedOrangeLight
            HabiticaPalette.YELLOW -> ExtendedYellowLight
            HabiticaPalette.GREEN -> ExtendedGreenLight
            HabiticaPalette.TEAL -> ExtendedTealLight
            HabiticaPalette.BLUE -> ExtendedBlueLight
            else -> ExtendedBaseLight
        }
    }
    CompositionLocalProvider(LocalExtendedColors provides extendedColors) {
        MaterialTheme(
//            colors = colors ?: fallbackColors,
            colors = fallbackColors,
//            typography = typography ?: Typography,
            typography = Typography,
//            shapes = shapes ?: Shapes,
            shapes = Shapes,
            content = content
        )
    }
}

// Use with eg. ExtendedTheme.colors.tertiary
object HabiticaTheme {
    val colors: ExtendedColors
        @Composable
        get() = LocalExtendedColors.current
}