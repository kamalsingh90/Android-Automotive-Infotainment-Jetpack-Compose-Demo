package com.example.autoconnect.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.autoconnect.core.navigation.Screen
import com.example.ui.theme.AutoCardBorder
import com.example.ui.theme.AutoDarkSurface
import com.example.ui.theme.AutoDarkSurfaceVariant
import com.example.ui.theme.AutoPrimaryCyan
import com.example.ui.theme.AutoTextMuted
import com.example.ui.theme.AutoTextPrimary

@Composable
fun AutomotiveBottomNavBar(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("automotive_bottom_bar"),
        color = AutoDarkSurface,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavBarItem(
                title = "Dashboard",
                icon = Icons.Default.Speed,
                isSelected = currentRoute == Screen.Dashboard.route,
                testTag = "nav_dashboard",
                onClick = { onNavigate(Screen.Dashboard.route) }
            )
            NavBarItem(
                title = "Vehicle",
                icon = Icons.Default.DirectionsCar,
                isSelected = currentRoute == Screen.Vehicle.route,
                testTag = "nav_vehicle",
                onClick = { onNavigate(Screen.Vehicle.route) }
            )
            NavBarItem(
                title = "Media",
                icon = Icons.Default.MusicNote,
                isSelected = currentRoute == Screen.Media.route,
                testTag = "nav_media",
                onClick = { onNavigate(Screen.Media.route) }
            )
            NavBarItem(
                title = "Navigation",
                icon = Icons.Default.Navigation,
                isSelected = currentRoute == Screen.Navigation.route,
                testTag = "nav_navigation",
                onClick = { onNavigate(Screen.Navigation.route) }
            )
            NavBarItem(
                title = "Settings",
                icon = Icons.Default.Settings,
                isSelected = currentRoute == Screen.Settings.route,
                testTag = "nav_settings",
                onClick = { onNavigate(Screen.Settings.route) }
            )
            NavBarItem(
                title = "Diagnostics",
                icon = Icons.Default.Build,
                isSelected = currentRoute == Screen.Diagnostics.route,
                testTag = "nav_diagnostics",
                onClick = { onNavigate(Screen.Diagnostics.route) }
            )
        }
    }
}

@Composable
private fun NavBarItem(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    testTag: String,
    onClick: () -> Unit
) {
    val activeColor = AutoPrimaryCyan
    val inactiveColor = AutoTextMuted

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) AutoDarkSurfaceVariant else androidx.compose.ui.graphics.Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .height(52.dp)
            .testTag(testTag),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = if (isSelected) activeColor else inactiveColor,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) AutoTextPrimary else inactiveColor
        )
    }
}
