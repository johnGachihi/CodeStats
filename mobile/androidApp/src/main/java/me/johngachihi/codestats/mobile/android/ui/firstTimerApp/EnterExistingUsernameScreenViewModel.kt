package me.johngachihi.codestats.mobile.android.ui.firstTimerApp

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.johngachihi.codestats.mobile.android.data.datastore.IsFirstUsePref
import me.johngachihi.codestats.mobile.android.data.datastore.UsernamePref
import me.johngachihi.codestats.mobile.android.ui.UiState

class EnterExistingUsernameScreenViewModel(
    private val usernamePref: UsernamePref,
    private val isFirstUsePref: IsFirstUsePref
) : ViewModel() {
    private val _saveUsernameState = mutableStateOf<UiState<Unit>?>(null)
    val saveUsernameState: State<UiState<Unit>?> = _saveUsernameState

    fun setUsername(username: String) {
        _saveUsernameState.value = UiState.Loading
        viewModelScope.launch {
            try {
                usernamePref.saveUsername(username)
                isFirstUsePref.saveIsFirstUse(false)
                _saveUsernameState.value = UiState.Success(Unit)
            } catch (e: Exception) {
                _saveUsernameState.value = UiState.Error(e)
            }
        }
    }
}