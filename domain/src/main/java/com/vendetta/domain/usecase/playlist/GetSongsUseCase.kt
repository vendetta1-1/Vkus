package com.vendetta.domain.usecase.playlist

import com.vendetta.domain.entity.SongEntity
import com.vendetta.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.StateFlow

class GetSongsUseCase(
    private val repository: PlaylistRepository
) {

    operator fun invoke() : StateFlow<List<SongEntity>>{
        return repository.getSongs()
    }
}