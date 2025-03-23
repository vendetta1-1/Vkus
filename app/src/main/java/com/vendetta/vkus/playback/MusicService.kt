package com.vendetta.vkus.playback

import androidx.annotation.OptIn
import androidx.media3.common.DeviceInfo
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture

class MusicService : MediaSessionService() {

    private val player: ExoPlayer by lazy {
        ExoPlayer
            .Builder(this)
            .setHandleAudioBecomingNoisy(true)
            .build().apply {
                addListener(Listener())
            }
    }

    private lateinit var mediaSession: MediaSession

    override fun onCreate() {
        super.onCreate()
        mediaSession = MediaSession.Builder(this, player)
            .setCallback(SessionCallback())
            .build()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return if (isAllowedController(controllerInfo)) mediaSession else null
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaSession.release()
        player.release()
    }

    private fun isAllowedController(controllerInfo: MediaSession.ControllerInfo): Boolean =
        controllerInfo.packageName == packageName

    private inner class Listener : Player.Listener {

        override fun onDeviceInfoChanged(deviceInfo: DeviceInfo) {
            super.onDeviceInfoChanged(deviceInfo)
            when (deviceInfo.playbackType) {
                DeviceInfo.PLAYBACK_TYPE_LOCAL -> {
                    player.pause()
                }

                DeviceInfo.PLAYBACK_TYPE_REMOTE -> {
                    player.play()
                }
            }
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            when (playbackState) {
                Player.STATE_BUFFERING -> {
                    player.pause()
                }

                Player.STATE_ENDED -> {
                    player.seekToNext()
                }

                Player.STATE_IDLE -> {
                    player.prepare()
                }

                Player.STATE_READY -> {
                    player.play()
                }
            }
        }
    }

    private inner class SessionCallback : MediaSession.Callback {
        @OptIn(UnstableApi::class)
        override fun onSetMediaItems(
            mediaSession: MediaSession,
            controller: MediaSession.ControllerInfo,
            mediaItems: List<MediaItem>,
            startIndex: Int,
            startPositionMs: Long
        ): ListenableFuture<MediaSession.MediaItemsWithStartPosition> {
            player.setMediaItems(mediaItems)
            player.prepare()
            return Futures.immediateFuture(
                MediaSession.MediaItemsWithStartPosition(
                    mediaItems,
                    startIndex,
                    startPositionMs
                )
            )
        }

        override fun onAddMediaItems(
            mediaSession: MediaSession,
            controller: MediaSession.ControllerInfo,
            mediaItems: List<MediaItem>
        ): ListenableFuture<List<MediaItem>> {
            player.addMediaItems(mediaItems)
            player.prepare()
            return Futures.immediateFuture(mediaItems)
        }
    }
}