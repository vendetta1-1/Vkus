package com.vendetta.domain.repository.playlist

import android.net.Uri
import com.vendetta.domain.entity.SongEntity
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    val songs: Flow<List<SongEntity>>

    val favouriteSongs: Flow<List<SongEntity>>

    suspend fun changeLikeStatus(song: SongEntity)

    suspend fun addSong(uri: Uri)

    suspend fun deleteSong(song: SongEntity)
}