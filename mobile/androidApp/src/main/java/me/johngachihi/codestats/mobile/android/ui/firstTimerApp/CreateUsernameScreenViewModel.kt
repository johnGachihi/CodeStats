package me.johngachihi.codestats.mobile.android.ui.firstTimerApp

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.johngachihi.codestats.mobile.android.data.datastore.IsFirstUsePref
import me.johngachihi.codestats.mobile.android.data.datastore.UsernamePref
import me.johngachihi.codestats.mobile.android.data.net.isUsernameAvailable
import me.johngachihi.codestats.mobile.android.ui.UiState

class CreateUsernameScreenViewModel(
    private val usernamePref: UsernamePref,
    private val isFirstUsePref: IsFirstUsePref
) : ViewModel() {
    private val _isUsernameAvailable = mutableStateOf<UiState<Boolean>?>(null)
    val isUsernameAvailable: State<UiState<Boolean>?> = _isUsernameAvailable

    // TODO: Better name. What does this function do?
    fun createUsername(username: String) = viewModelScope.launch {
        _isUsernameAvailable.value = UiState.Loading
        try {
            val isAvailable = isUsernameAvailable(username)
            _isUsernameAvailable.value = UiState.Success(isAvailable)
            if (isAvailable) {
                usernamePref.saveUsername(username)
                isFirstUsePref.saveIsFirstUse(firstUse = false)
            }
        } catch (e: Exception) {
            _isUsernameAvailable.value = UiState.Error(e)
        }
    }

    fun clearIsUsernameAvailableState() {
        _isUsernameAvailable.value = null
    }
}