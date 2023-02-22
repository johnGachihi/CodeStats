package me.johngachihi.codestats.mobile.android.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import org.koin.dsl.module

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

val prefsModule = module {
    single { get<Context>().dataStore }
    single { UsernamePref(get()) }
    single { IsFirstUsePref(get()) }
}