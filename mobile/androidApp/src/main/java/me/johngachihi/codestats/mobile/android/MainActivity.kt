package me.johngachihi.codestats.mobile.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import me.johngachihi.codestats.mobile.android.data.datastore.firstUsePref
import me.johngachihi.codestats.mobile.android.ui.AppTheme
import me.johngachihi.codestats.mobile.android.ui.firstTimerApp.FirstTimerApp
import me.johngachihi.codestats.mobile.android.ui.UiState
import me.johngachihi.codestats.mobile.android.ui.home.HomeScreen

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
    val isFirstUse by getIsFirstUseState()

    when (isFirstUse) {
        is UiState.Loading -> Text(text = "Loading")
        is UiState.Error -> Text(text = "Error")
        is UiState.Success -> {
            if ((isFirstUse as UiState.Success<Boolean>).data) {
                FirstTimerApp()
            } else {
                TheApp()
            }
        }
    }
}


@Composable
fun getIsFirstUseState(): State<UiState<Boolean>> {
    val context = LocalContext.current
    val firstUse by context.firstUsePref.collectAsStateWithLifecycle(initialValue = null)

    return remember(firstUse) {
        derivedStateOf {
            when (firstUse) {
                null -> UiState.Loading
                else -> UiState.Success(firstUse!!)
            }
        }
    }
}

@Composable
fun TheApp() {
    val navController = rememberNavController()

    val (appBarActions, setAppBarActions) = remember {
        mutableStateOf<@Composable (RowScope.() -> Unit)>({ })
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Codestats") },
                elevation = 0.dp,
                contentColor = MaterialTheme.colors.primary,
                actions = appBarActions
            )
        }
    ) { contentPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(contentPadding)
        ) {
            composable("home") {
                HomeScreen(setAppBarActions = setAppBarActions)
            }
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
