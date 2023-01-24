package me.johngachihi.codestats.mobile.android.ui

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import me.johngachihi.codestats.mobile.android.R

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    val colors = darkColors(
        primary = Color(0xFF52DEE5),
        primaryVariant = Color(0xFF44B4B9),
        background = Color(0xFF00241B),
        surface = Color(0xFF00241B)
    )

    val splineSansMono = FontFamily(
        Font(R.font.spline_sans_mono_regular),
        Font(R.font.spline_sans_mono_medium, FontWeight.Medium)
    )

    val typography = Typography(
        body1 = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        ),
        h6 = TextStyle(
            fontFamily = splineSansMono,
            fontWeight = FontWeight.Black,
            fontSize = 20.sp,
            letterSpacing = 0.15.sp
        )
    )
    val shapes = Shapes(
        small = RoundedCornerShape(4.dp),
        medium = RoundedCornerShape(4.dp),
        large = RoundedCornerShape(0.dp)
    )

    val systemUIController = rememberSystemUiController()
    DisposableEffect(systemUIController) {
        systemUIController.setSystemBarsColor(color = colors.background)
        onDispose {}
    }

    MaterialTheme(
        colors = colors,
        typography = typography,
        shapes = shapes,
        content = content
    )
}