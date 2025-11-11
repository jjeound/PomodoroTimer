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
    val textPreview1: TextStyle,
    val textPreview2: TextStyle,
    val textPreview3: TextStyle,
    val textPreview4: TextStyle,
    val textPreview5: TextStyle,
    val textPreview6: TextStyle,
)

val pretendard = FontFamily(
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
val comfortaa = FontFamily(
    Font(R.font.comfortaa_light, FontWeight.Light),
    Font(R.font.comfortaa_regular, FontWeight.Normal),
    Font(R.font.comfortaa_medium, FontWeight.Medium),
    Font(R.font.comfortaa_semibold, FontWeight.SemiBold),
    Font(R.font.comfortaa_bold, FontWeight.Bold),
)
val freesentation = FontFamily(
    Font(R.font.freesentation_1thin, FontWeight.Thin),
    Font(R.font.freesentation_2extralight, FontWeight.ExtraLight),
    Font(R.font.freesentation_3light, FontWeight.Light),
    Font(R.font.freesentation_4regular, FontWeight.Normal),
    Font(R.font.freesentation_6semibold, FontWeight.SemiBold),
    Font(R.font.freesentation_7bold, FontWeight.Bold),
    Font(R.font.freesentation_8extrabold, FontWeight.ExtraBold),
    Font(R.font.freesentation_9black, FontWeight.Black),
)
val bebasNeue = FontFamily(
    Font(R.font.bebasneue_regular, FontWeight.Normal),
)

val mountainsOfChristmas = FontFamily(
    Font(R.font.mountains_of_christmas_bold, FontWeight.Bold),
    Font(R.font.mountains_of_christmas_regular, FontWeight.Normal)
)

val customTypography =  CustomTypography(
    buttonTimerSmall = TextStyle(fontFamily = pretendard, fontWeight = FontWeight.Light, fontSize = 30.sp),
    buttonTimerLarge = TextStyle(fontFamily = pretendard, fontWeight = FontWeight.Light, fontSize = 40.sp),
    buttonEditSmall = TextStyle(fontFamily = pretendard, fontWeight = FontWeight.SemiBold, fontSize = 14.sp),
    buttonEditLarge = TextStyle(fontFamily = pretendard, fontWeight = FontWeight.SemiBold, fontSize = 14.sp),
    clockTimeSmall = TextStyle(fontFamily = pretendard, fontWeight = FontWeight.Normal, fontSize = 37.sp),
    clockTimeLarge = TextStyle(fontFamily = pretendard, fontWeight = FontWeight.Normal, fontSize = 82.sp),
    digitalTimeSmall = TextStyle(fontFamily = pretendard, fontWeight = FontWeight.Normal, fontSize = 60.sp),
    digitalTimeLarge = TextStyle(fontFamily = pretendard, fontWeight = FontWeight.Normal, fontSize = 100.sp),
    deskTimeSmall = TextStyle(fontFamily = digitalMonoFont, fontSize = 76.sp),
    deskTimeLarge = TextStyle(fontFamily = digitalMonoFont, fontSize = 300.sp),
    editTitleSmall = TextStyle(fontFamily = pretendard, fontWeight = FontWeight.Bold, fontSize = 18.sp),
    editTitleLarge = TextStyle(fontFamily = pretendard, fontWeight = FontWeight.Bold, fontSize = 50.sp),
    selectorSelected = TextStyle(fontFamily = pretendard, fontWeight = FontWeight.SemiBold, fontSize = 13.sp),
    selectorUnSelected = TextStyle(fontFamily = pretendard, fontWeight = FontWeight.Normal, fontSize = 13.sp),
    textField = TextStyle(fontFamily = pretendard, fontWeight = FontWeight.Normal, fontSize = 13.sp),
    textPreview1 = TextStyle(fontFamily = pretendard, fontWeight = FontWeight.SemiBold, fontSize = 30.sp),
    textPreview2 = TextStyle(fontFamily = comfortaa, fontWeight = FontWeight.SemiBold, fontSize = 30.sp),
    textPreview3 = TextStyle(fontFamily = freesentation, fontWeight = FontWeight.SemiBold, fontSize = 30.sp),
    textPreview4 = TextStyle(fontFamily = bebasNeue, fontWeight = FontWeight.SemiBold, fontSize = 30.sp),
    textPreview5 = TextStyle(fontFamily = digitalMonoFont, fontWeight = FontWeight.SemiBold, fontSize = 30.sp),
    textPreview6 = TextStyle(fontFamily = mountainsOfChristmas, fontWeight = FontWeight.SemiBold, fontSize = 30.sp),
)