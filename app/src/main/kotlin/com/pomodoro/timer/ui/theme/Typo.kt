package com.pomodoro.timer.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.pomodoro.timer.R

@Immutable
data class CustomTypography(
    val buttonTimerSmall: TextStyle,
    val buttonTimerLarge: TextStyle,
    val buttonEditSmall: TextStyle,
    val buttonEditLarge: TextStyle,
    val clockTimeSmall: TextStyle,
    val clockTimeLarge: TextStyle,
    val digitalTimeSmall: TextStyle,
    val digitalTimeLarge: TextStyle,
    val deskTimeSmall: TextStyle,
    val deskTimeLarge: TextStyle,
    val editTitleSmall: TextStyle,
    val editTitleLarge: TextStyle,
    val selectorSelected: TextStyle,
    val selectorUnSelected: TextStyle,
    val textField: TextStyle,
    val textPreview: TextStyle
)

val fontFamily = FontFamily(
    Font(R.font.pretendard_black, FontWeight.Black),
    Font(R.font.pretendard_bold, FontWeight.Bold),
    Font(R.font.pretendard_extrabold, FontWeight.ExtraBold),
    Font(R.font.pretendard_light, FontWeight.Light),
    Font(R.font.pretendard_medium, FontWeight.Medium),
    Font(R.font.pretendard_regular, FontWeight.Normal),
    Font(R.font.pretendard_semibold, FontWeight.SemiBold),
    Font(R.font.pretendard_thin, FontWeight.Thin),
    Font(R.font.pretendard_extralight, FontWeight.ExtraLight),
)
val digitalMonoFont = FontFamily(
    Font(R.font.digital_7_mono),
)

val customTypography =  CustomTypography(
    buttonTimerSmall = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Light, fontSize = 30.sp),
    buttonTimerLarge = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Light, fontSize = 40.sp),
    buttonEditSmall = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.SemiBold, fontSize = 14.sp),
    buttonEditLarge = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.SemiBold, fontSize = 14.sp),
    clockTimeSmall = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Normal, fontSize = 37.sp),
    clockTimeLarge = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Normal, fontSize = 82.sp),
    digitalTimeSmall = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Normal, fontSize = 60.sp),
    digitalTimeLarge = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Normal, fontSize = 100.sp),
    deskTimeSmall = TextStyle(fontFamily = digitalMonoFont, fontSize = 76.sp),
    deskTimeLarge = TextStyle(fontFamily = digitalMonoFont, fontSize = 300.sp),
    editTitleSmall = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Bold, fontSize = 18.sp),
    editTitleLarge = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Bold, fontSize = 50.sp),
    selectorSelected = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.SemiBold, fontSize = 13.sp),
    selectorUnSelected = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Normal, fontSize = 13.sp),
    textField = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Normal, fontSize = 13.sp),
    textPreview = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.SemiBold, fontSize = 50.sp)
)