package com.vendetta.domain.usecase.playlist

import com.vendetta.domain.entity.SongEntity
import com.vendetta.domain.repository.playlist.PlaylistRepository
import kotlinx.coroutines.flow.Flow

class GetSongsUseCase(
    private val repository: PlaylistRepository
) {

    operator fun invoke(): Flow<List<SongEntity>> {
        return repository.songs
    }
}