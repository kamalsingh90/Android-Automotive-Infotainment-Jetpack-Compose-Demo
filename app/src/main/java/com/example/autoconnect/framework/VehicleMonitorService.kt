package com.example.autoconnect.framework

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.example.autoconnect.app.AutoConnectApplication
import com.example.autoconnect.domain.vehicle.VehicleState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Android Service monitoring vehicle telemetry signals, speed thresholds, and door states.
 * Follows modern Android Service lifecycle without unconstrained background drains.
 */
class VehicleMonitorService : Service() {

    companion object {
        private const val TAG = "VehicleMonitorService"
        const val ACTION_START_MONITORING = "com.example.autoconnect.action.START_MONITORING"
        const val ACTION_STOP_MONITORING = "com.example.autoconnect.action.STOP_MONITORING"

        private val _isServiceRunning = MutableStateFlow(false)
        val isServiceRunning: StateFlow<Boolean> = _isServiceRunning.asStateFlow()

        private val _alertMessage = MutableStateFlow<String?>(null)
        val alertMessage: StateFlow<String?> = _alertMessage.asStateFlow()
    }

    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.Default + serviceJob)
    private val binder = LocalBinder()

    inner class LocalBinder : Binder() {
        fun getService(): VehicleMonitorService = this@VehicleMonitorService
    }

    override fun onCreate() {
        super.onCreate()
        LifecycleTracker.track("SERVICE_CREATE", TAG)
        _isServiceRunning.value = true
        Log.d(TAG, "VehicleMonitorService created.")
        observeVehicleSignals()
    }

    private fun observeVehicleSignals() {
        val app = application as? AutoConnectApplication ?: return
        val vehicleRepo = app.appContainer.vehicleRepository

        serviceScope.launch {
            vehicleRepo.observeVehicleState().collect { state ->
                evaluateSafetyAlerts(state)
            }
        }
    }

    private fun evaluateSafetyAlerts(state: VehicleState) {
        val alert = when {
            state.speed > 0 && (state.driverDoorOpen || state.passengerDoorOpen) ->
                "CRITICAL: Door open while vehicle in motion!"
            state.speed > 130 ->
                "Overspeed warning: Speed exceeds 130 km/h."
            state.battery < 15 ->
                "Battery Warning: State of charge below 15%."
            state.parkingBrake && state.speed > 0 ->
                "Parking brake engaged while vehicle is moving!"
            else -> null
        }
        _alertMessage.value = alert
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_STOP_MONITORING -> {
                stopSelf()
                return START_NOT_STICKY
            }
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onDestroy() {
        super.onDestroy()
        LifecycleTracker.track("SERVICE_DESTROY", TAG)
        _isServiceRunning.value = false
        _alertMessage.value = null
        serviceScope.cancel()
        Log.d(TAG, "VehicleMonitorService destroyed and coroutines cancelled.")
    }
}
