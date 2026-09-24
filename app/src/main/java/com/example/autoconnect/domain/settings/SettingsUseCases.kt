package com.example.autoconnect.domain.settings

import kotlinx.coroutines.flow.Flow

class GetSettingsUseCase(private val repository: SettingsRepository) {
    operator fun invoke(): Flow<SettingsState> = repository.observeSettings()
}

class UpdateSettingsUseCase(private val repository: SettingsRepository) {
    suspend operator fun invoke(settings: SettingsState) = repository.updateSettings(settings)
    suspend fun setDarkMode(enabled: Boolean) = repository.setDarkMode(enabled)
    suspend fun setSimulationEnabled(enabled: Boolean) = repository.setSimulationEnabled(enabled)
    suspend fun setVolume(volume: Float) = repository.setVolume(volume)
    suspend fun setNotificationsEnabled(enabled: Boolean) = repository.setNotificationsEnabled(enabled)
}
