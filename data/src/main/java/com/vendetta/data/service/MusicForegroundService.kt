package com.vendetta.data.service

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

class MusicForegroundService : Service() {

    private lateinit var exoPlayer: ExoPlayer

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        exoPlayer = ExoPlayer.Builder(this).build()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        startForeground(FOREGROUND_ID, createNotification())
        when (intent.action) {
            MusicAction.Play.name -> {
                val songUri =
                    intent.getStringExtra(EXTRA_SONG) ?: throw RuntimeException("uri == null")
                val mediaItem = MediaItem.fromUri(songUri)
                exoPlayer.setMediaItem(mediaItem)
                exoPlayer.prepare()
                exoPlayer.play()
            }

            MusicAction.Pause.name -> {
                exoPlayer.pause()
            }

            MusicAction.Next.name -> {
                exoPlayer.seekToNextMediaItem()
            }

            MusicAction.Previous.name -> {
                exoPlayer.seekToPreviousMediaItem()
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer.release()
    }

    private fun createNotification(): Notification {

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Воспроизведение...")
            .setContentText("Сейчас играет...")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .build()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        fun newIntent(application: Application, musicAction: MusicAction): Intent {
            return Intent(application, MusicForegroundService::class.java).apply {
                action = musicAction.name
            }
        }

        fun newIntent(
            application: Application,
            songUri: String
        ): Intent {
            return Intent(application, MusicForegroundService::class.java).apply {
                action = MusicAction.Play.name
                putExtra(EXTRA_SONG, songUri)
            }
        }


        private const val FOREGROUND_ID = 1
        private const val EXTRA_SONG = "extra_song"
        const val CHANNEL_ID = "music_channel_id"
        const val CHANNEL_NAME = "music"
    }
}