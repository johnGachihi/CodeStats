package me.johngachihi.codestats.mobile.android.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.dsl.module

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

val prefsModule = module {
    single { get<Context>().dataStore }
    single { UsernamePrefs(get()) }
}

class UsernamePrefs(private val context: Context) {
    val username: Flow<String?>
        get() = context.usernamePref

    suspend fun saveUsername(username: String) {
        context.saveUsernamePref(username)
    }
}

val USERNAME = stringPreferencesKey("username")

val Context.usernamePref: Flow<String?>
    get() = dataStore.data
        .map { it[USERNAME] }
        .map { if (it.isNullOrBlank()) null else it }

suspend fun Context.saveUsernamePref(username: String) {
    dataStore.edit { it[USERNAME] = username }
}

val FIRST_USE = booleanPreferencesKey("first_use")

val Context.firstUsePref: Flow<Boolean>
    get() = dataStore.data.map { it[FIRST_USE] ?: true }

suspend fun Context.saveFirstUsePref(firstUse: Boolean) {
    dataStore.edit { it[FIRST_USE] = firstUse }
}