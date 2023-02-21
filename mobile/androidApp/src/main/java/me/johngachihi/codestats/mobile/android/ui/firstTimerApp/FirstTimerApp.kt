package me.johngachihi.codestats.mobile.android.ui.firstTimerApp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun FirstTimerApp() {
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()

    suspend fun showSnackbar(message: String, actionLabel: String) =
        scaffoldState.snackbarHostState.showSnackbar(message, actionLabel)

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize()
    ) { contentPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.CreateUsername.route,
            modifier = Modifier.padding(contentPadding)
        ) {
            composable(Screen.CreateUsername.route) {
                CreateUsernameScreen(
                    navigateToEnterExistingUsernameScreen = {
                        navController.navigate(Screen.EnterExistingUsername.route)
                    },
                    showSnackbar = { message, actionLabel ->
                        showSnackbar(message, actionLabel)
                    }
                )
            }

            composable(Screen.EnterExistingUsername.route) {
                EnterExistingUsernameScreen(navigateToCreateUsername = {
                    navController.navigate(Screen.CreateUsername.route)
                })
            }
        }
    }
}

private sealed class Screen(val route: String) {
    object CreateUsername : Screen("create-username")
    object EnterExistingUsername : Screen("enter-existing-username")
}