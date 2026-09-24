package com.example.autoconnect.feature.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ElectricCar
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.NoCrash
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.autoconnect.core.navigation.Screen
import com.example.autoconnect.core.ui.components.AutomotiveCard
import com.example.autoconnect.core.ui.components.AutomotiveTopBar
import com.example.autoconnect.core.ui.components.VehicleSpeedGauge
import com.example.ui.theme.AutoBatteryGreen
import com.example.ui.theme.AutoCardBorder
import com.example.ui.theme.AutoDangerRed
import com.example.ui.theme.AutoDarkBackground
import com.example.ui.theme.AutoDarkSurface
import com.example.ui.theme.AutoDarkSurfaceElevated
import com.example.ui.theme.AutoDarkSurfaceVariant
import com.example.ui.theme.AutoPrimaryCyan
import com.example.ui.theme.AutoSecondaryElectric
import com.example.ui.theme.AutoTextMuted
import com.example.ui.theme.AutoTextPrimary
import com.example.ui.theme.AutoTextSecondary
import com.example.ui.theme.AutoVehicleReadyGreen
import com.example.ui.theme.AutoWarningAmber

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val vehicle = uiState.vehicle

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AutoDarkBackground)
            .testTag("dashboard_screen")
    ) {
        AutomotiveTopBar(
            cabinTemp = vehicle.temperature,
            networkConnected = uiState.networkConnected,
            simulationActive = uiState.isSimulating
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Speed Gauge & Quick Telemetry
            item {
                AutomotiveCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("dashboard_gauge_card")
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        VehicleSpeedGauge(
                            speed = vehicle.speed,
                            maxSpeed = 240,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // High-Level Telemetry Badges
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            StatusBadge(
                                label = "BATTERY",
                                value = "${vehicle.battery}%",
                                icon = Icons.Default.BatteryChargingFull,
                                tint = if (vehicle.battery > 20) AutoBatteryGreen else AutoDangerRed,
                                testTag = "badge_battery"
                            )
                            StatusBadge(
                                label = "RANGE",
                                value = "${vehicle.range} km",
                                icon = Icons.Default.ElectricCar,
                                tint = AutoSecondaryElectric,
                                testTag = "badge_range"
                            )
                            StatusBadge(
                                label = "POWERTRAIN",
                                value = if (vehicle.vehicleReady) "READY" else "OFF",
                                icon = if (vehicle.vehicleReady) Icons.Default.CheckCircle else Icons.Default.PowerSettingsNew,
                                tint = if (vehicle.vehicleReady) AutoVehicleReadyGreen else AutoTextMuted,
                                testTag = "badge_powertrain"
                            )
                        }
                    }
                }
            }

            // Real-Time Vehicle Status Grid
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Powertrain & Driving State Card
                    AutomotiveCard(
                        modifier = Modifier
                            .weight(1f)
                            .testTag("dashboard_powertrain_card")
                    ) {
                        Text(
                            text = "POWERTRAIN",
                            style = MaterialTheme.typography.labelSmall,
                            color = AutoTextSecondary,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        IndicatorRow(
                            label = "Engine",
                            active = vehicle.engineRunning,
                            activeText = "ON",
                            inactiveText = "OFF",
                            activeColor = AutoPrimaryCyan,
                            testTag = "indicator_engine"
                        )
                        IndicatorRow(
                            label = "Vehicle Ready",
                            active = vehicle.vehicleReady,
                            activeText = "READY",
                            inactiveText = "STANDBY",
                            activeColor = AutoVehicleReadyGreen,
                            testTag = "indicator_ready"
                        )
                        IndicatorRow(
                            label = "Parking Brake",
                            active = vehicle.parkingBrake,
                            activeText = "ENGAGED",
                            inactiveText = "RELEASED",
                            activeColor = AutoWarningAmber,
                            testTag = "indicator_parking_brake"
                        )
                    }

                    // Doors & Security Card
                    AutomotiveCard(
                        modifier = Modifier
                            .weight(1f)
                            .testTag("dashboard_doors_card")
                    ) {
                        Text(
                            text = "CABIN & DOORS",
                            style = MaterialTheme.typography.labelSmall,
                            color = AutoTextSecondary,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        IndicatorRow(
                            label = "Driver Door",
                            active = vehicle.driverDoorOpen,
                            activeText = "OPEN",
                            inactiveText = "CLOSED",
                            activeColor = AutoDangerRed,
                            testTag = "indicator_driver_door"
                        )
                        IndicatorRow(
                            label = "Passenger Door",
                            active = vehicle.passengerDoorOpen,
                            activeText = "OPEN",
                            inactiveText = "CLOSED",
                            activeColor = AutoDangerRed,
                            testTag = "indicator_passenger_door"
                        )
                        IndicatorRow(
                            label = "Cabin Temp",
                            active = true,
                            activeText = "${vehicle.temperature}°C",
                            inactiveText = "--",
                            activeColor = AutoSecondaryElectric,
                            testTag = "indicator_temperature"
                        )
                    }
                }
            }

            // Media Quick Pill (Now Playing)
            item {
                AutomotiveCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigate(Screen.Media.route) }
                        .testTag("dashboard_media_pill")
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(AutoDarkSurfaceVariant),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MusicNote,
                                    contentDescription = "Media Playing",
                                    tint = AutoPrimaryCyan,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = if (uiState.media.title.isNotEmpty()) uiState.media.title else "No Media",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = AutoTextPrimary,
                                    maxLines = 1
                                )
                                Text(
                                    text = if (uiState.media.artist.isNotEmpty()) uiState.media.artist else "Tap to open Media Player",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = AutoTextSecondary,
                                    maxLines = 1
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (uiState.media.isPlaying) AutoBatteryGreen.copy(alpha = 0.2f) else AutoDarkSurfaceVariant)
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = if (uiState.media.isPlaying) "PLAYING" else "PAUSED",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (uiState.media.isPlaying) AutoBatteryGreen else AutoTextMuted,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // Quick App Launchers Grid
            item {
                Text(
                    text = "INFOTAINMENT APPLICATIONS",
                    style = MaterialTheme.typography.labelSmall,
                    color = AutoTextSecondary,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickLaunchButton(
                        title = "Vehicle",
                        icon = Icons.Default.DirectionsCar,
                        testTag = "btn_quick_vehicle",
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(Screen.Vehicle.route) }
                    )
                    QuickLaunchButton(
                        title = "Media",
                        icon = Icons.Default.MusicNote,
                        testTag = "btn_quick_media",
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(Screen.Media.route) }
                    )
                    QuickLaunchButton(
                        title = "Navigation",
                        icon = Icons.Default.Navigation,
                        testTag = "btn_quick_navigation",
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(Screen.Navigation.route) }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickLaunchButton(
                        title = "Settings",
                        icon = Icons.Default.Settings,
                        testTag = "btn_quick_settings",
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(Screen.Settings.route) }
                    )
                    QuickLaunchButton(
                        title = "Diagnostics",
                        icon = Icons.Default.Warning,
                        testTag = "btn_quick_diagnostics",
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(Screen.Diagnostics.route) }
                    )
                    QuickLaunchButton(
                        title = if (uiState.isSimulating) "Stop Sim" else "Live Sim",
                        icon = if (uiState.isSimulating) Icons.Default.Pause else Icons.Default.PlayArrow,
                        testTag = "btn_quick_simulation",
                        tint = if (uiState.isSimulating) AutoWarningAmber else AutoPrimaryCyan,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.toggleSimulation() }
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusBadge(
    label: String,
    value: String,
    icon: ImageVector,
    tint: Color,
    testTag: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.testTag(testTag)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = tint,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = AutoTextMuted
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = AutoTextPrimary
        )
    }
}

@Composable
private fun IndicatorRow(
    label: String,
    active: Boolean,
    activeText: String,
    inactiveText: String,
    activeColor: Color,
    testTag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .testTag(testTag),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = AutoTextSecondary
        )
        Text(
            text = if (active) activeText else inactiveText,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = if (active) activeColor else AutoTextMuted
        )
    }
}

@Composable
private fun QuickLaunchButton(
    title: String,
    icon: ImageVector,
    testTag: String,
    modifier: Modifier = Modifier,
    tint: Color = AutoPrimaryCyan,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = AutoDarkSurfaceVariant),
        contentPadding = PaddingValues(vertical = 12.dp, horizontal = 8.dp),
        modifier = modifier
            .height(56.dp)
            .testTag(testTag)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = tint,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = AutoTextPrimary,
                maxLines = 1
            )
        }
    }
}
