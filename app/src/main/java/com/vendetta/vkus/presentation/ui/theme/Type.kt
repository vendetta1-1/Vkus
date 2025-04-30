package com.vendetta.vkus.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp
import com.vendetta.vkus.R

val Typography = Typography(
    titleLarge = TextStyle(
        fontSize = 32.sp,
        fontFamily = FontFamily(Font(R.font.comfortaa)),
        fontWeight = FontWeight.Medium,
        localeList = LocaleList.current,
        fontStyle = FontStyle.Normal,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.Both,
            mode = LineHeightStyle.Mode.Fixed
        ),
        letterSpacing = 1.sp,
        lineHeight = 2.sp,
    ),
    bodyMedium = TextStyle(
        fontSize = 16.sp,
        fontFamily = FontFamily(Font(R.font.comfortaa)),
        fontWeight = FontWeight.Medium,
        localeList = LocaleList.current,
        fontStyle = FontStyle.Normal,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.Both,
            mode = LineHeightStyle.Mode.Fixed
        ),
        letterSpacing = 1.sp,
        lineHeight = 2.sp,
    ),
    bodySmall = TextStyle(
        fontSize = 12.sp,
        fontFamily = FontFamily(Font(R.font.comfortaa)),
        fontWeight = FontWeight.Medium,
        localeList = LocaleList.current,
        fontStyle = FontStyle.Normal,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.Both,
            mode = LineHeightStyle.Mode.Fixed
        ),
        letterSpacing = 1.sp,
        lineHeight = 2.sp,
    )
)