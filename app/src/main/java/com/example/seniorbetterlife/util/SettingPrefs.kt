package com.example.seniorbetterlife.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingPrefs(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
            name = "preferences")
        private val isRebootedKey = booleanPreferencesKey("isReboot")

    }

    val getIsReboot: Flow<Boolean>
    get() = context.dataStore.data.map{
        it[isRebootedKey] ?: false
    }

    suspend fun setIsReboot(value: Boolean) {
        context.dataStore.edit { it[isRebootedKey] = value }
    }

}