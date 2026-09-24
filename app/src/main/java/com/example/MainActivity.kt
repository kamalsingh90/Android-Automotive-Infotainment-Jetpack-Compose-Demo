package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.autoconnect.app.AutoConnectApplication
import com.example.autoconnect.core.navigation.Screen
import com.example.autoconnect.core.ui.components.AutomotiveBottomNavBar
import com.example.autoconnect.feature.dashboard.DashboardScreen
import com.example.autoconnect.feature.dashboard.DashboardViewModel
import com.example.autoconnect.feature.diagnostics.DiagnosticsScreen
import com.example.autoconnect.feature.diagnostics.DiagnosticsViewModel
import com.example.autoconnect.feature.media.MediaScreen
import com.example.autoconnect.feature.media.MediaViewModel
import com.example.autoconnect.feature.navigation.NavigationSimulatorScreen
import com.example.autoconnect.feature.navigation.NavigationViewModel
import com.example.autoconnect.feature.settings.SettingsScreen
import com.example.autoconnect.feature.settings.SettingsViewModel
import com.example.autoconnect.feature.vehicle.VehicleSimulatorScreen
import com.example.autoconnect.feature.vehicle.VehicleViewModel
import com.example.autoconnect.framework.LifecycleTracker
import com.example.autoconnect.framework.StartupPerformanceManager
import com.example.ui.theme.AutoConnectTheme

class MainActivity : ComponentActivity() {

    private val container by lazy {
        (application as AutoConnectApplication).appContainer
    }

    private val dashboardViewModel: DashboardViewModel by viewModels {
        DashboardViewModel.Factory(
            container.getVehicleStateUseCase,
            container.getMediaStateUseCase,
            container.networkMonitor,
            container.vehicleRepository,
            container.startVehicleSimulationUseCase,
            container.stopVehicleSimulationUseCase
        )
    }

    private val vehicleViewModel: VehicleViewModel by viewModels {
        VehicleViewModel.Factory(
            container.getVehicleStateUseCase,
            container.updateVehicleStateUseCase,
            container.validateVehicleStateUseCase,
            container.startVehicleSimulationUseCase,
            container.stopVehicleSimulationUseCase,
            container.vehicleRepository
        )
    }

    private val mediaViewModel: MediaViewModel by viewModels {
        MediaViewModel.Factory(
            container.getMediaStateUseCase,
            container.playMediaUseCase,
            container.pauseMediaUseCase,
            container.togglePlayPauseUseCase,
            container.skipMediaUseCase,
            container.seekMediaUseCase,
            container.setVolumeUseCase,
            container.mediaRepository
        )
    }

    private val navigationViewModel: NavigationViewModel by viewModels()

    private val settingsViewModel: SettingsViewModel by viewModels {
        SettingsViewModel.Factory(
            container.getSettingsUseCase,
            container.updateSettingsUseCase,
            container.startVehicleSimulationUseCase,
            container.stopVehicleSimulationUseCase,
            container.settingsRepository
        )
    }

    private val diagnosticsViewModel: DiagnosticsViewModel by viewModels {
        DiagnosticsViewModel.Factory(
            container.deviceInfoProvider,
            container.networkMonitor,
            container.vehicleRepository,
            container.mediaRepository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        StartupPerformanceManager.recordActivityCreateStart()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        LifecycleTracker.track(Lifecycle.Event.ON_CREATE, "MainActivity")
        StartupPerformanceManager.recordActivityCreateEnd()

        setContent {
            val settingsState by settingsViewModel.settingsState.collectAsStateWithLifecycle()

            AutoConnectTheme(darkTheme = settingsState.darkMode) {
                AutoConnectApp(
                    dashboardViewModel = dashboardViewModel,
                    vehicleViewModel = vehicleViewModel,
                    mediaViewModel = mediaViewModel,
                    navigationViewModel = navigationViewModel,
                    settingsViewModel = settingsViewModel,
                    diagnosticsViewModel = diagnosticsViewModel,
                    onFirstFrame = {
                        StartupPerformanceManager.recordFirstFrameDrawn(applicationContext)
                    }
                )
            }
        }
    }

    override fun onStart() {
        super.onStart()
        LifecycleTracker.track(Lifecycle.Event.ON_START, "MainActivity")
    }

    override fun onResume() {
        super.onResume()
        LifecycleTracker.track(Lifecycle.Event.ON_RESUME, "MainActivity")
    }

    override fun onPause() {
        super.onPause()
        LifecycleTracker.track(Lifecycle.Event.ON_PAUSE, "MainActivity")
    }

    override fun onStop() {
        super.onStop()
        LifecycleTracker.track(Lifecycle.Event.ON_STOP, "MainActivity")
    }

    override fun onDestroy() {
        super.onDestroy()
        LifecycleTracker.track(Lifecycle.Event.ON_DESTROY, "MainActivity")
    }
}

@Composable
fun AutoConnectApp(
    dashboardViewModel: DashboardViewModel,
    vehicleViewModel: VehicleViewModel,
    mediaViewModel: MediaViewModel,
    navigationViewModel: NavigationViewModel,
    settingsViewModel: SettingsViewModel,
    diagnosticsViewModel: DiagnosticsViewModel,
    onFirstFrame: () -> Unit
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Dashboard.route

    LaunchedEffect(Unit) {
        onFirstFrame()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            AutomotiveBottomNavBar(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    if (route != currentRoute) {
                        navController.navigate(route) {
                            popUpTo(Screen.Dashboard.route) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            NavHost(
                navController = navController,
                startDestination = Screen.Dashboard.route,
                modifier = Modifier.fillMaxSize()
            ) {
                composable(Screen.Dashboard.route) {
                    DashboardScreen(
                        viewModel = dashboardViewModel,
                        onNavigate = { route ->
                            navController.navigate(route) {
                                launchSingleTop = true
                            }
                        }
                    )
                }

                composable(Screen.Vehicle.route) {
                    VehicleSimulatorScreen(
                        viewModel = vehicleViewModel
                    )
                }

                composable(Screen.Media.route) {
                    MediaScreen(
                        viewModel = mediaViewModel
                    )
                }

                composable(Screen.Navigation.route) {
                    NavigationSimulatorScreen(
                        viewModel = navigationViewModel
                    )
                }

                composable(Screen.Settings.route) {
                    SettingsScreen(
                        viewModel = settingsViewModel
                    )
                }

                composable(Screen.Diagnostics.route) {
                    DiagnosticsScreen(
                        viewModel = diagnosticsViewModel
                    )
                }
            }
        }
    }
}
