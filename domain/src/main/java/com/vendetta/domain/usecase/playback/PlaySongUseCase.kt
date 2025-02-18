package com.vendetta.domain.usecase.playback

import com.vendetta.domain.repository.PlaybackRepository

class PlaySongUseCase(
    private val repository: PlaybackRepository
) {

    operator fun invoke(songUri: String) {
        repository.play(songUri)
    }
}