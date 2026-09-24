package com.example.autoconnect.domain.vehicle

/**
 * Result of vehicle state business rule validation.
 */
data class VehicleValidationResult(
    val isValid: Boolean,
    val errors: List<String> = emptyList()
) {
    val firstErrorOrNull: String? get() = errors.firstOrNull()
}

/**
 * Validates domain business rules for vehicle state transitions.
 *
 * Rules:
 * 1. speed >= 0
 * 2. 0 <= battery <= 100
 * 3. range >= 0
 * 4. If parking brake is ON: engineRunning cannot be true
 * 5. If driver door is open: vehicleReady cannot be true
 * 6. If battery < 10: vehicleReady cannot be true
 * 7. Vehicle cannot become READY if driverDoorOpen == true OR parkingBrake == true OR battery < 10
 */
class ValidateVehicleStateUseCase {

    operator fun invoke(state: VehicleState): VehicleValidationResult {
        val errors = mutableListOf<String>()

        if (state.speed < 0) {
            errors.add("Speed cannot be negative (${state.speed} km/h).")
        }

        if (state.battery < 0 || state.battery > 100) {
            errors.add("Battery state of charge must be between 0% and 100% (${state.battery}%).")
        }

        if (state.range < 0) {
            errors.add("Estimated range cannot be negative (${state.range} km).")
        }

        if (state.parkingBrake && state.engineRunning) {
            errors.add("Engine cannot be running while parking brake is engaged.")
        }

        if (state.driverDoorOpen && state.vehicleReady) {
            errors.add("Vehicle cannot be READY while the driver door is open.")
        }

        if (state.battery < 10 && state.vehicleReady) {
            errors.add("Battery critically low (<10%). Vehicle cannot be in READY mode.")
        }

        if (state.vehicleReady && (state.driverDoorOpen || state.parkingBrake || state.battery < 10)) {
            // Rule 7 composite check
            if (!errors.any { it.contains("READY") }) {
                errors.add("Vehicle cannot become READY if driver door is open, parking brake is set, or battery is under 10%.")
            }
        }

        return VehicleValidationResult(
            isValid = errors.isEmpty(),
            errors = errors
        )
    }
}
