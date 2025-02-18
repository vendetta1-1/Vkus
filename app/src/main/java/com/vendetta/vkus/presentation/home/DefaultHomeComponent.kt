package com.vendetta.vkus.presentation.home

import com.arkivanov.decompose.ComponentContext
import com.vendetta.domain.entity.SongEntity
import com.vendetta.domain.usecase.playback.PlaySongUseCase
import com.vendetta.domain.usecase.playlist.AddSongUseCase
import com.vendetta.domain.usecase.playlist.DeleteSongUseCase
import com.vendetta.domain.usecase.playlist.GetSongsUseCase
import com.vendetta.vkus.core.componentScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DefaultHomeComponent(
    componentContext: ComponentContext,
    private val getSongsUseCase: GetSongsUseCase,
    private val addSongUseCase: AddSongUseCase,
    private val deleteSongUseCase: DeleteSongUseCase,
    private val playSongUseCase: PlaySongUseCase
) : HomeComponent, ComponentContext by componentContext {

    private val scope = componentScope()

    override val model: StateFlow<HomeComponent.Model> = getSongsUseCase()
        .map { HomeComponent.Model(it) }
        .stateIn(
            scope = scope,
            started = SharingStarted.Lazily,
            initialValue = HomeComponent.Model(listOf())
        )

    override fun deleteSong(song: SongEntity) {
        scope.launch {
            deleteSongUseCase(song)
        }
    }

    override fun addSong(path: String) {
        scope.launch {
            addSongUseCase(path)
        }
    }

    override fun playSong(songUri: String) {
        playSongUseCase(songUri)
    }
}