package com.example.autoconnect.feature.vehicle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.autoconnect.core.ui.components.AutomotiveCard
import com.example.autoconnect.core.ui.components.AutomotiveTopBar
import com.example.ui.theme.AutoBatteryGreen
import com.example.ui.theme.AutoDangerRed
import com.example.ui.theme.AutoDarkBackground
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
fun VehicleSimulatorScreen(
    viewModel: VehicleViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val draft = uiState.draftState

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AutoDarkBackground)
            .testTag("vehicle_simulator_screen")
    ) {
        AutomotiveTopBar(
            cabinTemp = uiState.liveVehicleState.temperature,
            simulationActive = uiState.isSimulating
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Automotive Simulation Disclaimer Banner
            item {
                AutomotiveCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("simulator_disclaimer_card"),
                    containerColor = AutoDarkSurfaceVariant
                ) {
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Automotive Note",
                            tint = AutoPrimaryCyan,
                            modifier = Modifier
                                .size(24.dp)
                                .padding(top = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "AUTOMOTIVE SIGNAL SIMULATOR",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = AutoPrimaryCyan,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Vehicle signals are simulated for demonstration purposes. This project does not implement production vehicle control, ECU communication, CAN communication, Vehicle HAL, or safety-critical automotive functionality.",
                                style = MaterialTheme.typography.bodySmall,
                                color = AutoTextSecondary,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }

            // Validation Error Alert Banner
            if (uiState.validationError != null) {
                item {
                    AutomotiveCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("validation_error_card"),
                        containerColor = AutoDangerRed.copy(alpha = 0.15f),
                        borderColor = AutoDangerRed
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Validation Alert",
                                tint = AutoDangerRed,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = uiState.validationError ?: "",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = AutoDangerRed,
                                modifier = Modifier.testTag("validation_error_text")
                            )
                        }
                    }
                }
            }

            // Success Confirmation Banner
            if (uiState.successMessage != null && uiState.validationError == null) {
                item {
                    AutomotiveCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("success_message_card"),
                        containerColor = AutoBatteryGreen.copy(alpha = 0.15f),
                        borderColor = AutoBatteryGreen
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Success",
                                tint = AutoBatteryGreen,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = uiState.successMessage ?: "",
                                style = MaterialTheme.typography.bodyMedium,
                                color = AutoBatteryGreen,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Telemetry Sliders Card
            item {
                AutomotiveCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("simulator_sliders_card")
                ) {
                    Text(
                        text = "ANALOG TELEMETRY CONTROLS",
                        style = MaterialTheme.typography.labelSmall,
                        color = AutoTextSecondary,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    // Speed Slider
                    SliderField(
                        label = "Vehicle Speed",
                        valueText = "${draft.speed} km/h",
                        value = draft.speed.toFloat(),
                        valueRange = 0f..240f,
                        steps = 47,
                        sliderTestTag = "slider_speed",
                        onValueChange = { viewModel.updateDraftSpeed(it.toInt()) }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Battery Slider
                    SliderField(
                        label = "Battery State of Charge",
                        valueText = "${draft.battery}%",
                        value = draft.battery.toFloat(),
                        valueRange = 0f..100f,
                        steps = 99,
                        sliderTestTag = "slider_battery",
                        onValueChange = { viewModel.updateDraftBattery(it.toInt()) }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Range Slider
                    SliderField(
                        label = "Estimated Range",
                        valueText = "${draft.range} km",
                        value = draft.range.toFloat(),
                        valueRange = 0f..600f,
                        steps = 59,
                        sliderTestTag = "slider_range",
                        onValueChange = { viewModel.updateDraftRange(it.toInt()) }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Temperature Slider
                    SliderField(
                        label = "Cabin Temperature",
                        valueText = "${draft.temperature}°C",
                        value = draft.temperature.toFloat(),
                        valueRange = 15f..35f,
                        steps = 19,
                        sliderTestTag = "slider_temperature",
                        onValueChange = { viewModel.updateDraftTemperature(it.toInt()) }
                    )
                }
            }

            // Binary States & Switches Card
            item {
                AutomotiveCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("simulator_switches_card")
                ) {
                    Text(
                        text = "POWERTRAIN & ACCESS STATES",
                        style = MaterialTheme.typography.labelSmall,
                        color = AutoTextSecondary,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    SwitchRow(
                        label = "Engine Running",
                        checked = draft.engineRunning,
                        testTag = "switch_engine",
                        onCheckedChange = { viewModel.updateDraftEngine(it) }
                    )

                    SwitchRow(
                        label = "Vehicle Ready Mode",
                        checked = draft.vehicleReady,
                        testTag = "switch_ready",
                        onCheckedChange = { viewModel.updateDraftReady(it) }
                    )

                    SwitchRow(
                        label = "Parking Brake Engaged",
                        checked = draft.parkingBrake,
                        testTag = "switch_parking_brake",
                        onCheckedChange = { viewModel.updateDraftParkingBrake(it) }
                    )

                    SwitchRow(
                        label = "Driver Door Open",
                        checked = draft.driverDoorOpen,
                        testTag = "switch_driver_door",
                        onCheckedChange = { viewModel.updateDraftDriverDoor(it) }
                    )

                    SwitchRow(
                        label = "Passenger Door Open",
                        checked = draft.passengerDoorOpen,
                        testTag = "switch_passenger_door",
                        onCheckedChange = { viewModel.updateDraftPassengerDoor(it) }
                    )
                }
            }

            // Action Buttons (Apply, Reset, Simulation Toggle)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { viewModel.applyDraftState() },
                        colors = ButtonDefaults.buttonColors(containerColor = AutoPrimaryCyan),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1.2f)
                            .height(54.dp)
                            .testTag("btn_apply_simulator")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Apply Signals",
                            tint = AutoDarkBackground,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "APPLY",
                            fontWeight = FontWeight.Bold,
                            color = AutoDarkBackground,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    OutlinedButton(
                        onClick = { viewModel.resetDefaults() },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(54.dp)
                            .testTag("btn_reset_simulator")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Reset Defaults",
                            tint = AutoTextPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "RESET",
                            fontWeight = FontWeight.SemiBold,
                            color = AutoTextPrimary
                        )
                    }

                    OutlinedButton(
                        onClick = { viewModel.toggleSimulation() },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(54.dp)
                            .testTag("btn_toggle_sim_mode")
                    ) {
                        Icon(
                            imageVector = if (uiState.isSimulating) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = "Toggle Simulation",
                            tint = if (uiState.isSimulating) AutoWarningAmber else AutoPrimaryCyan,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (uiState.isSimulating) "STOP" else "AUTO",
                            fontWeight = FontWeight.SemiBold,
                            color = if (uiState.isSimulating) AutoWarningAmber else AutoPrimaryCyan
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SliderField(
    label: String,
    valueText: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
    sliderTestTag: String,
    onValueChange: (Float) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = AutoTextSecondary
            )
            Text(
                text = valueText,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = AutoTextPrimary
            )
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            steps = steps,
            colors = SliderDefaults.colors(
                thumbColor = AutoPrimaryCyan,
                activeTrackColor = AutoPrimaryCyan,
                inactiveTrackColor = AutoDarkSurfaceElevated
            ),
            modifier = Modifier.testTag(sliderTestTag)
        )
    }
}

@Composable
private fun SwitchRow(
    label: String,
    checked: Boolean,
    testTag: String,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = AutoTextPrimary
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = AutoDarkBackground,
                checkedTrackColor = AutoPrimaryCyan,
                uncheckedThumbColor = AutoTextMuted,
                uncheckedTrackColor = AutoDarkSurfaceElevated
            ),
            modifier = Modifier.testTag(testTag)
        )
    }
}
