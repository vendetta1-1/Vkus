package com.vendetta.domain.repository

import com.vendetta.domain.entity.SongEntity
import kotlinx.coroutines.flow.StateFlow


interface PlaylistRepository {

    fun getSongs(): StateFlow<List<SongEntity>>

    suspend fun addSong(uri: String)

    suspend fun deleteSong(songId: Int)
}