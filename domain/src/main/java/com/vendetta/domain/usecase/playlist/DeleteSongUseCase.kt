package com.vendetta.domain.usecase.playlist

import com.vendetta.domain.repository.PlaylistRepository

class DeleteSongUseCase(
    private val repository: PlaylistRepository
) {

    suspend operator fun invoke(songId: Int) {
        repository.deleteSong(songId)
    }
}