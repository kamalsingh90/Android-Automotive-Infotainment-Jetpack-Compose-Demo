package com.example.autoconnect.data.media

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.autoconnect.domain.media.MediaRepository
import com.example.autoconnect.domain.media.MediaState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * AndroidX Media3 ExoPlayer-backed implementation of MediaRepository.
 * Manages player lifecycle, local playback, and reactive state updates.
 */
class MediaRepositoryImpl(
    private val context: Context,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Main)
) : MediaRepository {

    companion object {
        private const val TAG = "MediaRepository"
    }

    private var player: ExoPlayer? = null
    private val tracks = DemoMediaCatalog.getDemoTracks(context)
    private var currentTrackIndex = 0

    private val _mediaState = MutableStateFlow(
        MediaState(
            title = tracks.firstOrNull()?.title ?: "Demo Track",
            artist = tracks.firstOrNull()?.artist ?: "AutoConnect Audio",
            album = tracks.firstOrNull()?.album ?: "Automotive Demo",
            duration = 4000L,
            position = 0L,
            isPlaying = false,
            volume = 0.7f,
            isAvailable = true
        )
    )

    private var positionTrackerJob: Job? = null
    private val mainHandler = Handler(Looper.getMainLooper())

    init {
        mainHandler.post {
            initializePlayer()
        }
    }

    private fun initializePlayer() {
        try {
            val exoPlayer = ExoPlayer.Builder(context).build()
            player = exoPlayer

            tracks.forEach { track ->
                val mediaItem = MediaItem.fromUri(track.uriString)
                exoPlayer.addMediaItem(mediaItem)
            }

            exoPlayer.repeatMode = Player.REPEAT_MODE_ALL
            exoPlayer.volume = _mediaState.value.volume

            exoPlayer.addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    val currentPos = player?.currentPosition ?: 0L
                    val duration = player?.duration?.takeIf { it > 0 } ?: 4000L
                    _mediaState.value = _mediaState.value.copy(
                        isPlaying = isPlaying,
                        position = currentPos,
                        duration = duration
                    )
                    if (isPlaying) {
                        startPositionTracker()
                    } else {
                        stopPositionTracker()
                    }
                }

                override fun onPlaybackStateChanged(playbackState: Int) {
                    val duration = player?.duration?.takeIf { it > 0 } ?: _mediaState.value.duration
                    _mediaState.value = _mediaState.value.copy(duration = duration)
                }

                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    val index = player?.currentMediaItemIndex ?: 0
                    currentTrackIndex = index
                    val track = tracks.getOrNull(index) ?: return
                    _mediaState.value = _mediaState.value.copy(
                        title = track.title,
                        artist = track.artist,
                        album = track.album,
                        position = 0L
                    )
                }

                override fun onPlayerError(error: PlaybackException) {
                    Log.w(TAG, "Playback error encountered: ${error.message}")
                    _mediaState.value = _mediaState.value.copy(
                        isPlaying = false,
                        isAvailable = false
                    )
                }
            })

            exoPlayer.prepare()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize ExoPlayer: ${e.message}")
            _mediaState.value = _mediaState.value.copy(isAvailable = false)
        }
    }

    private fun startPositionTracker() {
        positionTrackerJob?.cancel()
        positionTrackerJob = scope.launch {
            while (isActive) {
                delay(300L)
                player?.let { p ->
                    val pos = p.currentPosition
                    val dur = if (p.duration > 0) p.duration else _mediaState.value.duration
                    _mediaState.value = _mediaState.value.copy(
                        position = pos,
                        duration = dur
                    )
                }
            }
        }
    }

    private fun stopPositionTracker() {
        positionTrackerJob?.cancel()
        positionTrackerJob = null
    }

    override fun observeMediaState(): Flow<MediaState> = _mediaState.asStateFlow()

    override fun getCurrentMediaState(): MediaState = _mediaState.value

    override fun play() {
        mainHandler.post {
            try {
                player?.play()
            } catch (e: Exception) {
                Log.e(TAG, "Play failed: ${e.message}")
            }
        }
    }

    override fun pause() {
        mainHandler.post {
            try {
                player?.pause()
            } catch (e: Exception) {
                Log.e(TAG, "Pause failed: ${e.message}")
            }
        }
    }

    override fun togglePlayPause() {
        mainHandler.post {
            player?.let {
                if (it.isPlaying) it.pause() else it.play()
            }
        }
    }

    override fun nextTrack() {
        mainHandler.post {
            try {
                if (player?.hasNextMediaItem() == true) {
                    player?.seekToNextMediaItem()
                } else if (tracks.isNotEmpty()) {
                    player?.seekToDefaultPosition(0)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Next track failed: ${e.message}")
            }
        }
    }

    override fun previousTrack() {
        mainHandler.post {
            try {
                if (player?.hasPreviousMediaItem() == true) {
                    player?.seekToPreviousMediaItem()
                } else if (tracks.isNotEmpty()) {
                    player?.seekToDefaultPosition(tracks.size - 1)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Previous track failed: ${e.message}")
            }
        }
    }

    override fun seekTo(positionMs: Long) {
        mainHandler.post {
            try {
                player?.seekTo(positionMs)
                _mediaState.value = _mediaState.value.copy(position = positionMs)
            } catch (e: Exception) {
                Log.e(TAG, "Seek failed: ${e.message}")
            }
        }
    }

    override fun setVolume(volume: Float) {
        val clamped = volume.coerceIn(0f, 1f)
        mainHandler.post {
            player?.volume = clamped
            _mediaState.value = _mediaState.value.copy(volume = clamped)
        }
    }

    override fun release() {
        mainHandler.post {
            stopPositionTracker()
            player?.release()
            player = null
        }
    }
}
