package com.vendetta.vkus.presentation.song_list

import android.net.Uri
import com.vendetta.domain.entity.SongEntity
import kotlinx.coroutines.flow.StateFlow

interface SongListComponent {

    val model: StateFlow<SongListStore.State>

    fun changeLikeStatus(song: SongEntity)

    fun deleteSong(song: SongEntity)

    fun addSong(uri: Uri)

    fun playSong(song: SongEntity)

}