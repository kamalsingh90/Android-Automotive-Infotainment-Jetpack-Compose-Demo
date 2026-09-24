package com.example.autoconnect.feature.navigation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.autoconnect.core.ui.components.AutomotiveCard
import com.example.autoconnect.core.ui.components.AutomotiveTopBar
import com.example.ui.theme.AutoBatteryGreen
import com.example.ui.theme.AutoCardBorder
import com.example.ui.theme.AutoDarkBackground
import com.example.ui.theme.AutoDarkSurfaceElevated
import com.example.ui.theme.AutoDarkSurfaceVariant
import com.example.ui.theme.AutoPrimaryCyan
import com.example.ui.theme.AutoSecondaryElectric
import com.example.ui.theme.AutoTextMuted
import com.example.ui.theme.AutoTextPrimary
import com.example.ui.theme.AutoTextSecondary

@Composable
fun NavigationSimulatorScreen(
    viewModel: NavigationViewModel,
    modifier: Modifier = Modifier
) {
    val navState by viewModel.navigationState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AutoDarkBackground)
            .testTag("navigation_screen")
    ) {
        AutomotiveTopBar()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Turn-by-Turn Maneuver Guidance Card
            item {
                AutomotiveCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("nav_guidance_card"),
                    containerColor = AutoDarkSurfaceVariant
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(AutoPrimaryCyan),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Turn Instruction",
                                tint = AutoDarkBackground,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text(
                                text = navState.instruction,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = AutoTextPrimary,
                                modifier = Modifier.testTag("nav_instruction_text")
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Next: ${navState.nextManeuver}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = AutoTextSecondary,
                                modifier = Modifier.testTag("nav_next_maneuver_text")
                            )
                        }
                    }
                }
            }

            // Destination Selector Tabs (Home, Office, Airport)
            item {
                Text(
                    text = "SELECT DESTINATION",
                    style = MaterialTheme.typography.labelSmall,
                    color = AutoTextSecondary,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(start = 4.dp, bottom = 2.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    DestinationButton(
                        name = "Home",
                        icon = Icons.Default.Home,
                        isSelected = navState.destination == "Home",
                        testTag = "dest_home",
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.selectDestination("Home") }
                    )
                    DestinationButton(
                        name = "Office",
                        icon = Icons.Default.Work,
                        isSelected = navState.destination == "Office",
                        testTag = "dest_office",
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.selectDestination("Office") }
                    )
                    DestinationButton(
                        name = "Airport",
                        icon = Icons.Default.FlightTakeoff,
                        isSelected = navState.destination == "Airport",
                        testTag = "dest_airport",
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.selectDestination("Airport") }
                    )
                }
            }

            // Route Metrics (Distance, ETA, Current Road)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AutomotiveCard(
                        modifier = Modifier
                            .weight(1f)
                            .testTag("nav_eta_card")
                    ) {
                        Text(
                            text = "ETA",
                            style = MaterialTheme.typography.labelSmall,
                            color = AutoTextMuted
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${navState.etaMinutes} min",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = AutoBatteryGreen,
                            modifier = Modifier.testTag("nav_eta_text")
                        )
                    }

                    AutomotiveCard(
                        modifier = Modifier
                            .weight(1f)
                            .testTag("nav_distance_card")
                    ) {
                        Text(
                            text = "DISTANCE",
                            style = MaterialTheme.typography.labelSmall,
                            color = AutoTextMuted
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        val km = navState.distanceMeters / 1000f
                        Text(
                            text = String.format("%.1f km", km),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = AutoSecondaryElectric,
                            modifier = Modifier.testTag("nav_distance_text")
                        )
                    }
                }
            }

            // Simulated Map Route Canvas Graphic
            item {
                AutomotiveCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .testTag("nav_map_canvas_card")
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val w = size.width
                            val h = size.height

                            // Draw simulated road grid lines
                            drawLine(
                                color = AutoDarkSurfaceElevated,
                                start = Offset(0f, h * 0.35f),
                                end = Offset(w, h * 0.35f),
                                strokeWidth = 18f
                            )
                            drawLine(
                                color = AutoDarkSurfaceElevated,
                                start = Offset(0f, h * 0.7f),
                                end = Offset(w, h * 0.7f),
                                strokeWidth = 24f
                            )
                            drawLine(
                                color = AutoDarkSurfaceElevated,
                                start = Offset(w * 0.4f, 0f),
                                end = Offset(w * 0.4f, h),
                                strokeWidth = 20f
                            )

                            // Route Path in Glowing Cyan
                            val routePath = Path().apply {
                                moveTo(w * 0.15f, h * 0.7f)
                                lineTo(w * 0.4f, h * 0.7f)
                                lineTo(w * 0.4f, h * 0.35f)
                                lineTo(w * 0.85f, h * 0.35f)
                            }
                            drawPath(
                                path = routePath,
                                color = AutoPrimaryCyan,
                                style = Stroke(width = 8f, cap = StrokeCap.Round)
                            )

                            // Vehicle position dot
                            drawCircle(
                                color = AutoPrimaryCyan,
                                radius = 12f,
                                center = Offset(w * 0.4f, h * 0.55f)
                            )
                            drawCircle(
                                color = Color.White,
                                radius = 6f,
                                center = Offset(w * 0.4f, h * 0.55f)
                            )

                            // Destination pin
                            drawCircle(
                                color = AutoBatteryGreen,
                                radius = 10f,
                                center = Offset(w * 0.85f, h * 0.35f)
                            )
                        }

                        // Current Street Overlay
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .clip(RoundedCornerShape(8.dp))
                                .background(AutoDarkBackground.copy(alpha = 0.85f))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "Current: ${navState.currentStreet}",
                                style = MaterialTheme.typography.bodySmall,
                                color = AutoTextPrimary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DestinationButton(
    name: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    testTag: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) AutoPrimaryCyan else AutoDarkSurfaceVariant
        ),
        modifier = modifier
            .height(52.dp)
            .testTag(testTag)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = name,
            tint = if (isSelected) AutoDarkBackground else AutoTextPrimary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = name,
            style = MaterialTheme.typography.labelLarge,
            color = if (isSelected) AutoDarkBackground else AutoTextPrimary,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}
