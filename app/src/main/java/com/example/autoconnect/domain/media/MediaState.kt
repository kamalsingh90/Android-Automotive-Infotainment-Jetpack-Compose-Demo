package com.example.autoconnect.domain.media

/**
 * Real-time media playback state observed from Media3 ExoPlayer & MediaSession.
 */
data class MediaState(
    val title: String = "Cyber Highway",
    val artist: String = "AutoConnect Synth Ensemble",
    val album: String = "Infotainment Soundscapes",
    val duration: Long = 0L,
    val position: Long = 0L,
    val isPlaying: Boolean = false,
    val volume: Float = 0.7f,
    val isAvailable: Boolean = true
)
