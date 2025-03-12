package com.vendetta.domain.usecase.playlist

import android.net.Uri
import com.vendetta.domain.repository.playlist.PlaylistRepository


class AddSongUseCase(
    private val repository: PlaylistRepository
) {

    suspend operator fun invoke(uri: Uri) {
        repository.addSong(uri)
    }
}