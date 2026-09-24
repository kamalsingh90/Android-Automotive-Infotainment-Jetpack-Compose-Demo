package com.example.autoconnect.data.vehicle

import com.example.autoconnect.domain.vehicle.VehicleState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * Data source abstraction for vehicle telemetry signals.
 */
interface VehicleDataSource {
    fun observeVehicleState(): Flow<VehicleState>
    fun getCurrentVehicleState(): VehicleState
    suspend fun updateVehicleState(state: VehicleState)
    fun startSimulation()
    fun stopSimulation()
    fun isSimulating(): StateFlow<Boolean>
}
