package com.example.autoconnect.domain.vehicle

import com.example.autoconnect.core.common.AppResult

/**
 * Validates and updates vehicle state through repository.
 */
class UpdateVehicleStateUseCase(
    private val repository: VehicleRepository,
    private val validator: ValidateVehicleStateUseCase = ValidateVehicleStateUseCase()
) {
    suspend operator fun invoke(state: VehicleState): AppResult<Unit> {
        val validation = validator(state)
        if (!validation.isValid) {
            val primaryMessage = validation.firstErrorOrNull ?: "Invalid vehicle state configuration."
            return AppResult.Error(primaryMessage)
        }
        return repository.updateVehicleState(state)
    }
}
