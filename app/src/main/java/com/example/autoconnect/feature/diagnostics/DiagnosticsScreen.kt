package com.example.autoconnect.feature.diagnostics

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.autoconnect.core.ui.components.AutomotiveCard
import com.example.autoconnect.core.ui.components.AutomotiveTopBar
import com.example.ui.theme.AutoBatteryGreen
import com.example.ui.theme.AutoCardBorder
import com.example.ui.theme.AutoDangerRed
import com.example.ui.theme.AutoDarkBackground
import com.example.ui.theme.AutoDarkSurfaceElevated
import com.example.ui.theme.AutoDarkSurfaceVariant
import com.example.ui.theme.AutoPrimaryCyan
import com.example.ui.theme.AutoSecondaryElectric
import com.example.ui.theme.AutoTextMuted
import com.example.ui.theme.AutoTextPrimary
import com.example.ui.theme.AutoTextSecondary
import com.example.ui.theme.AutoWarningAmber

@Composable
fun DiagnosticsScreen(
    viewModel: DiagnosticsViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val dev = state.deviceInfo
    val perf = state.startupMilestones

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AutoDarkBackground)
            .testTag("diagnostics_screen")
    ) {
        AutomotiveTopBar()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Hardware & Services Status Table
            item {
                AutomotiveCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("diagnostics_services_card")
                ) {
                    Text(
                        text = "AUTOMOTIVE SYSTEM TELEMETRY",
                        style = MaterialTheme.typography.labelSmall,
                        color = AutoTextSecondary,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    DiagRow(
                        label = "Vehicle Service",
                        value = if (state.vehicleServiceConnected) "CONNECTED" else "STOPPED",
                        valueColor = if (state.vehicleServiceConnected) AutoBatteryGreen else AutoDangerRed,
                        testTag = "diag_vehicle_service"
                    )
                    DiagRow(
                        label = "Media Service",
                        value = if (state.mediaServiceRunning) "RUNNING" else "IDLE",
                        valueColor = if (state.mediaServiceRunning) AutoPrimaryCyan else AutoTextMuted,
                        testTag = "diag_media_service"
                    )
                    DiagRow(
                        label = "Network",
                        value = if (state.networkConnected) "CONNECTED" else "DISCONNECTED",
                        valueColor = if (state.networkConnected) AutoBatteryGreen else AutoDangerRed,
                        testTag = "diag_network"
                    )
                    DiagRow(
                        label = "Device Battery",
                        value = "${dev.batteryPercent}% ${if (dev.isCharging) "(Charging)" else ""}",
                        valueColor = AutoBatteryGreen,
                        testTag = "diag_battery"
                    )
                    DiagRow(
                        label = "Lifecycle State",
                        value = state.currentLifecycle,
                        valueColor = AutoSecondaryElectric,
                        testTag = "diag_lifecycle"
                    )
                    DiagRow(
                        label = "Startup Measurement",
                        value = "${perf.totalStartupDurationMs} ms",
                        valueColor = AutoPrimaryCyan,
                        testTag = "diag_startup"
                    )
                    DiagRow(
                        label = "Android Version",
                        value = "${dev.androidVersion} (SDK ${dev.sdkVersion})",
                        valueColor = AutoTextPrimary,
                        testTag = "diag_android"
                    )
                    DiagRow(
                        label = "Manufacturer",
                        value = dev.manufacturer,
                        valueColor = AutoTextPrimary,
                        testTag = "diag_manufacturer"
                    )
                    DiagRow(
                        label = "Model",
                        value = dev.model,
                        valueColor = AutoTextPrimary,
                        testTag = "diag_model"
                    )
                    DiagRow(
                        label = "Memory (RAM)",
                        value = "${dev.availableMemoryMb} MB free / ${dev.totalMemoryMb} MB total",
                        valueColor = AutoTextPrimary,
                        testTag = "diag_memory"
                    )
                    DiagRow(
                        label = "Simulation Mode",
                        value = if (state.isSimulating) "ON" else "OFF",
                        valueColor = if (state.isSimulating) AutoWarningAmber else AutoTextMuted,
                        testTag = "diag_simulation"
                    )
                    DiagRow(
                        label = "Architecture",
                        value = "MVVM + Clean Architecture",
                        valueColor = AutoPrimaryCyan,
                        testTag = "diag_architecture"
                    )
                }
            }

            // Cold Startup Performance Breakdown
            item {
                AutomotiveCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("diagnostics_perf_card")
                ) {
                    Text(
                        text = "COLD STARTUP PERFORMANCE MILESTONES",
                        style = MaterialTheme.typography.labelSmall,
                        color = AutoTextSecondary,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "App Init → Activity Created: ${
                            if (perf.activityCreateStartTimeMs > 0 && perf.appCreateStartTimeMs > 0)
                                "${perf.activityCreateStartTimeMs - perf.appCreateStartTimeMs} ms"
                            else "N/A"
                        }",
                        style = MaterialTheme.typography.bodyMedium,
                        color = AutoTextPrimary
                    )
                    Text(
                        text = "Activity Init → First Frame Drawn: ${
                            if (perf.firstFrameTimeMs > 0 && perf.activityCreateStartTimeMs > 0)
                                "${perf.firstFrameTimeMs - perf.activityCreateStartTimeMs} ms"
                            else "N/A"
                        }",
                        style = MaterialTheme.typography.bodyMedium,
                        color = AutoTextPrimary
                    )
                    Text(
                        text = "Total App Cold Launch to Interactive: ${perf.totalStartupDurationMs} ms",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = AutoPrimaryCyan
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Note: Measures application process startup. Android Automotive OS platform boot includes hypervisor, early EVCS, kernel, and CarService initialization.",
                        style = MaterialTheme.typography.bodySmall,
                        color = AutoTextMuted,
                        lineHeight = 16.sp
                    )
                }
            }

            // Android Lifecycle Log (Latest 50 events)
            item {
                Text(
                    text = "LIFECYCLE EVENT AUDIT LOG (LATEST ${state.lifecycleEvents.size})",
                    style = MaterialTheme.typography.labelSmall,
                    color = AutoTextSecondary,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                )
            }

            items(state.lifecycleEvents, key = { it.id }) { event ->
                AutomotiveCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("lifecycle_event_item_${event.id}"),
                    containerColor = AutoDarkSurfaceVariant
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = event.event,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = AutoPrimaryCyan
                            )
                            Text(
                                text = event.component,
                                style = MaterialTheme.typography.bodySmall,
                                color = AutoTextSecondary
                            )
                        }
                        Text(
                            text = event.formattedTime,
                            style = MaterialTheme.typography.labelSmall,
                            fontFamily = FontFamily.Monospace,
                            color = AutoTextMuted
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DiagRow(
    label: String,
    value: String,
    valueColor: Color,
    testTag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
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
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = valueColor
        )
    }
}
