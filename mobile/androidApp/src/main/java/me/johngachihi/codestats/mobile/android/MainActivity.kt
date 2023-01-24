package me.johngachihi.codestats.mobile.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import me.johngachihi.codestats.mobile.android.ui.AppTheme
import me.johngachihi.codestats.mobile.android.ui.HomeScreen

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
