package com.vendetta.vkus.presentation.player

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.vendetta.domain.entity.SongEntity
import com.vendetta.domain.usecase.playback.ResumeOrPauseUseCase
import com.vendetta.domain.usecase.playback.SeekToNextUseCase
import com.vendetta.domain.usecase.playback.SeekToPreviousUseCase
import com.vendetta.domain.usecase.playlist.ChangeLikeStatusUseCase
import com.vendetta.vkus.presentation.player.PlayerStore.Intent
import com.vendetta.vkus.presentation.player.PlayerStore.State
import kotlinx.coroutines.launch

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
        data object ChangeLikeStatus : Intent
        data object ResumeOrPause : Intent
        data class SeekToNext(val nextSong: SongEntity) : Intent
        data class SeekToPrevious(val previousSong: SongEntity) : Intent

    }
}

class PlayerFactory(
    private val storeFactory: StoreFactory,
    private val changeLikeStatusUseCase: ChangeLikeStatusUseCase,
    private val resumeOrPauseUseCase: ResumeOrPauseUseCase,
    private val seekToNextUseCase: SeekToNextUseCase,
    private val seekToPreviousUseCase: SeekToPreviousUseCase
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
                Intent.ChangeLikeStatus -> {
                    scope.launch {
                        changeLikeStatusUseCase(state().nowPlaying)
                    }
                    dispatch(Message.ChangeLikeStatus)
                }

                Intent.ResumeOrPause -> {
                    scope.launch {
                        resumeOrPauseUseCase()
                    }
                    dispatch(Message.ResumeOrPause)
                }

                is Intent.SeekToNext -> {
                    scope.launch {
                        seekToNextUseCase()
                    }
                    dispatch(Message.SeekToNext(state().nextSong))
                }

                is Intent.SeekToPrevious -> {
                    scope.launch {
                        seekToPreviousUseCase()
                    }
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

