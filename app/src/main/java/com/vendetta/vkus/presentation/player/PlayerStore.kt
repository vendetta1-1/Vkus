package com.vendetta.vkus.presentation.player

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.vendetta.domain.entity.SongEntity
import com.vendetta.domain.usecase.ChangeLikeStatusUseCase
import com.vendetta.vkus.presentation.player.PlayerStore.Intent
import com.vendetta.vkus.presentation.player.PlayerStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

interface PlayerStore : Store<Intent, State, Nothing> {

    data class State(
        val nowPlaying: SongEntity,
        val isFavourite: Boolean,
        val nextSong: SongEntity,
        val previousSong: SongEntity,
        val progress: Long = 0,
        val isPause: Boolean = false
    )

    sealed interface Intent {
        data class ChangeLikeStatus(val song: SongEntity) : Intent
        data object ResumeOrPause : Intent
        data class SeekToNext(val nextSong: SongEntity) : Intent
        data class SeekToPrevious(val previousSong: SongEntity) : Intent
    }
}

class PlayerFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val changeLikeStatusUseCase: ChangeLikeStatusUseCase,
) {

    fun create(song: SongEntity, nextSong: SongEntity, previousSong: SongEntity): PlayerStore =
        object : PlayerStore, Store<Intent, State, Nothing> by storeFactory.create(
            name = STORE_NAME,
            initialState = State(
                nowPlaying = song,
                isFavourite = song.isFavourite,
                nextSong = nextSong,
                previousSong = previousSong
            ),
            executorFactory = ::Executor,
            reducer = ReducerImpl
        ) {}


    private sealed interface Action

    private sealed interface Message {
        data object ResumeOrPause : Message
        data object ChangeLikeStatus : Message
        data class SeekToNext(val nextSong: SongEntity) : Message
        data class SeekToPrevious(val previousSong: SongEntity) : Message
    }

    private inner class Executor :
        CoroutineExecutor<Intent, Action, State, Message, Nothing>() {

        override fun executeIntent(intent: Intent) {
            when (intent) {
                is Intent.ChangeLikeStatus -> {
                    scope.launch {
                        changeLikeStatusUseCase(intent.song)
                    }
                    dispatch(Message.ChangeLikeStatus)
                }

                Intent.ResumeOrPause -> {
                    dispatch(Message.ResumeOrPause)
                }

                is Intent.SeekToNext -> {
                    dispatch(Message.SeekToNext(state().nextSong))
                }

                is Intent.SeekToPrevious -> {
                    dispatch(Message.SeekToPrevious(state().previousSong))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Message> {

        override fun State.reduce(msg: Message): State {
            return when (msg) {
                Message.ChangeLikeStatus -> {
                    copy(isFavourite = !isFavourite)
                }

                Message.ResumeOrPause -> {
                    copy(isPause = !isPause)
                }

                is Message.SeekToNext -> {
                    copy(nowPlaying = msg.nextSong)
                }

                is Message.SeekToPrevious -> {
                    copy(nowPlaying = msg.previousSong)
                }
            }
        }
    }

    private companion object {
        const val STORE_NAME = "PlayerStore"
    }
}

