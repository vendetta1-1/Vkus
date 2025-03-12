package com.vendetta.domain.usecase.playback

import com.vendetta.domain.entity.SongEntity
import com.vendetta.domain.repository.playback.PlaybackRepository

class PlaySongUseCase(
    private val repository: PlaybackRepository
) {
    operator fun invoke(song: SongEntity) {
        repository.play(song)
    }
}