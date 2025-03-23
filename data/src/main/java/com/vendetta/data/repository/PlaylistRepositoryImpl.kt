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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlaylistRepositoryImpl @Inject constructor(
    private val retriever: MediaMetadataRetriever,
    private val musicDao: MusicDao,
    private val application: Application
) : PlaylistRepository {

    private val scope = CoroutineScope(Dispatchers.IO)

    private lateinit var songsList: MutableList<SongEntity>
    private lateinit var favouriteSongList: MutableList<SongEntity>

    init {
        scope.launch {
            songsList = musicDao.getSongs().toEntity().toMutableList()
            favouriteSongList = musicDao.getFavouriteSongs().toEntity().toMutableList()
        }
    }

    private val songListChangeEvents = MutableSharedFlow<Unit>(replay = 1).apply {
        tryEmit(Unit)
    }

    override val songs: Flow<List<SongEntity>> = flow {
        songListChangeEvents.collect {
            emit(songsList)
        }
    }
    override val favouriteSongs: Flow<List<SongEntity>> = flow {
        songListChangeEvents.collect {
            emit(favouriteSongList)
        }
    }

    override suspend fun changeLikeStatus(song: SongEntity) {
        musicDao.addSong(song.copy(isFavourite = !song.isFavourite).toDbModel())
        songListChangeEvents.tryEmit(Unit)
    }

    override suspend fun addSong(uri: String) {
        retriever.setDataSource(application, uri.toUri())
        val song = SongEntity(
            id = songsList.size,
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
        songsList.add(song.id, song)
        songListChangeEvents.tryEmit(Unit)
    }

    override suspend fun deleteSong(song: SongEntity) {
        musicDao.deleteSong(song.id)
        songsList.remove(song)
        songListChangeEvents.tryEmit(Unit)
    }
}