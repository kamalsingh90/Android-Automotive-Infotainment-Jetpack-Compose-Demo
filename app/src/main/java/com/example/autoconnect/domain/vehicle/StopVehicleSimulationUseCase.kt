package com.example.autoconnect.domain.vehicle

/**
 * Use case to pause/stop automated vehicle simulation.
 */
class StopVehicleSimulationUseCase(
    private val repository: VehicleRepository
) {
    operator fun invoke() {
        repository.stopSimulation()
    }
}
