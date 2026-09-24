package com.example.autoconnect.domain.vehicle

import com.example.autoconnect.core.common.AppResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * Repository interface defining vehicle data contract.
 */
interface VehicleRepository {
    fun observeVehicleState(): Flow<VehicleState>
    fun getCurrentVehicleState(): VehicleState
    suspend fun updateVehicleState(state: VehicleState): AppResult<Unit>
    fun startSimulation()
    fun stopSimulation()
    fun isSimulating(): StateFlow<Boolean>
}
