package com.example.autoconnect.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material.icons.filled.SignalCellularConnectedNoInternet0Bar
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AutoBatteryGreen
import com.example.ui.theme.AutoCardBorder
import com.example.ui.theme.AutoDangerRed
import com.example.ui.theme.AutoDarkSurface
import com.example.ui.theme.AutoDarkSurfaceVariant
import com.example.ui.theme.AutoPrimaryCyan
import com.example.ui.theme.AutoTextPrimary
import com.example.ui.theme.AutoTextSecondary
import com.example.ui.theme.AutoWarningAmber
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AutomotiveTopBar(
    cabinTemp: Int = 24,
    networkConnected: Boolean = true,
    simulationActive: Boolean = false,
    modifier: Modifier = Modifier
) {
    var currentTime by remember {
        mutableStateOf(SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()))
    }

    LaunchedEffect(Unit) {
        while (isActive) {
            currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
            delay(10000L)
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(AutoDarkSurface)
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .testTag("automotive_top_bar"),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left Branding
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(AutoPrimaryCyan)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "AUTOCONNECT",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                color = AutoTextPrimary
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "OS DEMO",
                style = MaterialTheme.typography.labelSmall,
                color = AutoPrimaryCyan,
                modifier = Modifier
                    .background(AutoDarkSurfaceVariant, RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }

        // Center / Right Info Row
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Temperature
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.testTag("top_bar_temp")
            ) {
                Icon(
                    imageVector = Icons.Default.Thermostat,
                    contentDescription = "Cabin Temperature",
                    tint = AutoTextSecondary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${cabinTemp}°C",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AutoTextPrimary,
                    fontWeight = FontWeight.Medium
                )
            }

            // Simulation Active Pill
            if (simulationActive) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(AutoWarningAmber.copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                        .testTag("simulation_badge")
                ) {
                    Text(
                        text = "SIMULATING",
                        style = MaterialTheme.typography.labelSmall,
                        color = AutoWarningAmber,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Network Indicator
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.testTag("top_bar_network")
            ) {
                Icon(
                    imageVector = if (networkConnected)
                        Icons.Default.SignalCellularAlt
                    else
                        Icons.Default.SignalCellularConnectedNoInternet0Bar,
                    contentDescription = if (networkConnected) "Network Connected" else "Network Offline",
                    tint = if (networkConnected) AutoBatteryGreen else AutoDangerRed,
                    modifier = Modifier.size(18.dp)
                )
            }

            // Time
            Text(
                text = currentTime,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = AutoTextPrimary,
                modifier = Modifier.testTag("top_bar_clock")
            )
        }
    }
}
