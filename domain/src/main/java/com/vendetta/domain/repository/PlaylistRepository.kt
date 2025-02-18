package com.vendetta.domain.repository

import com.vendetta.domain.entity.SongEntity
import kotlinx.coroutines.flow.Flow


interface PlaylistRepository {

    val songs: Flow<List<SongEntity>>

    suspend fun addSong(path: String)

    suspend fun deleteSong(song: SongEntity)
}