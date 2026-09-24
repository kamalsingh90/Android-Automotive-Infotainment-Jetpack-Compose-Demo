package com.example.autoconnect.domain.settings

/**
 * Persisted application settings state.
 */
data class SettingsState(
    val darkMode: Boolean = true,
    val simulationEnabled: Boolean = false,
    val volume: Float = 0.7f,
    val notificationsEnabled: Boolean = true,
    val speedUnits: String = "km/h"
)
