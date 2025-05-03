package com.example.coinquestfinancialxp.ui.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradient

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

// HyperLinks
// Light
val TextBlack = Color(0xFF2A2A2A)
val TextBlackHover = Color(0xFF8B8B8B)
val TextInactive = Color(0xFF696969)

// Dark
val TextWhite = Color(0xFFFFFFFF)
val TextWhiteHover = Color(0xFFE0E0E0)

// Buttons
val LightBlue = Color(0xFF00B2FF)
val DarkBlue = Color(0xFF0090CE)
val White = Color(0xFFFFFFFF)
val LightBlack = Color(0xFF151515)
val DarkBlack = Color(0xFF000000)
val GradientOrange = Brush.linearGradient(
    colors = listOf(
        Color(0xFFFFCB01),
        Color(0xFFFF9E01)
    ),
    start=Offset(0f, 50f),
    end=Offset(Float.POSITIVE_INFINITY, 50f)
)

val GradientDark = Brush.linearGradient(
    colors = listOf(
        Color(0xFF444552),
        Color(0xFF151515)
    ),
    start=Offset(0f, 50f),
    end=Offset(Float.POSITIVE_INFINITY, 50f)
)

// Text Boxes
val DarkGrey = Color(0xFF959595)
val MidGrey = Color(0xFFD3D3D3)
val LightGrey = Color(0xFFF1F1F1)

// Progress Bar
val TurkishBlue=Color(0xFF3FAFFF)

// Divider
val Divider1=Color(0xFFBDBDBD)
val Divider2=Color(0xFFE0E0E0)

// Page
val PageLight=Color(0xFFFFFFFF)
val PageDark=Color(0x0F262626)

// Text
val TextLight = Color(0xFFFFFFFF)
val TextDark = Color(0xFF494949)