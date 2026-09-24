package com.example.autoconnect.domain.media

import kotlinx.coroutines.flow.Flow

class GetMediaStateUseCase(private val repository: MediaRepository) {
    operator fun invoke(): Flow<MediaState> = repository.observeMediaState()
}

class PlayMediaUseCase(private val repository: MediaRepository) {
    operator fun invoke() = repository.play()
}

class PauseMediaUseCase(private val repository: MediaRepository) {
    operator fun invoke() = repository.pause()
}

class TogglePlayPauseUseCase(private val repository: MediaRepository) {
    operator fun invoke() = repository.togglePlayPause()
}

class SkipMediaUseCase(private val repository: MediaRepository) {
    fun next() = repository.nextTrack()
    fun previous() = repository.previousTrack()
}

class SeekMediaUseCase(private val repository: MediaRepository) {
    operator fun invoke(positionMs: Long) = repository.seekTo(positionMs)
}

class SetVolumeUseCase(private val repository: MediaRepository) {
    operator fun invoke(volume: Float) = repository.setVolume(volume)
}
