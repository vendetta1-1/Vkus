package com.vendetta.media.playback

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.core.content.edit

internal class PersistentStorage private constructor(val context: Context) {

    private var preferences: SharedPreferences = context.getSharedPreferences(
        PREFERENCES_NAME,
        Context.MODE_PRIVATE
    )

    companion object {

        @Volatile
        private var instance: PersistentStorage? = null

        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: PersistentStorage(context).also { instance = it }
            }
    }

    suspend fun saveRecentSong(mediaItem: MediaItem, position: Long) {

        withContext(Dispatchers.IO) {
            preferences.edit {
                putString(RECENT_SONG_MEDIA_ID_KEY, mediaItem.mediaId)
                    .putString(RECENT_SONG_TITLE_KEY, mediaItem.mediaMetadata.title.toString())
                    .putString(
                        RECENT_SONG_SUBTITLE_KEY,
                        mediaItem.mediaMetadata.subtitle.toString()
                    )
                    .putString(
                        RECENT_SONG_ICON_URI_KEY,
                        mediaItem.mediaMetadata.artworkUri.toString()
                    )
                    .putLong(RECENT_SONG_POSITION_KEY, position)
            }
        }
    }

    fun loadRecentSong(): MediaItem? {
        val mediaId = preferences.getString(RECENT_SONG_MEDIA_ID_KEY, null)
        return if (mediaId == null) {
            null
        } else {
            val extras = Bundle().also {
                val position = preferences.getLong(RECENT_SONG_POSITION_KEY, 0L)
                it.putLong(MEDIA_DESCRIPTION_EXTRAS_START_PLAYBACK_POSITION_MS, position)
            }
            val metadataBuilder = with(MediaMetadata.Builder()) {
                setTitle(preferences.getString(RECENT_SONG_TITLE_KEY, ""))
                setSubtitle(preferences.getString(RECENT_SONG_SUBTITLE_KEY, ""))
                setFolderType(MediaMetadata.FOLDER_TYPE_NONE)
                setIsPlayable(true)
                setArtworkUri(Uri.parse(preferences.getString(RECENT_SONG_ICON_URI_KEY, "")))
                setExtras(extras)
            }
            return with(MediaItem.Builder()) {
                setMediaId(mediaId)
                setMediaMetadata(metadataBuilder.build())
                build()
            }
        }
    }
}

const val NOTIFICATION_LARGE_ICON_SIZE = 144 // px

private const val PREFERENCES_NAME = "uamp"
private const val RECENT_SONG_MEDIA_ID_KEY = "recent_song_media_id"
private const val RECENT_SONG_TITLE_KEY = "recent_song_title"
private const val RECENT_SONG_SUBTITLE_KEY = "recent_song_subtitle"
private const val RECENT_SONG_ICON_URI_KEY = "recent_song_icon_uri"
private const val RECENT_SONG_POSITION_KEY = "recent_song_position"
