package com.vendetta.domain.usecase.playback

import com.vendetta.domain.repository.PlaybackRepository

class SeekToNextUseCase(
    private val repository: PlaybackRepository
) {

    suspend operator fun invoke() {
        repository.seekToNext()
    }
}