package com.example.autoconnect.domain.media

import kotlinx.coroutines.flow.Flow

/**
 * Interface contract for automotive media playback control and observation.
 */
interface MediaRepository {
    fun observeMediaState(): Flow<MediaState>
    fun getCurrentMediaState(): MediaState
    fun play()
    fun pause()
    fun togglePlayPause()
    fun nextTrack()
    fun previousTrack()
    fun seekTo(positionMs: Long)
    fun setVolume(volume: Float)
    fun release()
}
