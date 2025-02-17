package com.vendetta.domain.usecase.playback

import com.vendetta.domain.repository.PlaybackRepository

class PlayUseCase(
    private val repository: PlaybackRepository
) {

    suspend operator fun invoke(songUri: String) {
        repository.play(songUri)
    }
}