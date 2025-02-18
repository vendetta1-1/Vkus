package com.vendetta.domain.usecase.playlist

import com.vendetta.domain.repository.PlaylistRepository

class AddSongUseCase(
    private val repository: PlaylistRepository
) {

    suspend operator fun invoke(path: String) {
        repository.addSong(path)
    }
}