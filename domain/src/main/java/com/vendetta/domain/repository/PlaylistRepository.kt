package com.vendetta.domain.repository

import com.vendetta.domain.entity.SongEntity
import kotlinx.coroutines.flow.StateFlow

interface PlaylistRepository {

    val songs: StateFlow<List<SongEntity>>

    val favouriteSongs: StateFlow<List<SongEntity>>

    suspend fun changeLikeStatus(song: SongEntity)

    suspend fun addSong(uri: String): SongEntity

    suspend fun deleteSong(song: SongEntity)
}