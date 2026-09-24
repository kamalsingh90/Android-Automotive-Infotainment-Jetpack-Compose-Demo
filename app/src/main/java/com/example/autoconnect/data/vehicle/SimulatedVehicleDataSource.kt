package com.example.autoconnect.data.vehicle

import com.example.autoconnect.domain.vehicle.VehicleState
import com.example.autoconnect.framework.VehicleSignalManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min

/**
 * Vehicle data source implementing realistic vehicle simulation and
 * demonstrating Java/Kotlin interoperability with VehicleSignalManager.java.
 *
 * NOTE: Vehicle signals are simulated for demonstration purposes. This project does
 * not implement production vehicle control, ECU communication, CAN communication,
 * Vehicle HAL, or safety-critical automotive functionality.
 */
class SimulatedVehicleDataSource(
    private val signalManager: VehicleSignalManager = VehicleSignalManager(),
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default),
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : VehicleDataSource {

    private val _vehicleStateFlow = MutableStateFlow(
        VehicleState(
            speed = signalManager.speed,
            battery = signalManager.batteryLevel,
            temperature = signalManager.cabinTemperature,
            range = signalManager.estimatedRange,
            engineRunning = signalManager.isEngineRunning,
            vehicleReady = signalManager.isVehicleReady,
            driverDoorOpen = signalManager.isDriverDoorOpen,
            passengerDoorOpen = signalManager.isPassengerDoorOpen,
            parkingBrake = signalManager.isParkingBrakeEngaged
        )
    )

    private val _isSimulating = MutableStateFlow(false)
    private var simulationJob: Job? = null
    private var speedDirection = 1

    override fun observeVehicleState(): Flow<VehicleState> = _vehicleStateFlow.asStateFlow()

    override fun getCurrentVehicleState(): VehicleState = _vehicleStateFlow.value

    override suspend fun updateVehicleState(state: VehicleState) {
        // Sync to Java vehicle framework component
        signalManager.speed = state.speed
        signalManager.batteryLevel = state.battery
        signalManager.cabinTemperature = state.temperature
        signalManager.estimatedRange = state.range
        signalManager.isEngineRunning = state.engineRunning
        signalManager.isVehicleReady = state.vehicleReady
        signalManager.isDriverDoorOpen = state.driverDoorOpen
        signalManager.isPassengerDoorOpen = state.passengerDoorOpen
        signalManager.isParkingBrakeEngaged = state.parkingBrake

        _vehicleStateFlow.value = state
    }

    override fun startSimulation() {
        if (_isSimulating.value) return
        _isSimulating.value = true

        simulationJob?.cancel()
        simulationJob = scope.launch(dispatcher) {
            var tickCount = 0
            while (isActive) {
                delay(800L)
                tickCount++

                val current = _vehicleStateFlow.value

                if (current.engineRunning && current.vehicleReady && !current.parkingBrake) {
                    // Simulate natural dynamic driving speed
                    var newSpeed = current.speed
                    if (newSpeed >= 95) speedDirection = -1
                    if (newSpeed <= 35) speedDirection = 1
                    newSpeed = max(0, min(140, newSpeed + (speedDirection * (1 + (tickCount % 3)))))

                    // Battery drain simulation (1% every ~15 ticks)
                    var newBattery = current.battery
                    if (tickCount % 15 == 0 && newBattery > 5) {
                        newBattery -= 1
                    }

                    // Range dynamically tied to battery state
                    val newRange = (newBattery * 4.1).toInt()

                    // Temperature subtle fluctuation
                    val newTemp = if (tickCount % 8 == 0) {
                        if (current.temperature < 25) current.temperature + 1 else 23
                    } else {
                        current.temperature
                    }

                    val updated = current.copy(
                        speed = newSpeed,
                        battery = newBattery,
                        range = newRange,
                        temperature = newTemp
                    )
                    updateVehicleState(updated)
                } else if (current.speed > 0) {
                    // Natural deceleration if engine turned off or brake set
                    val decayedSpeed = max(0, current.speed - 5)
                    val updated = current.copy(speed = decayedSpeed)
                    updateVehicleState(updated)
                }
            }
        }
    }

    override fun stopSimulation() {
        _isSimulating.value = false
        simulationJob?.cancel()
        simulationJob = null
    }

    override fun isSimulating(): StateFlow<Boolean> = _isSimulating.asStateFlow()
}
