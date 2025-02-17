package com.vendetta.domain.usecase.playback

import com.vendetta.domain.repository.PlaybackRepository

class SeekToPreviousUseCase(
    private val repository: PlaybackRepository
) {

    suspend operator fun invoke(){
        repository.seekToPrevious()
    }
}