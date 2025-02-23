package com.vendetta.vkus.presentation.favourite

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.vendetta.domain.entity.SongEntity
import com.vendetta.domain.usecase.playback.PlaySongUseCase
import com.vendetta.domain.usecase.playlist.ChangeLikeStatusUseCase
import com.vendetta.domain.usecase.playlist.GetFavouriteSongsUseCase
import kotlinx.coroutines.launch

class FavouriteStoreFactory(
    private val storeFactory: StoreFactory,
    private val playSongUseCase: PlaySongUseCase,
    private val changeLikeStatusUseCase: ChangeLikeStatusUseCase,
    private val getFavouriteSongsUseCase: GetFavouriteSongsUseCase
) {

    fun create(): FavouriteStore = object : FavouriteStore,
        Store<FavouriteStore.Intent, FavouriteStore.State, Nothing> by storeFactory.create(
            name = STORE_NAME,
            initialState = FavouriteStore.State(listOf()),
            reducer = ReducerImpl,
            executorFactory = ::ExecutorImpl,
            bootstrapper = Bootstrapper()
        ) {}

    private sealed interface Action {
        data class SongsLoaded(val songs: List<SongEntity>) : Action
    }

    private sealed interface Message {
        data class SongsLoaded(val songs: List<SongEntity>) : Message
        data class PlaySong(val path: String) : Message
        data class ChangeLikeStatus(val song: SongEntity) : Message
    }

    private inner class Bootstrapper : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                getFavouriteSongsUseCase().collect {
                    dispatch(Action.SongsLoaded(it))
                }
            }
        }
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<FavouriteStore.Intent, Action, FavouriteStore.State, Message, Nothing>() {
        override fun executeAction(action: Action) {
            when (action) {
                is Action.SongsLoaded -> {
                    dispatch(Message.SongsLoaded(action.songs))
                }
            }
        }

        override fun executeIntent(intent: FavouriteStore.Intent) {
            when (intent) {
                is FavouriteStore.Intent.ChangeLikeStatus -> {
                    scope.launch {
                        changeLikeStatusUseCase(intent.song)
                    }
                    dispatch(Message.ChangeLikeStatus(intent.song))
                }

                is FavouriteStore.Intent.PlaySong -> {
                    scope.launch {
                        playSongUseCase(intent.path)
                    }
                    dispatch(Message.PlaySong(intent.path))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<FavouriteStore.State, Message> {

        override fun FavouriteStore.State.reduce(msg: Message): FavouriteStore.State {
            return when (msg) {
                is Message.ChangeLikeStatus -> {
                    copy(
                        songs = this.songs.toMutableList().apply {
                            remove(msg.song)
                        }
                    )
                }

                is Message.PlaySong -> {
                    copy(nowPlayingPath = msg.path)
                }

                is Message.SongsLoaded -> {
                    copy(songs = msg.songs)
                }
            }
        }
    }

    private companion object {
        const val STORE_NAME = "FavouriteStore"
    }
}