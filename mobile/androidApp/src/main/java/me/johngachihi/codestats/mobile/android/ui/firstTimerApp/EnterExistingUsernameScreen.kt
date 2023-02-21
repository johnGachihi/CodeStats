package me.johngachihi.codestats.mobile.android.ui.firstTimerApp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.johngachihi.codestats.mobile.android.ui.AppTheme
import me.johngachihi.codestats.mobile.android.ui.UiState

@Composable
fun EnterExistingUsernameScreen(
    navigateToCreateUsername: () -> Unit,
    vm: EnterExistingUsernameScreenViewModel = EnterExistingUsernameScreenViewModel()
) {
    val (usernameInput, setUsernameInput) = remember { mutableStateOf("") }

    val saveUsernameState by remember { vm.saveUsernameState }

    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = "Welcome back",
            style = MaterialTheme.typography.h2,
            color = MaterialTheme.colors.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Enter your username",
            style = MaterialTheme.typography.h6,
        )

        Spacer(modifier = Modifier.height(56.dp))

        Column {
            OutlinedTextField(
                value = usernameInput,
                onValueChange = { setUsernameInput(it) },
                label = { Text(text = "Username") },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = { vm.setUsername(usernameInput, context) },
                modifier = Modifier.align(Alignment.End),
                enabled = usernameInput.isNotEmpty()
            ) {
                if (saveUsernameState is UiState.Loading) {
                    LinearProgressIndicator(modifier = Modifier.width(24.dp))
                } else {
                    Text(text = "Ok")
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            Row(modifier = Modifier.clickable { navigateToCreateUsername() }) {
                Text(
                    text = "Don't have a username?",
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onBackground.copy(alpha = 0.6f),
                )

                Text(
                    text = "Create one.",
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
fun EnterExistingUsernameScreenPreview() {
    AppTheme {
        EnterExistingUsernameScreen(navigateToCreateUsername = {})
    }
}