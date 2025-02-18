package com.vendetta.data.repository.playback

import android.app.Application
import com.vendetta.data.service.MusicAction
import com.vendetta.data.service.MusicForegroundService
import com.vendetta.domain.repository.PlaybackRepository

class PlaybackRepositoryImpl(
    private val application: Application
) : PlaybackRepository {

    override fun play(songUri: String) {
        application.startForegroundService(
            MusicForegroundService.newIntent(
                application,
                songUri
            )
        )
    }

    override fun pause() {
        application.startForegroundService(
            MusicForegroundService.newIntent(
                application,
                MusicAction.Pause
            )
        )
    }

    override fun seekToNext() {
        application.startForegroundService(
            MusicForegroundService.newIntent(
                application,
                MusicAction.Next
            )
        )
    }

    override fun seekToPrevious() {
        application.startForegroundService(
            MusicForegroundService.newIntent(
                application,
                MusicAction.Previous
            )
        )
    }

}