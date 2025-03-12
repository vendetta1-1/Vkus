package com.vendetta.vkus.presentation.song_list

import android.net.Uri
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.vendetta.domain.entity.SongEntity
import com.vendetta.vkus.core.componentScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultSongListComponent(
    private val songListStoreFactory: SongListFactory,
    componentContext: ComponentContext,
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

    override fun addSong(uri: Uri) {
        store.accept(SongListStore.Intent.AddSong(uri))
    }

    override fun playSong(song: SongEntity) {
        store.accept(SongListStore.Intent.PlaySong(song))
    }

}