package me.johngachihi.codestats.mobile.android.ui

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import me.johngachihi.codestats.mobile.android.ui.accountSettings.AccountSettingsScreen
import me.johngachihi.codestats.mobile.android.ui.home.HomeScreen

@Composable
fun MainApp() {
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
                actions = {
                    appBarActions()
                    IconButton(onClick = { navController.navigate("settings") }) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Account settings"
                        )
                    }
                }
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
            composable("settings") {
                AccountSettingsScreen()
            }
        }
    }
}