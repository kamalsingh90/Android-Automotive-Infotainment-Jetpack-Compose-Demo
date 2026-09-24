package com.example.autoconnect.feature.dashboard

import com.example.autoconnect.domain.media.MediaState
import com.example.autoconnect.domain.vehicle.VehicleState

data class DashboardUiState(
    val vehicle: VehicleState = VehicleState(),
    val media: MediaState = MediaState(),
    val networkConnected: Boolean = true,
    val isSimulating: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)
