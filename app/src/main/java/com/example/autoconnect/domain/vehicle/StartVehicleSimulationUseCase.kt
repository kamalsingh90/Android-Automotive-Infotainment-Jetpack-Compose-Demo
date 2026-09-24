package com.example.autoconnect.domain.vehicle

/**
 * Use case to start automated realistic vehicle telemetry simulation.
 */
class StartVehicleSimulationUseCase(
    private val repository: VehicleRepository
) {
    operator fun invoke() {
        repository.startSimulation()
    }
}
