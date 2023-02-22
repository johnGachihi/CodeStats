package me.johngachihi.codestats.mobile.android.ui.root

import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import me.johngachihi.codestats.mobile.android.ui.AppTheme
import me.johngachihi.codestats.mobile.android.ui.MainApp
import me.johngachihi.codestats.mobile.android.ui.UiState
import me.johngachihi.codestats.mobile.android.ui.firstTimerApp.FirstTimerApp
import org.koin.androidx.compose.koinViewModel

@Composable
fun Root(vm: RootViewModel = koinViewModel()) {
    val isFirstUse by vm.isFirstUse.collectAsUiStateWithLifecycle()

    AppTheme {
        when (isFirstUse) {
            // TODO
            is UiState.Loading -> Text(text = "Loading")
            // TODO
            is UiState.Error -> Text(text = "Error")
            is UiState.Success -> {
                if ((isFirstUse as UiState.Success<Boolean>).data) {
                    FirstTimerApp()
                } else {
                    MainApp()
                }
            }
        }
    }
}

@Composable
private fun <T : Any> Flow<T?>.collectAsUiStateWithLifecycle(): State<UiState<T>> {
    val state by this.collectAsStateWithLifecycle(initialValue = null)

    return remember(state) {
        derivedStateOf {
            when (state) {
                null -> UiState.Loading
                else -> UiState.Success(state!!)
            }
        }
    }
}

@Preview
@Composable
fun PreviewRoot() {
    Root()
}
