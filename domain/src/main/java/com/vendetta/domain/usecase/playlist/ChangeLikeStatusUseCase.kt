package com.vendetta.domain.usecase.playlist

import com.vendetta.domain.entity.SongEntity
import com.vendetta.domain.repository.PlaylistRepository

class ChangeLikeStatusUseCase(
    private val repository: PlaylistRepository
) {

    suspend operator fun invoke(songEntity: SongEntity) {
        repository.changeLikeStatus(songEntity)
    }
}