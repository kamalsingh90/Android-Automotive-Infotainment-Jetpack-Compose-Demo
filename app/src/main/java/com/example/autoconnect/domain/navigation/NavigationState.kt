package com.example.autoconnect.domain.navigation

/**
 * State of the simulated automotive navigation system.
 */
data class NavigationState(
    val destination: String = "Office",
    val distanceMeters: Int = 14200,
    val etaMinutes: Int = 18,
    val instruction: String = "In 400m, take exit 14B towards Tech Park",
    val nextManeuver: String = "Turn Right onto Innovation Way",
    val currentStreet: String = "Grand Boulevard",
    val isNavigating: Boolean = true
)
