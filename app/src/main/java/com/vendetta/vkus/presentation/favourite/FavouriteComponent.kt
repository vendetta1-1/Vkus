package com.vendetta.vkus.presentation.favourite

import com.vendetta.domain.entity.SongEntity
import kotlinx.coroutines.flow.StateFlow

interface FavouriteComponent {

    val favouriteSongs: StateFlow<FavouriteStore.State>

    fun changeLikeStatus(song: SongEntity)

    fun playSong(songUri: String)

}