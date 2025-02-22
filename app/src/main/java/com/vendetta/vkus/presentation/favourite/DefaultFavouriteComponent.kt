package com.vendetta.vkus.presentation.favourite

import com.arkivanov.decompose.ComponentContext
import com.vendetta.domain.entity.SongEntity
import com.vendetta.domain.usecase.playback.PlaySongUseCase
import com.vendetta.domain.usecase.playlist.ChangeLikeStatusUseCase
import com.vendetta.domain.usecase.playlist.GetFavouriteSongsUseCase
import com.vendetta.vkus.core.componentScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DefaultFavouriteComponent(
    componentContext: ComponentContext,
    getFavouriteSongsUseCase: GetFavouriteSongsUseCase,
    private val playSongUseCase: PlaySongUseCase,
    private val changeLikeStatusUseCase: ChangeLikeStatusUseCase
) : FavouriteComponent, ComponentContext by componentContext {

    private val scope = componentScope()

    override val favouriteSongs: StateFlow<FavouriteComponent.Model> = getFavouriteSongsUseCase()
        .map { FavouriteComponent.Model(it) }
        .stateIn(
            scope = scope,
            started = SharingStarted.Lazily,
            initialValue = FavouriteComponent.Model(listOf())
        )

    override fun changeLikeStatus(song: SongEntity) {
        scope.launch {
            changeLikeStatusUseCase(song)
        }
    }

    override fun playSong(songUri: String) {
        playSongUseCase(songUri)
    }
}