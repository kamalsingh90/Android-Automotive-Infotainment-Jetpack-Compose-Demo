package com.example.autoconnect.data.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.autoconnect.domain.settings.SettingsState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "autoconnect_settings")

/**
 * DataStore implementation for persisting application configuration across restarts.
 */
class DataStoreSettingsDataSource(private val context: Context) {

    private object PreferencesKeys {
        val DARK_MODE = booleanPreferencesKey("dark_mode")
        val SIMULATION_ENABLED = booleanPreferencesKey("simulation_enabled")
        val VOLUME = floatPreferencesKey("volume")
        val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
        val SPEED_UNITS = stringPreferencesKey("speed_units")
    }

    val settingsFlow: Flow<SettingsState> = context.settingsDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            SettingsState(
                darkMode = preferences[PreferencesKeys.DARK_MODE] ?: true,
                simulationEnabled = preferences[PreferencesKeys.SIMULATION_ENABLED] ?: false,
                volume = preferences[PreferencesKeys.VOLUME] ?: 0.7f,
                notificationsEnabled = preferences[PreferencesKeys.NOTIFICATIONS_ENABLED] ?: true,
                speedUnits = preferences[PreferencesKeys.SPEED_UNITS] ?: "km/h"
            )
        }

    suspend fun getSettings(): SettingsState {
        return settingsFlow.first()
    }

    suspend fun updateSettings(settings: SettingsState) {
        context.settingsDataStore.edit { preferences ->
            preferences[PreferencesKeys.DARK_MODE] = settings.darkMode
            preferences[PreferencesKeys.SIMULATION_ENABLED] = settings.simulationEnabled
            preferences[PreferencesKeys.VOLUME] = settings.volume
            preferences[PreferencesKeys.NOTIFICATIONS_ENABLED] = settings.notificationsEnabled
            preferences[PreferencesKeys.SPEED_UNITS] = settings.speedUnits
        }
    }

    suspend fun setDarkMode(enabled: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[PreferencesKeys.DARK_MODE] = enabled
        }
    }

    suspend fun setSimulationEnabled(enabled: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[PreferencesKeys.SIMULATION_ENABLED] = enabled
        }
    }

    suspend fun setVolume(volume: Float) {
        context.settingsDataStore.edit { preferences ->
            preferences[PreferencesKeys.VOLUME] = volume
        }
    }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[PreferencesKeys.NOTIFICATIONS_ENABLED] = enabled
        }
    }
}
