package com.vendetta.vkus.presentation.home

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.vendetta.domain.entity.SongEntity
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow

class DefaultHomeComponent @AssistedInject constructor(
    private val songListStoreFactory: HomeFactory,
    @Assisted("componentContext") componentContext: ComponentContext
) : HomeComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { songListStoreFactory.create() }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<HomeStore.State> = store.stateFlow

    override fun changeLikeStatus(song: SongEntity) {
        store.accept(HomeStore.Intent.ChangeLikeStatus(song))
    }

    override fun swipeSong(song: SongEntity) {
        store.accept(HomeStore.Intent.DeleteSong(song))
    }

    override fun addSong(uri: String) {
        store.accept(HomeStore.Intent.AddSong(uri))
    }

    override fun playSong(song: SongEntity) {
        store.accept(HomeStore.Intent.PlaySong(song))
    }

    @AssistedFactory
    interface Factory {
        operator fun invoke(
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultHomeComponent
    }
}