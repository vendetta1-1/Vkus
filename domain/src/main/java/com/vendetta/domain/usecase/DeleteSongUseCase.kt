package com.vendetta.domain.usecase

import com.vendetta.domain.entity.SongEntity
import com.vendetta.domain.repository.PlaylistRepository
import javax.inject.Inject

class DeleteSongUseCase @Inject constructor(
    private val repository: PlaylistRepository
) {
    suspend operator fun invoke(song: SongEntity) {
        repository.deleteSong(song)
    }
}