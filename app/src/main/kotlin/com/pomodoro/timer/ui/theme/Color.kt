package com.pomodoro.timer.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

val White = Color(0xFFFFFFFF)
val Black = Color(0xFF000000)
val Grey100 = Color(0xFFB2B2B2)
val Grey200 = Color(0xFFCACACA)
val Grey300 = Color(0xFFE5E5E5)
val Grey400 = Color(0xFFEFEFF0)
val Grey500 = Color(0xFFD2D2D4)
val Red = Color(0xFFF83A4E)

@Immutable
data class CustomColors(
    val surface: Color,
    val text: Color,
    val textSecondary: Color,
    val buttonText: Color,
    val buttonBorder: Color,
    val rippledButtonText: Color,
    val rippledButtonBorder: Color,
    val indicatorBox: Color,
    val dotIndicatorFocused: Color,
    val dotIndicatorUnfocused: Color,
    val divider: Color,
    val textFieldContainer: Color,
    val textFieldText: Color,
    val textFieldContainerUnfocused: Color,
    val selectorTextSelected: Color,
    val selectorTextUnselected: Color,
    val selectorDivider: Color,
    val selectorContainer: Color,
    val selectorContainerSelected: Color,
    val icon: Color,
    val primary: Color,
)

val LightCustomColors = CustomColors(
    surface = White,
    text = Black,
    textSecondary = White,
    buttonText = Black,
    buttonBorder = Black,
    rippledButtonText = Black,
    rippledButtonBorder = Black,
    indicatorBox = Grey200,
    dotIndicatorFocused = Black,
    dotIndicatorUnfocused = Grey100,
    divider = Grey300,
    textFieldContainer = Grey400,
    textFieldText = Black,
    textFieldContainerUnfocused = Grey200,
    selectorTextSelected = Black,
    selectorTextUnselected = Black,
    selectorDivider = Grey500,
    selectorContainer = Grey400,
    selectorContainerSelected = White,
    icon = Black,
    primary = Red
)
//val DarkCustomColors = CustomColors(
//    surface = Black,
//    text = White,
//    textSecondary = White,
//    buttonText = White,
//    buttonBorder = White,
//    rippledButtonText = White,
//    rippledButtonBorder = White,
//    indicatorBox = White,
//    dotIndicatorFocused = White,
//    dotIndicatorUnfocused = Grey100,
//    divider = Grey300,
//    textFieldContainer = Grey400,
//    textFieldText = Black,
//    textFieldContainerUnfocused = Grey200,
//    selectorTextSelected = Black,
//    selectorTextUnselected = Black,
//    selectorDivider = Grey500,
//    selectorContainer = Grey400,
//    selectorContainerSelected = White,
//    icon = White,
//    primary = Red
//)