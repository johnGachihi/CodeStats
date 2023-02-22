package me.johngachihi.codestats.mobile.android.ui.accountSettings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.johngachihi.codestats.mobile.android.data.datastore.UsernamePrefs

class AccountSettingsViewModel(private val usernamePrefs: UsernamePrefs) : ViewModel() {
    val username = usernamePrefs.username

    fun saveUsername(username: String) {
        viewModelScope.launch {
            usernamePrefs.saveUsername(username)
        }
    }
}