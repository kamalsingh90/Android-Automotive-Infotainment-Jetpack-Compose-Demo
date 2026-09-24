package com.example.autoconnect.feature.vehicle

import com.example.autoconnect.domain.vehicle.VehicleState

data class VehicleSimulatorUiState(
    val liveVehicleState: VehicleState = VehicleState(),
    val draftState: VehicleState = VehicleState(),
    val isSimulating: Boolean = false,
    val validationError: String? = null,
    val successMessage: String? = null
)
