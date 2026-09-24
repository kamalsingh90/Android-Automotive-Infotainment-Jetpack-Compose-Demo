package com.example.autoconnect.domain.settings

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun observeSettings(): Flow<SettingsState>
    suspend fun getSettings(): SettingsState
    suspend fun updateSettings(settings: SettingsState)
    suspend fun setDarkMode(enabled: Boolean)
    suspend fun setSimulationEnabled(enabled: Boolean)
    suspend fun setVolume(volume: Float)
    suspend fun setNotificationsEnabled(enabled: Boolean)
}
