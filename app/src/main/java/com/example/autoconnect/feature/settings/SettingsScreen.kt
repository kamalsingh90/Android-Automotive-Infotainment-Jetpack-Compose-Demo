package com.example.autoconnect.feature.settings

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.autoconnect.core.ui.components.AutomotiveCard
import com.example.autoconnect.core.ui.components.AutomotiveTopBar
import com.example.ui.theme.AutoBatteryGreen
import com.example.ui.theme.AutoDarkBackground
import com.example.ui.theme.AutoDarkSurfaceElevated
import com.example.ui.theme.AutoDarkSurfaceVariant
import com.example.ui.theme.AutoPrimaryCyan
import com.example.ui.theme.AutoSecondaryElectric
import com.example.ui.theme.AutoTextMuted
import com.example.ui.theme.AutoTextPrimary
import com.example.ui.theme.AutoTextSecondary

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    modifier: Modifier = Modifier
) {
    val settings by viewModel.settingsState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AutoDarkBackground)
            .testTag("settings_screen")
    ) {
        AutomotiveTopBar()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // DataStore Persistence Status Card
            item {
                AutomotiveCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("settings_persistence_card"),
                    containerColor = AutoDarkSurfaceVariant
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Storage,
                            contentDescription = "DataStore",
                            tint = AutoPrimaryCyan,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.size(10.dp))
                        Column {
                            Text(
                                text = "PREFERENCES DATASTORE",
                                style = MaterialTheme.typography.labelSmall,
                                color = AutoPrimaryCyan,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Settings are reactively saved and persist across application restarts.",
                                style = MaterialTheme.typography.bodySmall,
                                color = AutoTextSecondary
                            )
                        }
                    }
                }
            }

            // General Display & Simulation Settings Card
            item {
                AutomotiveCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("settings_general_card")
                ) {
                    Text(
                        text = "SYSTEM & SIMULATION",
                        style = MaterialTheme.typography.labelSmall,
                        color = AutoTextSecondary,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    SettingToggleRow(
                        title = "Dark Theme",
                        subtitle = "High contrast automotive night palette",
                        checked = settings.darkMode,
                        testTag = "switch_dark_mode",
                        onCheckedChange = { viewModel.setDarkMode(it) }
                    )

                    SettingToggleRow(
                        title = "Live Vehicle Simulation",
                        subtitle = "Continuously simulate speed, battery, and range",
                        checked = settings.simulationEnabled,
                        testTag = "switch_simulation",
                        onCheckedChange = { viewModel.setSimulationEnabled(it) }
                    )

                    SettingToggleRow(
                        title = "Vehicle Safety Alerts",
                        subtitle = "Notify on overspeed, low battery, and unlatched doors",
                        checked = settings.notificationsEnabled,
                        testTag = "switch_notifications",
                        onCheckedChange = { viewModel.setNotificationsEnabled(it) }
                    )
                }
            }

            // Audio & Volume Settings Card
            item {
                AutomotiveCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("settings_audio_card")
                ) {
                    Text(
                        text = "INFOTAINMENT AUDIO",
                        style = MaterialTheme.typography.labelSmall,
                        color = AutoTextSecondary,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Master Infotainment Volume",
                            style = MaterialTheme.typography.bodyLarge,
                            color = AutoTextPrimary
                        )
                        Text(
                            text = "${(settings.volume * 100).toInt()}%",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = AutoSecondaryElectric
                        )
                    }

                    Slider(
                        value = settings.volume,
                        onValueChange = { viewModel.setVolume(it) },
                        valueRange = 0f..1f,
                        colors = SliderDefaults.colors(
                            thumbColor = AutoSecondaryElectric,
                            activeTrackColor = AutoSecondaryElectric,
                            inactiveTrackColor = AutoDarkSurfaceElevated
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("slider_settings_volume")
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    testTag: String,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = AutoTextPrimary
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = AutoTextSecondary
            )
        }
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
