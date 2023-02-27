package me.johngachihi.codestats.mobile.android.ui

import me.johngachihi.codestats.mobile.android.ui.accountSettings.AccountSettingsViewModel
import me.johngachihi.codestats.mobile.android.ui.firstTimerApp.CreateUsernameScreenViewModel
import me.johngachihi.codestats.mobile.android.ui.firstTimerApp.EnterExistingUsernameScreenViewModel
import me.johngachihi.codestats.mobile.android.ui.home.HomeViewModel
import me.johngachihi.codestats.mobile.android.ui.root.RootViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel { RootViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { AccountSettingsViewModel(get()) }
    viewModel { CreateUsernameScreenViewModel(get(), get()) }
    viewModel { EnterExistingUsernameScreenViewModel(get(), get()) }
}