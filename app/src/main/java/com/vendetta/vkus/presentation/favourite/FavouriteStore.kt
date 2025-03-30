package com.vendetta.vkus.presentation.favourite

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.vendetta.domain.entity.SongEntity
import com.vendetta.domain.usecase.ChangeLikeStatusUseCase
import com.vendetta.domain.usecase.GetFavouriteSongsUseCase
import com.vendetta.vkus.presentation.favourite.FavouriteFactory.Message.ChangeLikeStatus
import com.vendetta.vkus.presentation.favourite.FavouriteFactory.Message.PlaySong
import com.vendetta.vkus.presentation.favourite.FavouriteFactory.Message.SongsLoaded
import com.vendetta.vkus.presentation.favourite.FavouriteStore.Intent
import com.vendetta.vkus.presentation.favourite.FavouriteStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

interface FavouriteStore : Store<Intent, State, Nothing> {

    data class State(
        val songs: List<SongEntity> = listOf(),
        val nowPlayingSong: SongEntity? = null
    )

    sealed interface Intent {
        data class PlaySong(val song: SongEntity) : Intent
        data class ChangeLikeStatus(val song: SongEntity) : Intent
    }

}

class FavouriteFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getFavouriteSongsUseCase: GetFavouriteSongsUseCase,
    private val changeLikeStatusUseCase: ChangeLikeStatusUseCase
) {


    fun create(): FavouriteStore = object : FavouriteStore,
        Store<Intent, State, Nothing> by storeFactory.create(
            name = STORE_NAME,
            initialState = State(),
            reducer = ReducerImpl,
            executorFactory = ::Executor,
            bootstrapper = BootstrapperImpl()
        ) {}

    private sealed interface Action {
        data class SongsLoaded(val songs: List<SongEntity>) : Action
    }

    private sealed interface Message {
        data class SongsLoaded(val songs: List<SongEntity>) : Message
        data class PlaySong(val song: SongEntity) : Message
        data class ChangeLikeStatus(val song: SongEntity) : Message
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {

        override fun invoke() {
            scope.launch {
                getFavouriteSongsUseCase().collect {
                    dispatch(Action.SongsLoaded(it))
                }
            }
        }
    }

    private inner class Executor :
        CoroutineExecutor<Intent, Action, State, Message, Nothing>() {

        override fun executeAction(action: Action) {
            when (action) {
                is Action.SongsLoaded -> {
                    dispatch(SongsLoaded(action.songs))
                }
            } 
        }

        override fun executeIntent(intent: Intent) {
            when (intent) {
                is Intent.ChangeLikeStatus -> {
                    scope.launch {
                        changeLikeStatusUseCase(intent.song)
                    }
                    dispatch(ChangeLikeStatus(intent.song))
                }

                is Intent.PlaySong -> {
                    dispatch(PlaySong(intent.song))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Message> {

        override fun State.reduce(msg: Message): State {
            return when (msg) {
                is ChangeLikeStatus -> {
                    val oldSong = msg.song
                    val index = songs.indexOf(oldSong)
                    val newSong = oldSong.copy(isFavourite = !oldSong.isFavourite)
                    copy(
                        songs = this.songs.toMutableList().apply {
                            this[index] = newSong
                        }
                    )
                }
                is PlaySong -> {
                    copy(nowPlayingSong = msg.song)
                }
                is SongsLoaded -> {
                    copy(songs = msg.songs)
                }
            }
        }
    }

    private companion object {
        const val STORE_NAME = "FavouriteStore"
    }
}