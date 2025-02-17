package com.vendetta.data.repository.playlist

import android.app.Application
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import com.vendetta.data.local.db.MusicDao
import com.vendetta.data.local.model.SongDbModel
import com.vendetta.data.mapper.toEntity
import com.vendetta.domain.repository.PlaylistRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@OptIn(UnstableApi::class)
class DefaultPlaylistRepository(
    private val retriever: MediaMetadataRetriever,
    private val application: Application,
    private val musicDao: MusicDao
) : PlaylistRepository {

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun getSongs() = flow {
        emit(musicDao.getSongs())
    }.map {
        it.toEntity()
    }.stateIn(
        scope = scope,
        started = SharingStarted.Lazily,
        initialValue = listOf()
    )

    override suspend fun addSong(uri: String) {
        retriever.setDataSource(application, Uri.parse(uri))
        val songName =
            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE) ?: "Untitled"
        val artist =
            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST) ?: "Artist"
        val album =
            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM) ?: "Untitled"
        val coverByteArray = retriever.embeddedPicture ?: throw RuntimeException("coverByteArray is null")
        val duration =
            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong()
                ?: 0L

        musicDao.addSong(
            SongDbModel(
                id = 1, //найти способ как увеличивать id
                uri = uri,
                durationInMillis = duration,
                coverPath = coverByteArray,
                songName = songName,
                artistName = artist,
                albumName = album
            )
        )

    }

    override suspend fun deleteSong(songId: Int) {
        musicDao.deleteSong(songId)
    }

}