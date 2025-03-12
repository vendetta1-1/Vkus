package com.vendetta.domain.usecase.playback

import com.vendetta.domain.repository.playback.PlaybackRepository

class SeekToPreviousUseCase(
    private val repository: PlaybackRepository
) {
    operator fun invoke() {
        repository.seekToPrevious()
    }
}