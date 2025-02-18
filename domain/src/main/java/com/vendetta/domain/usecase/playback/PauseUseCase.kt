package com.vendetta.domain.usecase.playback

import com.vendetta.domain.repository.PlaybackRepository

class PauseUseCase(
    private val repository: PlaybackRepository
) {

    operator fun invoke() {
        repository.pause()
    }
}