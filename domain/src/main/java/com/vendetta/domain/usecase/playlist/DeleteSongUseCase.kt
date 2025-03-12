package com.vendetta.domain.usecase.playlist

import com.vendetta.domain.entity.SongEntity
import com.vendetta.domain.repository.playlist.PlaylistRepository

class DeleteSongUseCase(
    private val repository: PlaylistRepository
) {
    suspend operator fun invoke(song: SongEntity) {
        repository.deleteSong(song)
    }
}