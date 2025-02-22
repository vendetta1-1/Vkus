package com.vendetta.vkus.presentation.player

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.statekeeper.consume
import com.vendetta.domain.entity.SongEntity
import com.vendetta.domain.usecase.playback.PauseUseCase
import com.vendetta.domain.usecase.playback.PlaySongUseCase
import com.vendetta.domain.usecase.playback.SeekToNextUseCase
import com.vendetta.domain.usecase.playback.SeekToPreviousUseCase
import com.vendetta.domain.usecase.playlist.ChangeLikeStatusUseCase
import com.vendetta.vkus.core.componentScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DefaultPlayerComponent(
    componentContext: ComponentContext,
    private val songEntity: SongEntity,
    private val changeLikeStatusUseCase: ChangeLikeStatusUseCase,
    private val playSongUseCase: PlaySongUseCase,
    private val seekToNextUseCase: SeekToNextUseCase,
    private val seekToPreviousUseCase: SeekToPreviousUseCase,
    private val pauseUseCase: PauseUseCase
) : PlayerComponent, ComponentContext by componentContext {

    private val scope = componentScope()

    private val _model = MutableStateFlow(
        stateKeeper.consume(KEY) ?: PlayerComponent.Model(songEntity)
    )

    override val model: StateFlow<PlayerComponent.Model> = _model.asStateFlow()

    init {
        stateKeeper.register(KEY) {
            model.value
        }
    }

    override fun play(path: String) {
        playSongUseCase(path)
    }

    override fun changeLikeStatus() {
        scope.launch {
            changeLikeStatusUseCase(songEntity)
        }
    }

    override fun pause() {
        pauseUseCase()
    }

    override fun seekToNext() {
        seekToNextUseCase()
    }

    override fun seekToPrevious() {
        seekToPreviousUseCase()
    }

    companion object {
        private const val KEY = "song"
    }
}