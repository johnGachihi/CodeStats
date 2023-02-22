package me.johngachihi.codestats.mobile.android.ui.firstTimerApp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.johngachihi.codestats.mobile.android.ui.AppTheme
import me.johngachihi.codestats.mobile.android.ui.UiState
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreateUsernameScreen(
    navigateToEnterExistingUsernameScreen: () -> Unit,
    showSnackbar: suspend (String, String) -> SnackbarResult = { _, _ -> SnackbarResult.Dismissed },
    vm: CreateUsernameScreenViewModel = koinViewModel()
) {
    val (usernameInput, setUsernameInput) = remember { mutableStateOf("") }

    val isUsernameAvailable by remember { vm.isUsernameAvailable }

    LaunchedEffect(isUsernameAvailable) {
        if (isUsernameAvailable is UiState.Error) {
            when (showSnackbar("Error experienced", "Retry")) {
                SnackbarResult.ActionPerformed -> vm.createUsername(usernameInput)
                else -> {}
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = "Welcome",
            style = MaterialTheme.typography.h2,
            color = MaterialTheme.colors.primary,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Create a username",
            style = MaterialTheme.typography.h6,
        )

        Spacer(modifier = Modifier.height(56.dp))

        Column {
            OutlinedTextField(
                value = usernameInput,
                onValueChange = {
                    setUsernameInput(it)
                    vm.clearIsUsernameAvailableState()
                },
                label = { Text(text = "Username") },
                isError = if (isUsernameAvailable is UiState.Success)
                    !(isUsernameAvailable as UiState.Success<Boolean>).data
                else
                    false,
                singleLine = true
            )

            AnimatedVisibility(
                visible = isUsernameAvailable is UiState.Success &&
                        !(isUsernameAvailable as UiState.Success<Boolean>).data,
            ) {
                Text(
                    text = "This one's taken. Try another.",
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.error,
                    modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = { vm.createUsername(usernameInput) },
                modifier = Modifier.align(Alignment.End),
                enabled = isUsernameAvailable !is UiState.Loading && usernameInput.isNotBlank()
            ) {
                if (isUsernameAvailable is UiState.Loading) {
                    LinearProgressIndicator(modifier = Modifier.width(24.dp))
                } else {
                    Text(text = "Ok")
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            Row(
                modifier = Modifier.clickable {
                    navigateToEnterExistingUsernameScreen()
                }
            ) {
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
