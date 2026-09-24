package com.example.autoconnect.domain.vehicle

import kotlinx.coroutines.flow.Flow

/**
 * Use case to observe vehicle telemetry updates.
 */
class GetVehicleStateUseCase(
    private val repository: VehicleRepository
) {
    operator fun invoke(): Flow<VehicleState> = repository.observeVehicleState()
}
