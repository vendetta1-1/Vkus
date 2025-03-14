package com.vendetta.domain.usecase

import com.vendetta.domain.repository.PlaylistRepository
import javax.inject.Inject

class AddSongUseCase @Inject constructor(
    private val repository: PlaylistRepository
) {

    suspend operator fun invoke(uri: String) {
        repository.addSong(uri)
    }
}