package com.example.autoconnect.feature.navigation

import androidx.lifecycle.ViewModel
import com.example.autoconnect.domain.navigation.NavigationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NavigationViewModel : ViewModel() {

    private val presetRoutes = mapOf(
        "Office" to NavigationState(
            destination = "Office",
            distanceMeters = 14200,
            etaMinutes = 18,
            instruction = "In 400m, take exit 14B towards Tech Park",
            nextManeuver = "Turn Right onto Innovation Way",
            currentStreet = "Grand Boulevard",
            isNavigating = true
        ),
        "Home" to NavigationState(
            destination = "Home",
            distanceMeters = 8700,
            etaMinutes = 12,
            instruction = "Continue straight on Maple Parkway for 2.5 km",
            nextManeuver = "Keep left at the fork towards Oak Ridge",
            currentStreet = "Maple Parkway",
            isNavigating = true
        ),
        "Airport" to NavigationState(
            destination = "Airport",
            distanceMeters = 29500,
            etaMinutes = 27,
            instruction = "Follow signs for Terminal 2 Departure Expressway",
            nextManeuver = "Merge onto Interstate 80 East",
            currentStreet = "Skyway Toll Road",
            isNavigating = true
        )
    )

    private val _navigationState = MutableStateFlow(presetRoutes["Office"]!!)
    val navigationState: StateFlow<NavigationState> = _navigationState.asStateFlow()

    fun selectDestination(destination: String) {
        presetRoutes[destination]?.let { state ->
            _navigationState.value = state
        }
    }
}
