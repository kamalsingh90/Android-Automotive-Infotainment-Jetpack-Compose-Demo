package com.example.autoconnect.feature.diagnostics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.autoconnect.domain.media.MediaRepository
import com.example.autoconnect.domain.media.MediaState
import com.example.autoconnect.domain.vehicle.VehicleRepository
import com.example.autoconnect.domain.vehicle.VehicleState
import com.example.autoconnect.framework.DeviceInfo
import com.example.autoconnect.framework.DeviceInfoProvider
import com.example.autoconnect.framework.LifecycleEvent
import com.example.autoconnect.framework.LifecycleTracker
import com.example.autoconnect.framework.NetworkMonitor
import com.example.autoconnect.framework.StartupMilestones
import com.example.autoconnect.framework.StartupPerformanceManager
import com.example.autoconnect.framework.VehicleMonitorService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class DiagnosticsUiState(
    val deviceInfo: DeviceInfo,
    val startupMilestones: StartupMilestones,
    val lifecycleEvents: List<LifecycleEvent>,
    val currentLifecycle: String,
    val vehicleServiceConnected: Boolean,
    val mediaServiceRunning: Boolean,
    val networkConnected: Boolean,
    val vehicleState: VehicleState,
    val isSimulating: Boolean
)

class DiagnosticsViewModel(
    private val deviceInfoProvider: DeviceInfoProvider,
    private val networkMonitor: NetworkMonitor,
    private val vehicleRepository: VehicleRepository,
    private val mediaRepository: MediaRepository
) : ViewModel() {

    private val baseTelemetryFlow = combine(
        networkMonitor.isConnected,
        vehicleRepository.observeVehicleState(),
        vehicleRepository.isSimulating(),
        mediaRepository.observeMediaState()
    ) { network, vehicle, simulating, media ->
        CombinedTelemetry(network, vehicle, simulating, media)
    }

    val uiState: StateFlow<DiagnosticsUiState> = combine(
        baseTelemetryFlow,
        VehicleMonitorService.isServiceRunning,
        LifecycleTracker.eventsFlow
    ) { telemetry, serviceRunning, events ->
        DiagnosticsUiState(
            deviceInfo = deviceInfoProvider.getDeviceInfo(),
            startupMilestones = StartupPerformanceManager.milestones.value,
            lifecycleEvents = events,
            currentLifecycle = LifecycleTracker.currentEvent.value,
            vehicleServiceConnected = serviceRunning,
            mediaServiceRunning = telemetry.media.isPlaying,
            networkConnected = telemetry.network,
            vehicleState = telemetry.vehicle,
            isSimulating = telemetry.simulating
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = DiagnosticsUiState(
            deviceInfo = deviceInfoProvider.getDeviceInfo(),
            startupMilestones = StartupPerformanceManager.milestones.value,
            lifecycleEvents = LifecycleTracker.getRecentEvents(),
            currentLifecycle = LifecycleTracker.currentEvent.value,
            vehicleServiceConnected = VehicleMonitorService.isServiceRunning.value,
            mediaServiceRunning = mediaRepository.getCurrentMediaState().isPlaying,
            networkConnected = networkMonitor.isCurrentlyConnected(),
            vehicleState = vehicleRepository.getCurrentVehicleState(),
            isSimulating = false
        )
    )

    private data class CombinedTelemetry(
        val network: Boolean,
        val vehicle: VehicleState,
        val simulating: Boolean,
        val media: MediaState
    )

    class Factory(
        private val deviceInfoProvider: DeviceInfoProvider,
        private val networkMonitor: NetworkMonitor,
        private val vehicleRepository: VehicleRepository,
        private val mediaRepository: MediaRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DiagnosticsViewModel(
                deviceInfoProvider,
                networkMonitor,
                vehicleRepository,
                mediaRepository
            ) as T
        }
    }
}
