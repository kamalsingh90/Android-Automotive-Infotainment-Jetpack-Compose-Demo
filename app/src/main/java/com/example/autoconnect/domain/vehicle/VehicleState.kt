package com.example.autoconnect.domain.vehicle

/**
 * Immutable vehicle telemetry and status state.
 *
 * NOTE: Vehicle signals are simulated for demonstration purposes. This project does
 * not implement production vehicle control, ECU communication, CAN communication,
 * Vehicle HAL, or safety-critical automotive functionality.
 */
data class VehicleState(
    val speed: Int = 42,
    val battery: Int = 78,
    val temperature: Int = 24,
    val range: Int = 320,
    val engineRunning: Boolean = true,
    val vehicleReady: Boolean = true,
    val driverDoorOpen: Boolean = false,
    val passengerDoorOpen: Boolean = false,
    val parkingBrake: Boolean = false
)
