package me.johngachihi.codestats.mobile.android.ui.accountSettings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.johngachihi.codestats.mobile.android.ui.components.UsernameInputDialog
import org.koin.androidx.compose.koinViewModel

@Composable
fun AccountSettingsScreen(
    vm: AccountSettingsViewModel = koinViewModel()
) {
    val (isUsernameDialogOpen, setIsUsernameDialogOpen) =
        remember { mutableStateOf(false) }

    val username by vm.username.collectAsStateWithLifecycle(initialValue = null)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp)
    ) {
        Card(modifier = Modifier
            .fillMaxWidth()
            .clickable { setIsUsernameDialogOpen(true) }
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Username",
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = username ?: "--",
                    style = MaterialTheme.typography.h6
                )
            }
        }
    }

    UsernameInputDialog(
        isOpen = isUsernameDialogOpen,
        onDismiss = { setIsUsernameDialogOpen(false) },
        username = username ?: "",
        onUsernameChange = { vm.saveUsername(it) },
    )
}