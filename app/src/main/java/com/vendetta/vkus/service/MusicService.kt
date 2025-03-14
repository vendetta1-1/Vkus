package com.vendetta.vkus.service

import android.media.browse.MediaBrowser
import android.media.session.MediaSession
import android.os.Bundle
import android.service.media.MediaBrowserService

class MusicService : MediaBrowserService() {

    private lateinit var mediaSession: MediaSession

    override fun onCreate() {
        super.onCreate()
        mediaSession = MediaSession(this, SESSION_TAG).apply {
            isActive = true
        }
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot? {
        return if (this.packageName == clientPackageName) {
            BrowserRoot(ROOT_ID, null)
        } else {
            null
        }
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<List<MediaBrowser.MediaItem?>?>
    ) {
        TODO("Not yet implemented")
    }

    private companion object {
        const val ROOT_ID = "service_root_id"
        const val SESSION_TAG = "media_session_tag"
    }
}