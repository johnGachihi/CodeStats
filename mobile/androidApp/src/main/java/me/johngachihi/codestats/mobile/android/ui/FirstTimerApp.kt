package me.johngachihi.codestats.mobile.android.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import me.johngachihi.codestats.mobile.android.data.datastore.saveFirstUsePref
import me.johngachihi.codestats.mobile.android.data.datastore.saveUsernamePref

@Composable
fun FirstTimerApp() {
    val navController = rememberNavController()

    Scaffold(modifier = Modifier.fillMaxSize()) { contentPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.CreateUsername.route,
            modifier = Modifier.padding(contentPadding)
        ) {
            composable(Screen.CreateUsername.route) {
                CreateUsernameScreen(navigateToEnterExistingUsernameScreen = {
                    navController.navigate(Screen.EnterExistingUsername.route)
                })
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

@Composable
fun CreateUsernameScreen(
    navigateToEnterExistingUsernameScreen: () -> Unit,
) {
    val (usernameInput, setUsernameInput) = remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = "Welcome",
            style = MaterialTheme.typography.h2,
            color = MaterialTheme.colors.primary,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Create a username",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(56.dp))

        Column {
            OutlinedTextField(
                value = usernameInput,
                onValueChange = { setUsernameInput(it) },
                label = { Text(text = "Username") },
            )

            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = {
                    scope.launch {
                        launch { context.saveUsernamePref(usernameInput) }
                        context.saveFirstUsePref(false)
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(text = "Ok")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.clickable { navigateToEnterExistingUsernameScreen() }) {
                Text(
                    text = "I have one already.",
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onBackground.copy(alpha = 0.6f),
                )

                Text(
                    text = "Set it",
                    style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.padding(start = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.clickable { }) {
                Text(
                    text = "Not now.",
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onBackground.copy(alpha = 0.6f),
                )

                Text(
                    text = "Skip",
                    style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.padding(start = 2.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun CreateUsernameScreenPreview() {
    AppTheme {
        CreateUsernameScreen(navigateToEnterExistingUsernameScreen = {})
    }
}

@Composable
fun EnterExistingUsernameScreen(
    navigateToCreateUsername: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "Enter existing username")
        Button(onClick = { navigateToCreateUsername() }) {
            Text(text = "I don't have one")
        }
    }
}