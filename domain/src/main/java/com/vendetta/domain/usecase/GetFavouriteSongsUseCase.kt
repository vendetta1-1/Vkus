package com.vendetta.domain.usecase

import com.vendetta.domain.entity.SongEntity
import com.vendetta.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavouriteSongsUseCase @Inject constructor(
    private val repository: PlaylistRepository
) {

    operator fun invoke(): Flow<List<SongEntity>> {
        return repository.favouriteSongs
    }
}