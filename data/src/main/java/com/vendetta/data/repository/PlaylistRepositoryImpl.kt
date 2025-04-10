package com.vendetta.data.repository

import android.app.Application
import android.media.MediaMetadataRetriever
import androidx.core.net.toUri
import com.vendetta.data.local.db.MusicDao
import com.vendetta.data.mapper.toDbModel
import com.vendetta.data.mapper.toEntity
import com.vendetta.domain.entity.SongEntity
import com.vendetta.domain.repository.PlaylistRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class PlaylistRepositoryImpl @Inject constructor(
    private val retriever: MediaMetadataRetriever,
    private val musicDao: MusicDao,
    private val application: Application
) : PlaylistRepository {

    private val scope = CoroutineScope(Dispatchers.IO)

    override val songs: StateFlow<List<SongEntity>> = musicDao.getSongs()
        .map { list -> list.toEntity().sortedBy { it.id } }
        .stateIn(
            scope = scope,
            started = SharingStarted.Lazily,
            initialValue = listOf()
        )

    override val favouriteSongs: StateFlow<List<SongEntity>> = musicDao.getFavouriteSongs()
        .map { list -> list.toEntity().sortedBy { it.id } }
        .stateIn(
            scope = scope,
            started = SharingStarted.Lazily,
            initialValue = listOf()
        )

    override suspend fun changeLikeStatus(song: SongEntity) {
        musicDao.addSong(song.copy(isFavourite = !song.isFavourite).toDbModel())
    }

    override suspend fun addSong(uri: String): SongEntity {
        retriever.setDataSource(application, uri.toUri())
        val song = SongEntity(
            id = songs.value.size,
            uri = uri,
            isFavourite = false,
            durationInMillis = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                ?.toLong() ?: throw RuntimeException("duration == null"),
            coverBitmap = retriever.embeddedPicture ?: throw RuntimeException("cover == null"),
            songName = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                ?: "Untitled",
            artistName = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
                ?: "Unknown",
            albumName = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)
                ?: "Untitled"
        )
        musicDao.addSong(song.toDbModel())
        return song
    }

    override suspend fun deleteSong(song: SongEntity) {
        musicDao.deleteSong(song.id)
    }
}