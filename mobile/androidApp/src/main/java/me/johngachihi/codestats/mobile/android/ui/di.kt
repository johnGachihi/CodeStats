package me.johngachihi.codestats.mobile.android.ui

import me.johngachihi.codestats.mobile.android.ui.accountSettings.AccountSettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel { AccountSettingsViewModel(get()) }
}