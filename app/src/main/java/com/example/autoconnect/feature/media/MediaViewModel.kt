package com.example.autoconnect.feature.media

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.autoconnect.domain.media.GetMediaStateUseCase
import com.example.autoconnect.domain.media.MediaRepository
import com.example.autoconnect.domain.media.MediaState
import com.example.autoconnect.domain.media.PauseMediaUseCase
import com.example.autoconnect.domain.media.PlayMediaUseCase
import com.example.autoconnect.domain.media.SeekMediaUseCase
import com.example.autoconnect.domain.media.SetVolumeUseCase
import com.example.autoconnect.domain.media.SkipMediaUseCase
import com.example.autoconnect.domain.media.TogglePlayPauseUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class MediaViewModel(
    private val getMediaStateUseCase: GetMediaStateUseCase,
    private val playMediaUseCase: PlayMediaUseCase,
    private val pauseMediaUseCase: PauseMediaUseCase,
    private val togglePlayPauseUseCase: TogglePlayPauseUseCase,
    private val skipMediaUseCase: SkipMediaUseCase,
    private val seekMediaUseCase: SeekMediaUseCase,
    private val setVolumeUseCase: SetVolumeUseCase,
    private val mediaRepository: MediaRepository
) : ViewModel() {

    val mediaState: StateFlow<MediaState> = getMediaStateUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = mediaRepository.getCurrentMediaState()
    )

    fun play() = playMediaUseCase()

    fun pause() = pauseMediaUseCase()

    fun togglePlayPause() = togglePlayPauseUseCase()

    fun nextTrack() = skipMediaUseCase.next()

    fun previousTrack() = skipMediaUseCase.previous()

    fun seekTo(positionMs: Long) = seekMediaUseCase(positionMs)

    fun setVolume(volume: Float) = setVolumeUseCase(volume)

    class Factory(
        private val getMediaStateUseCase: GetMediaStateUseCase,
        private val playMediaUseCase: PlayMediaUseCase,
        private val pauseMediaUseCase: PauseMediaUseCase,
        private val togglePlayPauseUseCase: TogglePlayPauseUseCase,
        private val skipMediaUseCase: SkipMediaUseCase,
        private val seekMediaUseCase: SeekMediaUseCase,
        private val setVolumeUseCase: SetVolumeUseCase,
        private val mediaRepository: MediaRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MediaViewModel(
                getMediaStateUseCase,
                playMediaUseCase,
                pauseMediaUseCase,
                togglePlayPauseUseCase,
                skipMediaUseCase,
                seekMediaUseCase,
                setVolumeUseCase,
                mediaRepository
            ) as T
        }
    }
}
