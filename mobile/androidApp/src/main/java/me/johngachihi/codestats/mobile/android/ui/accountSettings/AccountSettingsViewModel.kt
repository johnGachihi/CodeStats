package me.johngachihi.codestats.mobile.android.ui.accountSettings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.johngachihi.codestats.mobile.android.data.datastore.UsernamePref

class AccountSettingsViewModel(private val usernamePrefs: UsernamePref) : ViewModel() {
    val username = usernamePrefs.username
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )

    fun saveUsername(username: String) {
        viewModelScope.launch {
            usernamePrefs.saveUsername(username)
        }
    }
}