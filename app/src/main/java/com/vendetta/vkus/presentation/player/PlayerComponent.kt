package com.vendetta.vkus.presentation.player

import com.vendetta.domain.entity.SongEntity
import kotlinx.coroutines.flow.StateFlow

interface PlayerComponent {

    val model: StateFlow<PlayerStore.State>

    fun playOrPause()

    fun changeLikeStatus()

    fun seekToNext(nextSong: SongEntity)

    fun seekToPrevious(previousSong: SongEntity)

    fun onDismiss()
}