package com.vendetta.domain.usecase.playlist

import com.vendetta.domain.entity.SongEntity
import com.vendetta.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow

class GetFavouriteSongsUseCase(
    private val repository: PlaylistRepository
) {

    operator fun invoke(): Flow<List<SongEntity>> {
        return repository.favouriteSongs
    }
}