package com.vendetta.vkus.presentation.player

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.vendetta.domain.entity.SongEntity
import com.vendetta.vkus.core.componentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultPlayerComponent @AssistedInject constructor(
    @Assisted("componentContext") componentContext: ComponentContext,
    @Assisted("currentSong") private val currentSong: SongEntity,
    @Assisted("nextSong") private val nextSong: SongEntity,
    @Assisted("previousSong") private val previousSong: SongEntity,
    @Assisted("onDismissed") private val onDismiss: () -> Unit,
    private val playerStoreFactory: PlayerFactory
) : PlayerComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore {
        playerStoreFactory.create(
            currentSong = currentSong,
            nextSong = nextSong,
            previousSong = previousSong
        )
    }

    init {
        componentScope().launch {
            store.labels.collect {
                when(it){
                    PlayerStore.Label.OnDismiss -> onDismiss
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<PlayerStore.State> = store.stateFlow

    override fun playOrPause() {
        store.accept(PlayerStore.Intent.ResumeOrPause)
    }

    override fun changeLikeStatus() {
        store.accept(PlayerStore.Intent.ChangeLikeStatus(currentSong))
    }

    override fun seekToNext(nextSong: SongEntity) {
        store.accept(PlayerStore.Intent.SeekToNext(nextSong))
    }

    override fun seekToPrevious(previousSong: SongEntity) {
        store.accept(PlayerStore.Intent.SeekToPrevious(previousSong))
    }

    override fun onDismiss() {
        store.accept(PlayerStore.Intent.OnDismiss)
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("currentSong") currentSong: SongEntity,
            @Assisted("nextSong") nextSong: SongEntity,
            @Assisted("previousSong") previousSong: SongEntity,
            @Assisted("onDismissed") onDismissed: () -> Unit
        ): DefaultPlayerComponent
    }
}