package me.johngachihi.codestats.mobile.android.ui.firstTimerApp

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.johngachihi.codestats.mobile.android.data.datastore.saveFirstUsePref
import me.johngachihi.codestats.mobile.android.data.datastore.saveUsernamePref
import me.johngachihi.codestats.mobile.android.data.net.isUsernameAvailable
import me.johngachihi.codestats.mobile.android.ui.UiState

class CreateUsernameScreenViewModel : ViewModel() {
    private val _isUsernameAvailable = mutableStateOf<UiState<Boolean>?>(null)
    val isUsernameAvailable: State<UiState<Boolean>?> = _isUsernameAvailable

    // TODO: Better name. What does this function do?
    fun onSubmit(
        username: String,
        context: Context // Makes me :(
    ) = viewModelScope.launch {
        _isUsernameAvailable.value = UiState.Loading
        try {
            val isAvailable = isUsernameAvailable(username)
            _isUsernameAvailable.value = UiState.Success(isAvailable)
            if (isAvailable) {
                context.saveUsernamePref(username)
                context.saveFirstUsePref(firstUse = false)
            }
        } catch (e: Exception) {
            _isUsernameAvailable.value = UiState.Error(e)
        }
    }

    fun clearIsUsernameAvailableState() {
        _isUsernameAvailable.value = null
    }
}