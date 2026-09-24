package com.example.autoconnect.feature.vehicle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.autoconnect.core.common.AppResult
import com.example.autoconnect.domain.vehicle.GetVehicleStateUseCase
import com.example.autoconnect.domain.vehicle.StartVehicleSimulationUseCase
import com.example.autoconnect.domain.vehicle.StopVehicleSimulationUseCase
import com.example.autoconnect.domain.vehicle.UpdateVehicleStateUseCase
import com.example.autoconnect.domain.vehicle.ValidateVehicleStateUseCase
import com.example.autoconnect.domain.vehicle.VehicleRepository
import com.example.autoconnect.domain.vehicle.VehicleState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel managing interactive vehicle state simulation, validation, and real-time synchronization.
 */
class VehicleViewModel(
    private val getVehicleStateUseCase: GetVehicleStateUseCase,
    private val updateVehicleStateUseCase: UpdateVehicleStateUseCase,
    private val validateVehicleStateUseCase: ValidateVehicleStateUseCase,
    private val startVehicleSimulationUseCase: StartVehicleSimulationUseCase,
    private val stopVehicleSimulationUseCase: StopVehicleSimulationUseCase,
    private val vehicleRepository: VehicleRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(VehicleSimulatorUiState())
    val uiState: StateFlow<VehicleSimulatorUiState> = _uiState.asStateFlow()

    init {
        // Observe live vehicle state from repository
        viewModelScope.launch {
            getVehicleStateUseCase().collect { liveState ->
                _uiState.update { current ->
                    // If draft is identical or unedited, sync draft as well
                    val updatedDraft = if (current.draftState == current.liveVehicleState) {
                        liveState
                    } else {
                        current.draftState
                    }
                    current.copy(
                        liveVehicleState = liveState,
                        draftState = updatedDraft
                    )
                }
            }
        }

        // Observe simulation status
        viewModelScope.launch {
            vehicleRepository.isSimulating().collect { isSim ->
                _uiState.update { it.copy(isSimulating = isSim) }
            }
        }
    }

    fun updateDraftSpeed(speed: Int) {
        _uiState.update { it.copy(draftState = it.draftState.copy(speed = speed), validationError = null) }
    }

    fun updateDraftBattery(battery: Int) {
        val estimatedRange = (battery * 4.1).toInt()
        _uiState.update {
            it.copy(
                draftState = it.draftState.copy(battery = battery, range = estimatedRange),
                validationError = null
            )
        }
    }

    fun updateDraftTemperature(temp: Int) {
        _uiState.update { it.copy(draftState = it.draftState.copy(temperature = temp), validationError = null) }
    }

    fun updateDraftRange(range: Int) {
        _uiState.update { it.copy(draftState = it.draftState.copy(range = range), validationError = null) }
    }

    fun updateDraftEngine(running: Boolean) {
        _uiState.update { it.copy(draftState = it.draftState.copy(engineRunning = running), validationError = null) }
    }

    fun updateDraftReady(ready: Boolean) {
        _uiState.update { it.copy(draftState = it.draftState.copy(vehicleReady = ready), validationError = null) }
    }

    fun updateDraftDriverDoor(open: Boolean) {
        _uiState.update { it.copy(draftState = it.draftState.copy(driverDoorOpen = open), validationError = null) }
    }

    fun updateDraftPassengerDoor(open: Boolean) {
        _uiState.update { it.copy(draftState = it.draftState.copy(passengerDoorOpen = open), validationError = null) }
    }

    fun updateDraftParkingBrake(engaged: Boolean) {
        _uiState.update { it.copy(draftState = it.draftState.copy(parkingBrake = engaged), validationError = null) }
    }

    fun applyDraftState() {
        val draft = _uiState.value.draftState
        val validation = validateVehicleStateUseCase(draft)

        if (!validation.isValid) {
            val errorMessage = validation.firstErrorOrNull ?: "Invalid vehicle configuration."
            _uiState.update { it.copy(validationError = errorMessage, successMessage = null) }
            return
        }

        viewModelScope.launch {
            when (val result = updateVehicleStateUseCase(draft)) {
                is AppResult.Success -> {
                    _uiState.update {
                        it.copy(
                            validationError = null,
                            successMessage = "Vehicle state applied successfully."
                        )
                    }
                }
                is AppResult.Error -> {
                    _uiState.update {
                        it.copy(
                            validationError = result.message,
                            successMessage = null
                        )
                    }
                }
                AppResult.Loading -> Unit
            }
        }
    }

    fun resetDefaults() {
        val defaultState = VehicleState(
            speed = 42,
            battery = 78,
            temperature = 24,
            range = 320,
            engineRunning = true,
            vehicleReady = true,
            driverDoorOpen = false,
            passengerDoorOpen = false,
            parkingBrake = false
        )
        _uiState.update {
            it.copy(
                draftState = defaultState,
                validationError = null,
                successMessage = "Restored default demonstration parameters."
            )
        }
        viewModelScope.launch {
            updateVehicleStateUseCase(defaultState)
        }
    }

    fun toggleSimulation() {
        if (_uiState.value.isSimulating) {
            stopVehicleSimulationUseCase()
        } else {
            startVehicleSimulationUseCase()
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(validationError = null, successMessage = null) }
    }

    class Factory(
        private val getVehicleStateUseCase: GetVehicleStateUseCase,
        private val updateVehicleStateUseCase: UpdateVehicleStateUseCase,
        private val validateVehicleStateUseCase: ValidateVehicleStateUseCase,
        private val startVehicleSimulationUseCase: StartVehicleSimulationUseCase,
        private val stopVehicleSimulationUseCase: StopVehicleSimulationUseCase,
        private val vehicleRepository: VehicleRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return VehicleViewModel(
                getVehicleStateUseCase,
                updateVehicleStateUseCase,
                validateVehicleStateUseCase,
                startVehicleSimulationUseCase,
                stopVehicleSimulationUseCase,
                vehicleRepository
            ) as T
        }
    }
}
