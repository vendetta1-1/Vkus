package com.vendetta.data.repository.playback

import android.app.Application
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.vendetta.data.service.MusicForegroundService
import com.vendetta.domain.repository.PlaybackRepository

class PlaybackRepositoryImpl(
    private val application: Application
) : PlaybackRepository {

    private lateinit var exoPlayer: ExoPlayer

    @OptIn(UnstableApi::class)
    override fun play(songUri: String) {
        val mediaDataSourceFactory: DataSource.Factory = DefaultDataSource.Factory(application)

        val mediaSource = ProgressiveMediaSource.Factory(mediaDataSourceFactory)
            .createMediaSource(MediaItem.fromUri(songUri))

        val mediaSourceFactory = DefaultMediaSourceFactory(application)

        exoPlayer = ExoPlayer.Builder(application)
            .setMediaSourceFactory(mediaSourceFactory)
            .build()

        exoPlayer.addMediaSource(mediaSource)
        exoPlayer.prepare()
        exoPlayer.play()
    }

    override fun pause() {
        application.startForegroundService(
            MusicForegroundService.newIntent(
                application,
                MusicForegroundService.MusicAction.Pause
            )
        )
    }

    override fun seekToNext() {
        application.startForegroundService(
            MusicForegroundService.newIntent(
                application,
                MusicForegroundService.MusicAction.Next
            )
        )
    }

    override fun seekToPrevious() {
        application.startForegroundService(
            MusicForegroundService.newIntent(
                application,
                MusicForegroundService.MusicAction.Previous
            )
        )
    }
}