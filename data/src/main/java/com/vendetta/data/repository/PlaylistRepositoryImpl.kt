package com.vendetta.data.repository

import android.app.Application
import android.media.MediaMetadataRetriever
import androidx.annotation.OptIn
import androidx.core.net.toUri
import androidx.media3.common.util.UnstableApi
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

@OptIn(UnstableApi::class)
class PlaylistRepositoryImpl @Inject constructor(
    private val retriever: MediaMetadataRetriever,
    private val musicDao: MusicDao,
    private val application: Application
) : PlaylistRepository {

    private val scope = CoroutineScope(Dispatchers.IO)

    private lateinit var songList: MutableList<SongEntity>

    private lateinit var favouriteSongList: MutableList<SongEntity>

    init {
        scope.launch {
            songList = musicDao.getSongs().toEntity().toMutableList()
            favouriteSongList = musicDao.getFavouriteSongs().toEntity().toMutableList()
        }
    }

    private val songListChangeEvents = MutableSharedFlow<Unit>(replay = 1).apply {
        tryEmit(Unit)
    }

    private val favouriteSongListChangeEvents = MutableSharedFlow<Unit>(replay = 1)

    override val songs: Flow<List<SongEntity>> = flow {
        songListChangeEvents.collect {
            emit(songList)
        }
    }
    override val favouriteSongs: Flow<List<SongEntity>> = flow {
        favouriteSongListChangeEvents.collect {
            emit(favouriteSongList)
        }
    }

    override suspend fun changeLikeStatus(song: SongEntity) {
        musicDao.addSong(song.copy(isFavourite = !song.isFavourite).toDbModel())
        favouriteSongList.add(song)
        songListChangeEvents.tryEmit(Unit)
        favouriteSongListChangeEvents.tryEmit(Unit)
    }

    override suspend fun addSong(uri: String) {
        retriever.setDataSource(application, uri.toUri())
        val song = SongEntity(
            id = songList.size,
            uri = uri.toString(),
            isFavourite = false,
            durationInMillis = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                ?.toLong() ?: throw RuntimeException("duration == null"),
            coverBitmap = retriever.embeddedPicture ?: throw RuntimeException("cover == null"),
            songName = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                ?: throw RuntimeException("songName == null"),
            artistName = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
                ?: throw RuntimeException("artistName == null"),
            albumName = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)
                ?: throw RuntimeException("albumName == null")
        )
        musicDao.addSong(song.toDbModel())
        songList[song.id] = song
        songListChangeEvents.tryEmit(Unit)
    }

    override suspend fun deleteSong(song: SongEntity) {
        musicDao.deleteSong(song.id)
        songList.remove(song)
        songListChangeEvents.tryEmit(Unit)
    }
}