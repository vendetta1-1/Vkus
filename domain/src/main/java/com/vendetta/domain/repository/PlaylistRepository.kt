package com.vendetta.domain.repository

import com.vendetta.domain.entity.SongEntity
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    val songs: Flow<List<SongEntity>>

    val favouriteSongs: Flow<List<SongEntity>>

    suspend fun changeLikeStatus(song: SongEntity)

    suspend fun addSong(uri: String)

    suspend fun deleteSong(song: SongEntity)
}