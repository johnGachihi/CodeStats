package me.johngachihi.codestats.mobile.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController

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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            AppTheme { Root() }
        }
    }
}

@Composable
fun Root() {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Codestats") },
                elevation = 0.dp,
                contentColor = MaterialTheme.colors.primary
            )
        }
    ) { contentPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(contentPadding)
        ) {
            composable("home") { HomeScreen() }
        }
    }
}

@Preview
@Composable
fun PreviewRoot() {
    AppTheme {
        Root()
    }
}
