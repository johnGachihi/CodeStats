package me.johngachihi.codestats.mobile.android.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val USERNAME = stringPreferencesKey("username")

class UsernamePref(private val dataStore: DataStore<Preferences>) {
    val username: Flow<String?>
        get() = dataStore.data
            .map { it[USERNAME] }
            .map { if (it.isNullOrBlank()) null else it }

    suspend fun saveUsername(username: String) {
        dataStore.edit { it[USERNAME] = username }
    }
}

private val FIRST_USE = booleanPreferencesKey("first_use")

class IsFirstUsePref(private val dataStore: DataStore<Preferences>) {
    val isFirstUse: Flow<Boolean>
        get() = dataStore.data.map { it[FIRST_USE] ?: true }

    suspend fun saveIsFirstUse(firstUse: Boolean) {
        dataStore.edit { it[FIRST_USE] = firstUse }
    }
}