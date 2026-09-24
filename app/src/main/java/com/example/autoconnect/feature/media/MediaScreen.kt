package com.example.autoconnect.feature.media

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
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.VolumeDown
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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
import java.util.Locale

@Composable
fun MediaScreen(
    viewModel: MediaViewModel,
    modifier: Modifier = Modifier
) {
    val media by viewModel.mediaState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AutoDarkBackground)
            .testTag("media_screen")
    ) {
        AutomotiveTopBar()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!media.isAvailable) {
                item {
                    AutomotiveCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("media_unavailable_card"),
                        containerColor = AutoDangerRed.copy(alpha = 0.15f),
                        borderColor = AutoDangerRed
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Error",
                                tint = AutoDangerRed,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Demo media unavailable",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = AutoDangerRed
                            )
                        }
                    }
                }
            }

            // Modern Album Artwork Card
            item {
                AutomotiveCard(
                    modifier = Modifier
                        .size(220.dp)
                        .testTag("media_art_card"),
                    containerColor = AutoDarkSurfaceVariant
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(110.dp)
                                    .clip(CircleShape)
                                    .background(AutoDarkSurfaceElevated),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (media.isPlaying) Icons.Default.GraphicEq else Icons.Default.Album,
                                    contentDescription = "Album Art",
                                    tint = if (media.isPlaying) AutoPrimaryCyan else AutoTextSecondary,
                                    modifier = Modifier.size(60.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "MEDIA3 EXOPLAYER",
                                style = MaterialTheme.typography.labelSmall,
                                color = AutoSecondaryElectric,
                                letterSpacing = 2.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Track Meta Information
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = media.title.ifEmpty { "Cyber Highway" },
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = AutoTextPrimary,
                        modifier = Modifier.testTag("media_title")
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = media.artist.ifEmpty { "AutoConnect Synth Ensemble" },
                        style = MaterialTheme.typography.bodyLarge,
                        color = AutoTextSecondary,
                        modifier = Modifier.testTag("media_artist")
                    )
                    Text(
                        text = media.album.ifEmpty { "Infotainment Soundscapes" },
                        style = MaterialTheme.typography.bodySmall,
                        color = AutoTextMuted
                    )
                }
            }

            // Playback Progress & Timeline
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    val currentPos = media.position.toFloat().coerceAtLeast(0f)
                    val totalDuration = media.duration.toFloat().coerceAtLeast(1000f)

                    Slider(
                        value = (currentPos / totalDuration).coerceIn(0f, 1f),
                        onValueChange = { fraction ->
                            viewModel.seekTo((fraction * totalDuration).toLong())
                        },
                        colors = SliderDefaults.colors(
                            thumbColor = AutoPrimaryCyan,
                            activeTrackColor = AutoPrimaryCyan,
                            inactiveTrackColor = AutoDarkSurfaceElevated
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("slider_seek")
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = formatTime(media.position),
                            style = MaterialTheme.typography.bodySmall,
                            color = AutoTextSecondary,
                            modifier = Modifier.testTag("media_current_time")
                        )
                        Text(
                            text = formatTime(media.duration),
                            style = MaterialTheme.typography.bodySmall,
                            color = AutoTextSecondary,
                            modifier = Modifier.testTag("media_total_time")
                        )
                    }
                }
            }

            // Transport Playback Controls (Previous, Play/Pause, Next)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { viewModel.previousTrack() },
                        modifier = Modifier
                            .size(56.dp)
                            .testTag("btn_prev_track")
                    ) {
                        Icon(
                            imageVector = Icons.Default.SkipPrevious,
                            contentDescription = "Previous Track",
                            tint = AutoTextPrimary,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    Button(
                        onClick = { viewModel.togglePlayPause() },
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = AutoPrimaryCyan),
                        modifier = Modifier
                            .size(72.dp)
                            .testTag("btn_play_pause")
                    ) {
                        Icon(
                            imageVector = if (media.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (media.isPlaying) "Pause" else "Play",
                            tint = AutoDarkBackground,
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    IconButton(
                        onClick = { viewModel.nextTrack() },
                        modifier = Modifier
                            .size(56.dp)
                            .testTag("btn_next_track")
                    ) {
                        Icon(
                            imageVector = Icons.Default.SkipNext,
                            contentDescription = "Next Track",
                            tint = AutoTextPrimary,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
            }

            // Volume Control Card
            item {
                AutomotiveCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("media_volume_card")
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeDown,
                            contentDescription = "Volume Down",
                            tint = AutoTextSecondary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Slider(
                            value = media.volume,
                            onValueChange = { viewModel.setVolume(it) },
                            valueRange = 0f..1f,
                            colors = SliderDefaults.colors(
                                thumbColor = AutoSecondaryElectric,
                                activeTrackColor = AutoSecondaryElectric,
                                inactiveTrackColor = AutoDarkSurfaceElevated
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("slider_volume")
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Volume Up",
                            tint = AutoTextSecondary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

private fun formatTime(millis: Long): String {
    val totalSeconds = (millis / 1000).coerceAtLeast(0)
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
}
