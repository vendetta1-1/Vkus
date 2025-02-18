package com.vendetta.data.repository.playlist

import android.media.MediaMetadataRetriever
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import com.vendetta.data.local.db.MusicDao
import com.vendetta.data.mapper.toDbModel
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

    private val _songList = mutableListOf<SongEntity>()
    private val songList: List<SongEntity> = _songList


    private val songListChangeEvents = MutableSharedFlow<Unit>(replay = 1).apply {
        tryEmit(Unit)
    }

    override val songs: Flow<List<SongEntity>> = flow {
        songListChangeEvents.collect {
            emit(songList)
        }
    }

    override suspend fun addSong(path: String) {
        retriever.setDataSource(path)
        val songEntity = SongEntity(
            id = songList.size,
            uri = path,
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

        musicDao.addSong(songEntity.toDbModel())
        _songList[songEntity.id] = songEntity
        songListChangeEvents.tryEmit(Unit)
    }

    override suspend fun deleteSong(song: SongEntity) {
        musicDao.deleteSong(song.id)
        _songList.remove(song)
        songListChangeEvents.tryEmit(Unit)
    }

}