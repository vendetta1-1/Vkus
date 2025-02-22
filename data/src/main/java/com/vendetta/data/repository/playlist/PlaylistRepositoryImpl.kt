package com.vendetta.data.repository.playlist

import android.media.MediaMetadataRetriever
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import com.vendetta.data.local.db.MusicDao
import com.vendetta.data.local.model.SongDbModel
import com.vendetta.data.mapper.toDbModel
import com.vendetta.data.mapper.toEntity
import com.vendetta.domain.entity.SongEntity
import com.vendetta.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow

@OptIn(UnstableApi::class)
class PlaylistRepositoryImpl(
    private val retriever: MediaMetadataRetriever,
    private val musicDao: MusicDao
) : PlaylistRepository {

    private val songsList = musicDao.getSongs().toMutableList()

    private val songListChangeEvents = MutableSharedFlow<Unit>(replay = 1).apply {
        tryEmit(Unit)
    }

    override val songs: Flow<List<SongEntity>> = flow {
        songListChangeEvents.collect {
            emit(songsList.toEntity())
        }
    }
    override val favouriteSongs: Flow<List<SongEntity>> = flow {
        songListChangeEvents.collect {
            emit(songsList.filter { it.isFavourite }.toEntity())
        }
    }

    override suspend fun changeLikeStatus(song: SongEntity) {
        musicDao.addSong(song.copy(isFavourite = !song.isFavourite).toDbModel())
        songListChangeEvents.tryEmit(Unit)
    }

    override suspend fun addSong(path: String) {
        retriever.setDataSource(path)
        val song = SongDbModel(
            id = songsList.size,
            uri = path,
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
        musicDao.addSong(song)
        songsList[song.id] = song
        songListChangeEvents.tryEmit(Unit)
    }

    override suspend fun deleteSong(song: SongEntity) {
        musicDao.deleteSong(song.id)
        songsList.remove(song.toDbModel())
        songListChangeEvents.tryEmit(Unit)
    }
}