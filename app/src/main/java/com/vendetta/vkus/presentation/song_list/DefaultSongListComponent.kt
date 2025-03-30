package com.vendetta.vkus.presentation.song_list

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.vendetta.domain.entity.SongEntity
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow

class DefaultSongListComponent @AssistedInject constructor(
    private val songListStoreFactory: SongListFactory,
    @Assisted("componentContext") componentContext: ComponentContext,
) : SongListComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { songListStoreFactory.create() }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<SongListStore.State> = store.stateFlow

    override fun changeLikeStatus(song: SongEntity) {
        store.accept(SongListStore.Intent.ChangeLikeStatus(song))
    }

    override fun deleteSong(song: SongEntity) {
        store.accept(SongListStore.Intent.DeleteSong(song))
    }

    override fun addSong(uri: String) {
        store.accept(SongListStore.Intent.AddSong(uri))
    }

    override fun playSong(song: SongEntity) {
        store.accept(SongListStore.Intent.PlaySong(song))
    }

    @AssistedFactory
    interface Factory {
        fun crete(
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultSongListComponent
    }
}