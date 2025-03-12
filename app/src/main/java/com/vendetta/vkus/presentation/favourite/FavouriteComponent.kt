package com.vendetta.vkus.presentation.favourite

import com.arkivanov.decompose.ComponentContext
import com.vendetta.domain.entity.SongEntity
import kotlinx.coroutines.flow.StateFlow

interface FavouriteComponent {

    val model: StateFlow<FavouriteStore.State>

    fun changeLikeStatus(song: SongEntity)

    fun playSong(song: SongEntity)

    fun interface Factory {
       operator fun invoke(
           componentContext: ComponentContext,
           storeFactory: FavouriteFactory
       ): FavouriteComponent
    }
}