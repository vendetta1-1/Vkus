package com.vendetta.vkus.presentation.song_list

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.vendetta.domain.entity.SongEntity
import com.vendetta.domain.usecase.AddSongUseCase
import com.vendetta.domain.usecase.ChangeLikeStatusUseCase
import com.vendetta.domain.usecase.DeleteSongUseCase
import com.vendetta.domain.usecase.GetSongsUseCase
import com.vendetta.vkus.presentation.song_list.SongListStore.Intent
import com.vendetta.vkus.presentation.song_list.SongListStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

interface SongListStore : Store<Intent, State, Nothing> {

    data class State(
        val songs: List<SongEntity> = listOf(), val nowPlayingSong: SongEntity? = null
    )

    sealed interface Intent {
        data class PlaySong(val song: SongEntity) : Intent
        data class DeleteSong(val song: SongEntity) : Intent
        data class ChangeLikeStatus(val song: SongEntity) : Intent
        data class AddSong(val uri: String) : Intent
    }

}

class SongListFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getSongsUseCase: GetSongsUseCase,
    private val deleteSongUseCase: DeleteSongUseCase,
    private val addSongUseCase: AddSongUseCase,
    private val changeLikeStatusUseCase: ChangeLikeStatusUseCase
) {

    fun create(): SongListStore =
        object : SongListStore, Store<Intent, State, Nothing> by storeFactory.create(
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

    private inner class Executor : CoroutineExecutor<Intent, Action, State, Message, Nothing>() {

        override fun executeAction(action: Action) {
            when (action) {
                is Action.SongsLoaded -> {
                    dispatch(Message.SongsLoaded(action.songs))
                }
            }
        }

        override fun executeIntent(intent: Intent) {
            when (intent) {
                is Intent.ChangeLikeStatus -> {
                    scope.launch {
                        changeLikeStatusUseCase(intent.song)
                    }
                    dispatch(Message.ChangeLikeStatus(intent.song))
                }

                is Intent.DeleteSong -> {
                    scope.launch {
                        deleteSongUseCase(intent.song)
                    }
                    dispatch(Message.DeleteSong(intent.song))
                }

                is Intent.PlaySong -> {
                    dispatch(Message.PlaySong(intent.song))
                }

                is Intent.AddSong -> {
                    scope.launch {
                        dispatch(Message.AddSong(addSongUseCase(intent.uri)))
                    }
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Message> {

        override fun State.reduce(msg: Message): State {
            return when (msg) {
                is Message.ChangeLikeStatus -> {
                    val oldSong = msg.song
                    val index = songs.indexOf(oldSong)
                    val newList = songs.toMutableList().apply {
                        this[index] = oldSong.copy(isFavourite = !oldSong.isFavourite)
                    }
                    copy(
                        songs = newList
                    )
                }

                is Message.DeleteSong -> {
                    val newList = songs.toMutableList().apply {
                        remove(msg.song)
                    }
                    copy(
                        songs = newList
                    )
                }

                is Message.PlaySong -> copy(nowPlayingSong = msg.song)

                is Message.SongsLoaded -> {
                    copy(songs = msg.songs)
                }

                is Message.AddSong -> {
                    copy(
                        songs = songs.toMutableList().apply {
                            add(msg.song)
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
