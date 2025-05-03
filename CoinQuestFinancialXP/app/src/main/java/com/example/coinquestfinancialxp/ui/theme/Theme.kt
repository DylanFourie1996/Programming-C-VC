package com.example.coinquestfinancialxp.ui.theme

import Font.AppTypography
import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /*
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

// Custom Color Data Class
data class CustomColors(
    val page : Color,
    val hyperlinkDefault: Color,
    val hyperlinkHover: Color,
    val hyperlinkInactive : Color,
    val ActionButtonTop : Color,
    val ActionButtonBot : Color,
    val ActionButtonText : Color,
    val ActionButton2Top : Color,
    val ActionButton2Bot : Color,
    val ActionButton2Text : Color,
    val ActionButton3Top : Brush,
    val ActionButton3Bot : Color,
    val ActionButton3Text : Color,
    val ActionButton4Top : Brush,
    val ActionButton4Bot : Color,
    val ActionButton4Text : Color,
    val TextBoxBorder : Color,
    val TextBoxBG : Color,
    val TextBoxText : Color,
    val ProgressBarColor : Color,
    val DividerColor1 : Color,
    val DividerColor2 : Color,
    val TextColor : Color,
    val PadColor : Color,
    val NavBarColor : Color,
    val InColor : Color
)

val LocalCustomColors = staticCompositionLocalOf {
    CustomColors(
        page = PageLight,
        hyperlinkDefault = TextBlack,
        hyperlinkHover = TextBlackHover,
        hyperlinkInactive = TextInactive,
        ActionButtonTop=LightBlue,
        ActionButtonBot=DarkBlue,
        ActionButtonText=White,
        ActionButton2Top=LightBlack,
        ActionButton2Bot=DarkBlack,
        ActionButton2Text=White,
        ActionButton3Top=GradientOrange,
        ActionButton3Bot=DarkGrey,
        ActionButton3Text=White,
        ActionButton4Top=GradientDark,
        ActionButton4Bot=DarkGrey,
        ActionButton4Text=White,
        TextBoxText=DarkGrey,
        TextBoxBorder=MidGrey,
        TextBoxBG=LightGrey,
        ProgressBarColor=TurkishBlue,
        DividerColor1=Divider1,
        DividerColor2=Divider2,
        TextColor=TextLight,
        PadColor = LightPad,
        NavBarColor = LightNav,
        InColor=LightIn
    )
}



@Composable
fun CoinQuestFinancialXPTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val customColors = if (!darkTheme) {
        CustomColors(
            page = PageLight,
            hyperlinkDefault = TextBlack,
            hyperlinkHover = TextBlackHover,
            hyperlinkInactive = TextInactive,
            ActionButtonTop=LightBlue,
            ActionButtonBot=DarkBlue,
            ActionButtonText=White,
            ActionButton2Top=LightBlack,
            ActionButton2Bot=DarkBlack,
            ActionButton2Text=White,
            ActionButton3Top=GradientOrange,
            ActionButton3Bot=DarkGrey,
            ActionButton3Text=White,
            ActionButton4Top=GradientDark,
            ActionButton4Bot=DarkGrey,
            ActionButton4Text=White,
            TextBoxText=DarkGrey,
            TextBoxBorder=MidGrey,
            TextBoxBG=LightGrey,
            ProgressBarColor=TurkishBlue,
            DividerColor1=Divider1,
            DividerColor2=Divider2,
            TextColor=TextDark,
            PadColor=LightPad,
            NavBarColor=LightNav,
            InColor=LightIn
        )
    } else {
        CustomColors(
            page = PageDark,
            hyperlinkDefault = TextBlack,
            hyperlinkHover = TextBlackHover,
            hyperlinkInactive = TextInactive,
            ActionButtonTop=LightBlue,
            ActionButtonBot=DarkBlue,
            ActionButtonText=White,
            ActionButton2Top=LightBlack,
            ActionButton2Bot=DarkBlack,
            ActionButton2Text=White,
            ActionButton3Top=GradientOrange,
            ActionButton3Bot=DarkGrey,
            ActionButton3Text=White,
            ActionButton4Top=GradientDark,
            ActionButton4Bot=DarkGrey,
            ActionButton4Text=White,
            TextBoxText=DarkGrey,
            TextBoxBorder=MidGrey,
            TextBoxBG=LightGrey,
            ProgressBarColor=TurkishBlue,
            DividerColor1=Divider1,
            DividerColor2=Divider2,
            TextColor=TextLight,
            PadColor=DarkPad,
            NavBarColor=DarkNav,
            InColor=DarkIn
        )
    }

    CompositionLocalProvider(LocalCustomColors provides customColors)
    {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            content = content
        )
    }
}