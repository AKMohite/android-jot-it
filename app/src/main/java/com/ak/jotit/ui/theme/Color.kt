package com.ak.jotit.ui.theme

import androidx.annotation.ColorRes
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import com.ak.jotit.R

val md_theme_light_primary = Color(0xFF5555A9)
val md_theme_light_onPrimary = Color(0xFFFFFFFF)
val md_theme_light_primaryContainer = Color(0xFFE2DFFF)
val md_theme_light_onPrimaryContainer = Color(0xFF0D0664)
val md_theme_light_secondary = Color(0xFF5D5C72)
val md_theme_light_onSecondary = Color(0xFFFFFFFF)
val md_theme_light_secondaryContainer = Color(0xFFE2E0F9)
val md_theme_light_onSecondaryContainer = Color(0xFF1A1A2C)
val md_theme_light_tertiary = Color(0xFF795369)
val md_theme_light_onTertiary = Color(0xFFFFFFFF)
val md_theme_light_tertiaryContainer = Color(0xFFFFD8EB)
val md_theme_light_onTertiaryContainer = Color(0xFF2F1124)
val md_theme_light_error = Color(0xFFBA1A1A)
val md_theme_light_errorContainer = Color(0xFFFFDAD6)
val md_theme_light_onError = Color(0xFFFFFFFF)
val md_theme_light_onErrorContainer = Color(0xFF410002)
val md_theme_light_background = Color(0xFFFFFBFF)
val md_theme_light_onBackground = Color(0xFF1C1B1F)
val md_theme_light_surface = Color(0xFFFFFBFF)
val md_theme_light_onSurface = Color(0xFF1C1B1F)
val md_theme_light_surfaceVariant = Color(0xFFE4E1EC)
val md_theme_light_onSurfaceVariant = Color(0xFF47464F)
val md_theme_light_outline = Color(0xFF777680)
val md_theme_light_inverseOnSurface = Color(0xFFF3EFF4)
val md_theme_light_inverseSurface = Color(0xFF313034)
val md_theme_light_inversePrimary = Color(0xFFC2C1FF)
val md_theme_light_shadow = Color(0xFF000000)
val md_theme_light_surfaceTint = Color(0xFF5555A9)

val md_theme_dark_primary = Color(0xFFC2C1FF)
val md_theme_dark_onPrimary = Color(0xFF252477)
val md_theme_dark_primaryContainer = Color(0xFF3D3C8F)
val md_theme_dark_onPrimaryContainer = Color(0xFFE2DFFF)
val md_theme_dark_secondary = Color(0xFFC6C4DD)
val md_theme_dark_onSecondary = Color(0xFF2F2F42)
val md_theme_dark_secondaryContainer = Color(0xFF454559)
val md_theme_dark_onSecondaryContainer = Color(0xFFE2E0F9)
val md_theme_dark_tertiary = Color(0xFFE9B9D2)
val md_theme_dark_onTertiary = Color(0xFF46263A)
val md_theme_dark_tertiaryContainer = Color(0xFF5F3C51)
val md_theme_dark_onTertiaryContainer = Color(0xFFFFD8EB)
val md_theme_dark_error = Color(0xFFFFB4AB)
val md_theme_dark_errorContainer = Color(0xFF93000A)
val md_theme_dark_onError = Color(0xFF690005)
val md_theme_dark_onErrorContainer = Color(0xFFFFDAD6)
val md_theme_dark_background = Color(0xFF1C1B1F)
val md_theme_dark_onBackground = Color(0xFFE5E1E6)
val md_theme_dark_surface = Color(0xFF1C1B1F)
val md_theme_dark_onSurface = Color(0xFFE5E1E6)
val md_theme_dark_surfaceVariant = Color(0xFF47464F)
val md_theme_dark_onSurfaceVariant = Color(0xFFC8C5D0)
val md_theme_dark_outline = Color(0xFF918F9A)
val md_theme_dark_inverseOnSurface = Color(0xFF1C1B1F)
val md_theme_dark_inverseSurface = Color(0xFFE5E1E6)
val md_theme_dark_inversePrimary = Color(0xFF5555A9)
val md_theme_dark_shadow = Color(0xFF000000)
val md_theme_dark_surfaceTint = Color(0xFFC2C1FF)


val seed = Color(0xFF1A1A40)


fun getRandomColor(): JotItNoteBgColor {
    return getNoteBgColors().entries.shuffled().first().value
}

// TODO use hex values directly with colorIsLight() condition
fun getNoteBgColors(): Map<String, JotItNoteBgColor> {
    val colorsMap = mutableMapOf<String, JotItNoteBgColor>()
    colorsMap["note_bg_orange"] = JotItNoteBgColor(
        colorName = "note_bg_orange",
        colorRes = R.color.note_bg_orange,
        lightColor = 0xFFFD841F,
        darkColor = 0xFFA8381E
    )
    colorsMap["note_bg_pink"] = JotItNoteBgColor(
        colorName = "note_bg_pink",
        colorRes = R.color.note_bg_pink,
        lightColor = 0xFFFFD1D,
        darkColor = 0xFFB16161
    )
    colorsMap["note_bg_blue"] = JotItNoteBgColor(
        colorName = "note_bg_blue",
        colorRes = R.color.note_bg_blue,
        lightColor = 0xFF4D76D1,
        darkColor = 0xFF083AA9
    )
    colorsMap["note_bg_violet"] = JotItNoteBgColor(
        colorName = "note_bg_violet",
        colorRes = R.color.note_bg_violet,
        lightColor = 0xFF9A54D3,
        darkColor = 0xFF3F0071
    )
    colorsMap["note_bg_green"] = JotItNoteBgColor(
        colorName = "note_bg_green",
        colorRes = R.color.note_bg_green,
        lightColor = 0xFFC8DBBE,
        darkColor = 0xFF425F57
    )
    return colorsMap
}

@Composable
fun colorIsLight(): Boolean {
    return MaterialTheme.colorScheme.background.luminance() > 0.5
}

data class JotItNoteBgColor(
    val colorName: String,
    @ColorRes val colorRes: Int,
    val lightColor: Long,
    val darkColor: Long
)
