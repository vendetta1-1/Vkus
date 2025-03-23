package com.vendetta.vkus.playback

import androidx.annotation.OptIn
import androidx.media3.common.DeviceInfo
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture

class MusicService : MediaLibraryService() {

    private val player: ExoPlayer by lazy {
        ExoPlayer
            .Builder(this)
            .setHandleAudioBecomingNoisy(true)
            .build().apply {
                addListener(Listener())
            }
    }

    private lateinit var mediaLibrarySession: MediaLibrarySession

    override fun onCreate() {
        super.onCreate()
        mediaLibrarySession = MediaLibrarySession.Builder(this, player, SessionCallback())
            .build()
    }


    override fun onDestroy() {
        super.onDestroy()
        mediaLibrarySession.release()
        player.release()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaLibrarySession? {
        return if (controllerInfo.isAllowedController()) mediaLibrarySession else null
    }

    private fun MediaSession.ControllerInfo.isAllowedController(): Boolean =
        this.packageName == packageName

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

    private inner class SessionCallback : MediaLibrarySession.Callback {
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