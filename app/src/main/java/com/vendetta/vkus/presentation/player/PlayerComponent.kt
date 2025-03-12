package com.vendetta.vkus.presentation.player

import com.arkivanov.decompose.ComponentContext
import com.vendetta.domain.entity.SongEntity
import kotlinx.coroutines.flow.StateFlow

interface PlayerComponent {

    val model: StateFlow<PlayerStore.State>

    fun playOrPause()

    fun changeLikeStatus()

    fun seekToNext(nextSong: SongEntity)

    fun seekToPrevious(previousSong: SongEntity)

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            storeFactory: PlayerFactory,
            currentSong: SongEntity,
            previousSong: SongEntity,
            nextSong: SongEntity
        ): PlayerComponent
    }
}