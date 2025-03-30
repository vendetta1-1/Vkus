package com.vendetta.domain.usecase

import com.vendetta.domain.entity.SongEntity
import com.vendetta.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetSongsUseCase @Inject constructor(
    private val repository: PlaylistRepository
) {

    operator fun invoke(): StateFlow<List<SongEntity>> {
        return repository.songs
    }
}