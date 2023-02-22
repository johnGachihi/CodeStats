package me.johngachihi.codestats.mobile.android.ui.root

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import me.johngachihi.codestats.mobile.android.data.datastore.IsFirstUsePref

class RootViewModel(isFirstUsePref: IsFirstUsePref) : ViewModel() {
    val isFirstUse = isFirstUsePref.isFirstUse
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )
}