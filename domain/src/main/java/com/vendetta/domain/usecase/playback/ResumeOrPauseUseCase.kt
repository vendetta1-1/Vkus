package com.vendetta.domain.usecase.playback

import com.vendetta.domain.repository.playback.PlaybackRepository

class ResumeOrPauseUseCase(
    private val repository: PlaybackRepository
) {
    operator fun invoke() {
        repository.resumeOrPause()
    }
}