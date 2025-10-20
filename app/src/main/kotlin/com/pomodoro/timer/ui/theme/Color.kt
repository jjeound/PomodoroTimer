package com.pomodoro.timer.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

internal val White = Color(0xFFFFFFFF)
internal val Black = Color(0xFF000000)
internal val Black100 = Color(0xFF0F0F14)
internal val Black200 = Color(0xFF16181C)
internal val Blue100 = Color(0xFF5FCBB2)
internal val Grey100 = Color(0xFF292929)
internal val Grey300 = Color(0xFFA3A3A3)
internal val Grey400 = Color(0xFFF0F0F0)
internal val Grey500 = Color(0xFFF6F6F6)
internal val Red = Color(0xFFFF0000)

@Immutable
data class CustomColors(
    val iconSelected: Color,
    val iconDefault: Color,
    val iconPrimary: Color,
    val iconRed: Color,
    val primary: Color,
    val onPrimary: Color,
    val surface: Color,
    val onSurface: Color,
    val border: Color,
    val borderLight : Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val error: Color,
    val buttonSurface: Color,
    val buttonBorderUnfocused: Color,
    val buttonBorderFocused: Color,
    val textFieldSurface: Color,
    val textFieldBorder: Color,
    val placeholder: Color,
)

val LightCustomColors = CustomColors(
    iconSelected = Black,
    iconDefault = Grey300,
    iconPrimary = Blue100,
    iconRed = Red,
    primary = Blue100,
    onPrimary = White,
    surface = Grey500,
    onSurface = White,
    border = Grey300,
    borderLight = Grey400,
    textPrimary = Black,
    textSecondary = Grey300,
    textTertiary = Grey400,
    error = Red,
    buttonSurface = White,
    buttonBorderUnfocused = Grey400,
    buttonBorderFocused = Black,
    textFieldSurface = White,
    textFieldBorder = Black,
    placeholder = Grey300,
)
val DarkCustomColors = CustomColors(
    iconSelected = White,
    iconDefault = Grey300,
    iconPrimary = Blue100,
    iconRed = Red,
    primary = Blue100,
    onPrimary = White,
    surface = Black100,
    onSurface = Black200,
    border = Grey300,
    borderLight = Grey100,
    textPrimary = White,
    textSecondary = Grey300,
    textTertiary = Grey400,
    error = Red,
    buttonSurface = Black,
    buttonBorderUnfocused = Grey300,
    buttonBorderFocused = White,
    textFieldSurface = Black200,
    textFieldBorder = White,
    placeholder = Grey300,
)