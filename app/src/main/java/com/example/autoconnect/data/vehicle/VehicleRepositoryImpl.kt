package com.example.autoconnect.data.vehicle

import com.example.autoconnect.core.common.AppResult
import com.example.autoconnect.domain.vehicle.VehicleRepository
import com.example.autoconnect.domain.vehicle.VehicleState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * Implementation of VehicleRepository coordinating vehicle data sources.
 */
class VehicleRepositoryImpl(
    private val dataSource: VehicleDataSource
) : VehicleRepository {

    override fun observeVehicleState(): Flow<VehicleState> = dataSource.observeVehicleState()

    override fun getCurrentVehicleState(): VehicleState = dataSource.getCurrentVehicleState()

    override suspend fun updateVehicleState(state: VehicleState): AppResult<Unit> {
        return try {
            dataSource.updateVehicleState(state)
            AppResult.Success(Unit)
        } catch (e: Exception) {
            AppResult.Error(e.message ?: "Failed to update vehicle state", e)
        }
    }

    override fun startSimulation() = dataSource.startSimulation()

    override fun stopSimulation() = dataSource.stopSimulation()

    override fun isSimulating(): StateFlow<Boolean> = dataSource.isSimulating()
}
