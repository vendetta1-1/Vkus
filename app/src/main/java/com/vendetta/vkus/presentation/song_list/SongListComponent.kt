package com.vendetta.vkus.presentation.song_list

import com.vendetta.domain.entity.SongEntity
import kotlinx.coroutines.flow.StateFlow

interface SongListComponent {

    val model: StateFlow<Model>

    fun changeLikeStatus(song: SongEntity)

    fun deleteSong(song: SongEntity)

    fun addSong(path: String)

    fun playSong(songUri: String)

    data class Model(
        val songs: List<SongEntity>
    )
}