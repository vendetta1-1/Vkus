package com.vendetta.vkus.presentation.player

import android.os.Parcelable
import com.vendetta.domain.entity.SongEntity
import kotlinx.coroutines.flow.StateFlow
import kotlinx.parcelize.Parcelize

interface PlayerComponent {

    val model: StateFlow<Model>

    fun play(path: String)

    fun changeLikeStatus()

    fun pause()

    fun seekToNext()

    fun seekToPrevious()

    @Parcelize
    data class Model(
        val song: SongEntity
    ) : Parcelable
}