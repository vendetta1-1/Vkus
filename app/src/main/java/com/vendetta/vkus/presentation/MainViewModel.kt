package com.vendetta.vkus.presentation

import androidx.lifecycle.ViewModel
import com.vendetta.domain.usecase.playlist.AddSongUseCase
import com.vendetta.domain.usecase.playlist.DeleteSongUseCase
import com.vendetta.domain.usecase.playlist.GetSongsUseCase

class MainViewModel(
    private val getSongsUseCase: GetSongsUseCase,
    private val deleteSongUseCase: DeleteSongUseCase,
    private val addSongUseCase: AddSongUseCase
) : ViewModel() {


}