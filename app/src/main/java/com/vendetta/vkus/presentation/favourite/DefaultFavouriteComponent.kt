package com.vendetta.vkus.presentation.favourite

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.vendetta.domain.entity.SongEntity
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow

class DefaultFavouriteComponent @AssistedInject constructor(
    private val favouriteStoreFactory: FavouriteFactory,
    @Assisted("componentContext") componentContext: ComponentContext
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

    @AssistedFactory
    interface Factory {
        operator fun invoke(
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultFavouriteComponent
    }
}