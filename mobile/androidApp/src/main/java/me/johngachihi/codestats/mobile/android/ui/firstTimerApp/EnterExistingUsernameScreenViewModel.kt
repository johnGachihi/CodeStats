package me.johngachihi.codestats.mobile.android.ui.firstTimerApp

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.johngachihi.codestats.mobile.android.data.datastore.saveFirstUsePref
import me.johngachihi.codestats.mobile.android.data.datastore.saveUsernamePref
import me.johngachihi.codestats.mobile.android.ui.UiState

class EnterExistingUsernameScreenViewModel : ViewModel() {
    private val _saveUsernameState = mutableStateOf<UiState<Unit>?>(null)
    val saveUsernameState: State<UiState<Unit>?> = _saveUsernameState

    fun setUsername(
        username: String,
        context: Context // :(
    ) {
        _saveUsernameState.value = UiState.Loading
        viewModelScope.launch {
            try {
                context.saveUsernamePref(username)
                context.saveFirstUsePref(false)
                _saveUsernameState.value = UiState.Success(Unit)
            } catch (e: Exception) {
                _saveUsernameState.value = UiState.Error(e)
            }
        }
    }
}