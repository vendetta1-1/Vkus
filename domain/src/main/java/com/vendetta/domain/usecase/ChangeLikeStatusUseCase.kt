package com.vendetta.domain.usecase

import com.vendetta.domain.entity.SongEntity
import com.vendetta.domain.repository.PlaylistRepository
import javax.inject.Inject

class ChangeLikeStatusUseCase @Inject constructor(
    private val repository: PlaylistRepository
) {

    suspend operator fun invoke(songEntity: SongEntity) {
        repository.changeLikeStatus(songEntity)
    }
}