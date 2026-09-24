package com.example.autoconnect.core.navigation

/**
 * Navigation destinations for AutoConnect Automotive Infotainment.
 */
sealed class Screen(val route: String, val title: String) {
    data object Dashboard : Screen("dashboard", "Dashboard")
    data object Vehicle : Screen("vehicle", "Vehicle Simulator")
    data object Media : Screen("media", "Media Player")
    data object Navigation : Screen("navigation", "Navigation")
    data object Settings : Screen("settings", "Settings")
    data object Diagnostics : Screen("diagnostics", "System Diagnostics")

    companion object {
        val navItems = listOf(
            Dashboard,
            Vehicle,
            Media,
            Navigation,
            Settings,
            Diagnostics
        )
    }
}
