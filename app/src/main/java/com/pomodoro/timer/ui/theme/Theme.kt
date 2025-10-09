package com.pomodoro.timer.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp

val LocalCustomColors = compositionLocalOf<CustomColors> {
    error("No colors provided!")
}
val LocalCustomTypography = staticCompositionLocalOf {
    customTypography
}

@Composable
fun MyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    colors: CustomColors = if (darkTheme) {
        DarkCustomColors
    } else {
        LightCustomColors
    },
    background: BackgroundTheme = BackgroundTheme(
        color = colors.surface,
        tonalElevation = 2.dp,
    ),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalCustomColors provides colors,
        LocalBackgroundTheme provides background,
        LocalCustomTypography provides customTypography,
    ) {
        Box(
            modifier = Modifier
                .background(background.color)
                .semantics { testTagsAsResourceId = true },
        ) {
            content()
        }
    }
}

object CustomTheme {
    val colors: CustomColors
        @Composable
        @ReadOnlyComposable
        get() = LocalCustomColors.current
    val typography: CustomTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalCustomTypography.current
    val background: BackgroundTheme
        @Composable
        @ReadOnlyComposable
        get() = LocalBackgroundTheme.current
}



