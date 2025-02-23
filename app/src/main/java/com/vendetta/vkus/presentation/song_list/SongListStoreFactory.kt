package com.vendetta.vkus.presentation.song_list

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.vendetta.domain.entity.SongEntity
import com.vendetta.domain.usecase.playback.PlaySongUseCase
import com.vendetta.domain.usecase.playlist.AddSongUseCase
import com.vendetta.domain.usecase.playlist.ChangeLikeStatusUseCase
import com.vendetta.domain.usecase.playlist.DeleteSongUseCase
import com.vendetta.domain.usecase.playlist.GetSongsUseCase
import kotlinx.coroutines.launch

class SongListStoreFactory(
    private val storeFactory: StoreFactory,
    private val getSongsUseCase: GetSongsUseCase,
    private val playSongUseCase: PlaySongUseCase,
    private val deleteSongUseCase: DeleteSongUseCase,
    private val addSongUseCase: AddSongUseCase,
    private val changeLikeStatusUseCase: ChangeLikeStatusUseCase
) {

    fun create(): SongListStore = object : SongListStore,
        Store<SongListStore.Intent, SongListStore.State, SongListStore.Label> by storeFactory.create(
            name = STORE_NAME,
            initialState = SongListStore.State(listOf()),
            reducer = ReducerImpl,
            executorFactory = ::Executor,
            bootstrapper = BootstrapperImpl()
        ) {}

    private sealed interface Action {
        data class SongsLoaded(val songs: List<SongEntity>) : Action
    }

    private sealed interface Message {
        data class SongsLoaded(val songs: List<SongEntity>) : Message
        data class PlaySong(val path: String) : Message
        data class DeleteSong(val song: SongEntity) : Message
        data class ChangeLikeStatus(val song: SongEntity) : Message
        data class AddSong(val song: SongEntity) : Message
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {

        override fun invoke() {
            scope.launch {
                getSongsUseCase().collect {
                    dispatch(Action.SongsLoaded(it))
                }
            }
        }
    }

    private inner class Executor :
        CoroutineExecutor<SongListStore.Intent, Action, SongListStore.State, Message, SongListStore.Label>() {

        override fun executeAction(action: Action) {
            when (action) {
                is Action.SongsLoaded -> {
                    dispatch(Message.SongsLoaded(action.songs))
                }
            }
        }

        override fun executeIntent(intent: SongListStore.Intent) {
            when (intent) {
                is SongListStore.Intent.ChangeLikeStatus -> {
                    scope.launch {
                        changeLikeStatusUseCase(intent.song)
                    }
                    dispatch(Message.ChangeLikeStatus(intent.song))
                }

                is SongListStore.Intent.DeleteSong -> {
                    scope.launch {
                        deleteSongUseCase(intent.song)
                    }
                    dispatch(Message.DeleteSong(intent.song))
                }

                is SongListStore.Intent.PlaySong -> {
                    scope.launch {
                        playSongUseCase(intent.path)
                    }
                    dispatch(Message.PlaySong(intent.path))
                }

                is SongListStore.Intent.AddSong -> {
                    scope.launch {
                        addSongUseCase(intent.path)
                    }
                    publish(SongListStore.Label.AddSong(intent.path))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<SongListStore.State, Message> {

        override fun SongListStore.State.reduce(msg: Message): SongListStore.State {
            return when (msg) {
                is Message.ChangeLikeStatus -> {
                    copy()
                }

                is Message.DeleteSong -> {
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

                is Message.AddSong -> {
                    copy(
                        songs = this.songs.toMutableList().apply {
                            this[msg.song.id] = msg.song
                        }
                    )
                }
            }

        }
    }


    private companion object {
        const val STORE_NAME = "SongListStore"
    }
}