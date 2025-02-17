package com.vendetta.data.repository.playback

import android.app.Application
import com.vendetta.data.service.MusicAction
import com.vendetta.data.service.MusicForegroundService
import com.vendetta.domain.repository.PlaybackRepository

class DefaultPlaybackRepository(
    private val application: Application
) : PlaybackRepository {

    override suspend fun play(songUri: String) {
        application.startForegroundService(
            MusicForegroundService.newIntent(
                application,
                songUri
            )
        )
    }

    override suspend fun pause() {
        application.startForegroundService(
            MusicForegroundService.newIntent(
                application,
                MusicAction.Pause
            )
        )
    }

    override suspend fun seekToNext() {
        application.startForegroundService(
            MusicForegroundService.newIntent(
                application,
                MusicAction.Next
            )
        )
    }

    override suspend fun seekToPrevious() {
        application.startForegroundService(
            MusicForegroundService.newIntent(
                application,
                MusicAction.Previous
            )
        )
    }

}