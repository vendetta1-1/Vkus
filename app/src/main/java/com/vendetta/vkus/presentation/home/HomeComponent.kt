package com.vendetta.vkus.presentation.home

import com.vendetta.domain.entity.SongEntity
import kotlinx.coroutines.flow.StateFlow

interface HomeComponent {

    val model: StateFlow<HomeStore.State>

    fun changeLikeStatus(song: SongEntity)

    fun swipeSong(song: SongEntity)

    fun addSong(uri: String)

    fun playSong(song: SongEntity)
}