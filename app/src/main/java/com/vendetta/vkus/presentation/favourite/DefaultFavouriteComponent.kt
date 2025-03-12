package com.vendetta.vkus.presentation.favourite

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.vendetta.domain.entity.SongEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow

class DefaultFavouriteComponent(
    componentContext: ComponentContext,
    private val favouriteStoreFactory: FavouriteFactory
) : FavouriteComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { favouriteStoreFactory.create() }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<FavouriteStore.State> = store.stateFlow

    override fun changeLikeStatus(song: SongEntity) {
        store.accept(FavouriteStore.Intent.ChangeLikeStatus(song))
    }

    override fun playSong(song: SongEntity) {
        store.accept(FavouriteStore.Intent.PlaySong(song))
    }
}