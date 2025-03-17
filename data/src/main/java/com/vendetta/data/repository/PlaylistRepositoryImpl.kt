package com.vendetta.data.repository

import android.app.Application
import android.media.MediaMetadataRetriever
import android.util.Log
import androidx.core.net.toUri
import com.vendetta.data.local.db.MusicDao
import com.vendetta.data.mapper.toDbModel
import com.vendetta.data.mapper.toEntity
import com.vendetta.domain.entity.SongEntity
import com.vendetta.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import kotlin.random.Random

class PlaylistRepositoryImpl @Inject constructor(
    private val retriever: MediaMetadataRetriever,
    private val musicDao: MusicDao,
    private val application: Application
) : PlaylistRepository {


    override val songs: Flow<List<SongEntity>> = musicDao.getSongs()
        .map { it.toEntity() }
        .onEach { Log.d("SONGS", it.toString()) }

    override val favouriteSongs: Flow<List<SongEntity>> =
        musicDao.getFavouriteSongs().map { it.toEntity() }

    override suspend fun changeLikeStatus(song: SongEntity) =
        musicDao.addSong(
            song.copy(
                isFavourite = !song.isFavourite
            ).toDbModel()
        )

    override suspend fun addSong(uri: String) {
        retriever.setDataSource(application, uri.toUri())
        val song = SongEntity(
            id = Random.nextInt(),
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
    }

    override suspend fun deleteSong(song: SongEntity) = musicDao.deleteSong(song.id)
}