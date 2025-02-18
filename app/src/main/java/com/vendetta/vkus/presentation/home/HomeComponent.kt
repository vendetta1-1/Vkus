package com.vendetta.vkus.presentation.home

import com.vendetta.domain.entity.SongEntity
import kotlinx.coroutines.flow.StateFlow

interface HomeComponent {

    val model: StateFlow<Model>

    fun deleteSong(song: SongEntity)

    fun addSong(path: String)

    fun playSong(songUri: String)

    data class Model(
        val songs: List<SongEntity>
    )
}