package me.johngachihi.codestats.mobile.android.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import me.johngachihi.codestats.mobile.android.ui.AppTheme

@Composable
fun UsernameInputDialog(
    isOpen: Boolean,
    onDismiss: () -> Unit,
    username: String,
    onUsernameChange: (String) -> Unit
) {
    val (input, setInput) = remember { mutableStateOf(username) }

    if (isOpen)
        Dialog(onDismissRequest = { }) {
            Surface {
                Column {
                    Box(modifier = Modifier.padding(horizontal = 24.dp)) {
                        Column {
                            Text(
                                text = "Enter your Codestats username",
                                style = MaterialTheme.typography.body1,
                                modifier = Modifier.paddingFromBaseline(top = 38.dp)
                            )

                            OutlinedTextField(
                                value = input,
                                onValueChange = { setInput(it) },
                                label = { Text("Username") },
                                textStyle = MaterialTheme.typography.body1,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
                    ) {
                        TextButton(onClick = { onDismiss() }) {
                            Text("Cancel")
                        }

                        TextButton(onClick = {
                            onUsernameChange(input)
                            onDismiss()
                        }) {
                            Text("OK")
                        }
                    }
                }
            }
        }
}

@Preview
@Composable
fun DialogPreview() {
    AppTheme {
        UsernameInputDialog(isOpen = true, onDismiss = { }, username = "", onUsernameChange = { })
    }
}