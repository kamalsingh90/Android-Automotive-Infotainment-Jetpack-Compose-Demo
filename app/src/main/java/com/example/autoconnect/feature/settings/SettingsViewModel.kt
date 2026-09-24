package com.example.autoconnect.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.autoconnect.domain.settings.GetSettingsUseCase
import com.example.autoconnect.domain.settings.SettingsRepository
import com.example.autoconnect.domain.settings.SettingsState
import com.example.autoconnect.domain.settings.UpdateSettingsUseCase
import com.example.autoconnect.domain.vehicle.StartVehicleSimulationUseCase
import com.example.autoconnect.domain.vehicle.StopVehicleSimulationUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val getSettingsUseCase: GetSettingsUseCase,
    private val updateSettingsUseCase: UpdateSettingsUseCase,
    private val startVehicleSimulationUseCase: StartVehicleSimulationUseCase,
    private val stopVehicleSimulationUseCase: StopVehicleSimulationUseCase,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val settingsState: StateFlow<SettingsState> = getSettingsUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = SettingsState()
    )

    fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            updateSettingsUseCase.setDarkMode(enabled)
        }
    }

    fun setSimulationEnabled(enabled: Boolean) {
        viewModelScope.launch {
            updateSettingsUseCase.setSimulationEnabled(enabled)
            if (enabled) {
                startVehicleSimulationUseCase()
            } else {
                stopVehicleSimulationUseCase()
            }
        }
    }

    fun setVolume(volume: Float) {
        viewModelScope.launch {
            updateSettingsUseCase.setVolume(volume)
        }
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            updateSettingsUseCase.setNotificationsEnabled(enabled)
        }
    }

    class Factory(
        private val getSettingsUseCase: GetSettingsUseCase,
        private val updateSettingsUseCase: UpdateSettingsUseCase,
        private val startVehicleSimulationUseCase: StartVehicleSimulationUseCase,
        private val stopVehicleSimulationUseCase: StopVehicleSimulationUseCase,
        private val settingsRepository: SettingsRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SettingsViewModel(
                getSettingsUseCase,
                updateSettingsUseCase,
                startVehicleSimulationUseCase,
                stopVehicleSimulationUseCase,
                settingsRepository
            ) as T
        }
    }
}
