package com.example.autoconnect.data.settings

import com.example.autoconnect.domain.settings.SettingsRepository
import com.example.autoconnect.domain.settings.SettingsState
import kotlinx.coroutines.flow.Flow

class SettingsRepositoryImpl(
    private val dataSource: DataStoreSettingsDataSource
) : SettingsRepository {

    override fun observeSettings(): Flow<SettingsState> = dataSource.settingsFlow

    override suspend fun getSettings(): SettingsState = dataSource.getSettings()

    override suspend fun updateSettings(settings: SettingsState) = dataSource.updateSettings(settings)

    override suspend fun setDarkMode(enabled: Boolean) = dataSource.setDarkMode(enabled)

    override suspend fun setSimulationEnabled(enabled: Boolean) = dataSource.setSimulationEnabled(enabled)

    override suspend fun setVolume(volume: Float) = dataSource.setVolume(volume)

    override suspend fun setNotificationsEnabled(enabled: Boolean) = dataSource.setNotificationsEnabled(enabled)
}
