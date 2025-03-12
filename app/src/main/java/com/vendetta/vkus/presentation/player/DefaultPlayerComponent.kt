package com.vendetta.vkus.presentation.player

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.vendetta.domain.entity.SongEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow

class DefaultPlayerComponent(
    componentContext: ComponentContext,
    private val currentSong: SongEntity,
    private val nextSong: SongEntity,
    private val previousSong: SongEntity,
    private val playerStoreFactory: PlayerFactory
) : PlayerComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore {
        playerStoreFactory.create(
            currentSong,
            nextSong,
            previousSong
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<PlayerStore.State> = store.stateFlow

    override fun playOrPause() {
        store.accept(PlayerStore.Intent.ResumeOrPause)
    }

    override fun changeLikeStatus() {
        store.accept(PlayerStore.Intent.ChangeLikeStatus)
    }

    override fun seekToNext(nextSong: SongEntity) {
        store.accept(PlayerStore.Intent.SeekToNext(nextSong))
    }

    override fun seekToPrevious(previousSong: SongEntity) {
        store.accept(PlayerStore.Intent.SeekToPrevious(previousSong))
    }

}