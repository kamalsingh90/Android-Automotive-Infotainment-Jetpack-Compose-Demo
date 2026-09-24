package com.example.autoconnect.feature.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.autoconnect.domain.media.GetMediaStateUseCase
import com.example.autoconnect.domain.vehicle.GetVehicleStateUseCase
import com.example.autoconnect.domain.vehicle.StartVehicleSimulationUseCase
import com.example.autoconnect.domain.vehicle.StopVehicleSimulationUseCase
import com.example.autoconnect.domain.vehicle.VehicleRepository
import com.example.autoconnect.framework.NetworkMonitor
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel for Automotive Dashboard observing vehicle telemetry, media playback,
 * network connectivity, and simulation status.
 */
class DashboardViewModel(
    private val getVehicleStateUseCase: GetVehicleStateUseCase,
    private val getMediaStateUseCase: GetMediaStateUseCase,
    private val networkMonitor: NetworkMonitor,
    private val vehicleRepository: VehicleRepository,
    private val startVehicleSimulationUseCase: StartVehicleSimulationUseCase,
    private val stopVehicleSimulationUseCase: StopVehicleSimulationUseCase
) : ViewModel() {

    val uiState: StateFlow<DashboardUiState> = combine(
        getVehicleStateUseCase(),
        getMediaStateUseCase(),
        networkMonitor.isConnected,
        vehicleRepository.isSimulating()
    ) { vehicle, media, networkConnected, isSimulating ->
        DashboardUiState(
            vehicle = vehicle,
            media = media,
            networkConnected = networkConnected,
            isSimulating = isSimulating,
            isLoading = false,
            error = null
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = DashboardUiState(
            vehicle = vehicleRepository.getCurrentVehicleState(),
            networkConnected = networkMonitor.isCurrentlyConnected(),
            isLoading = false
        )
    )

    fun toggleSimulation() {
        if (uiState.value.isSimulating) {
            stopVehicleSimulationUseCase()
        } else {
            startVehicleSimulationUseCase()
        }
    }

    class Factory(
        private val getVehicleStateUseCase: GetVehicleStateUseCase,
        private val getMediaStateUseCase: GetMediaStateUseCase,
        private val networkMonitor: NetworkMonitor,
        private val vehicleRepository: VehicleRepository,
        private val startVehicleSimulationUseCase: StartVehicleSimulationUseCase,
        private val stopVehicleSimulationUseCase: StopVehicleSimulationUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DashboardViewModel(
                getVehicleStateUseCase,
                getMediaStateUseCase,
                networkMonitor,
                vehicleRepository,
                startVehicleSimulationUseCase,
                stopVehicleSimulationUseCase
            ) as T
        }
    }
}
