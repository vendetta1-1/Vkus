//package com.vendetta.data.service
//
//import android.Manifest
//import android.app.Application
//import android.app.PendingIntent
//import android.app.Service
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.os.IBinder
//import androidx.core.app.NotificationCompat
//import androidx.core.content.ContextCompat.checkSelfPermission
//import androidx.media3.common.MediaItem
//import androidx.media3.exoplayer.ExoPlayer
//import com.google.gson.Gson
//import com.vendetta.data.R
//import com.vendetta.data.local.db.MusicDao
//import com.vendetta.data.mapper.toEntity
//import com.vendetta.domain.entity.SongEntity
//import kotlinx.coroutines.flow.MutableStateFlow
//import org.koin.android.ext.android.get
//
//
//class MusicPlayerService : Service() {
//
//    override fun onBind(intent: Intent?): IBinder? {
//        return null
//    }
//
//    private lateinit var exoPlayer: ExoPlayer
//
//   private val musicDao: MusicDao = get()
//
//    private val songs = musicDao.getSongs().toEntity()
//
//    private val currentSong = MutableStateFlow(songs[0])
//
//    override fun onCreate() {
//        super.onCreate()
//        exoPlayer = ExoPlayer.Builder(this).build()
//    }
//
//    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
//        when (intent.action) {
//            MusicPlayerAction.Play.title -> play(
//                Gson().fromJson(
//                    intent.getStringExtra(SONG_EXTRA),
//                    SongEntity::class.java
//                )
//            )
//
//            MusicPlayerAction.Next.title -> seekToNext()
//            MusicPlayerAction.Previous.title -> seekToPrevious()
//            MusicPlayerAction.ResumeOrPause.title -> playOrPause()
//            else -> {}
//        }
//        return START_STICKY
//    }
//
//    private fun play(song: SongEntity) {
//        exoPlayer.apply {
//            setMediaItem(MediaItem.fromUri(song.uri))
//            prepare()
//            play()
//        }
//        currentSong.tryEmit(song)
//        sendNotification(song)
//    }
//
//    private fun seekToPrevious() {
//        val currentSongIndex = songs.indexOf(currentSong.value)
//        val nextSong = if (currentSongIndex == songs.lastIndex) {
//            songs[0]
//        } else {
//            songs[currentSongIndex + 1]
//        }
//        exoPlayer.apply {
//            setMediaItem(MediaItem.fromUri(nextSong.uri))
//            prepare()
//            play()
//        }
//        currentSong.tryEmit(nextSong)
//        sendNotification(nextSong)
//    }
//
//    private fun seekToNext() {
//        val currentSongIndex = songs.indexOf(currentSong.value)
//        val previousSong = if (currentSongIndex == 0) {
//            songs[songs.lastIndex]
//        } else {
//            songs[currentSongIndex - 1]
//        }
//        exoPlayer.apply {
//            setMediaItem(MediaItem.fromUri(previousSong.uri))
//            prepare()
//            play()
//        }
//        currentSong.tryEmit(previousSong)
//        sendNotification(previousSong)
//    }
//
//    private fun playOrPause() {
//        if (exoPlayer.isPlaying) {
//            exoPlayer.pause()
//        } else {
//            exoPlayer.play()
//        }
//    }
//
//    private fun sendNotification(song: SongEntity) {
//        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
//            .setContentTitle(song.songName)
//            .setContentText(song.artistName)
//            .addAction(
//                R.drawable.ic_previous,
//                MusicPlayerAction.Previous.title,
//                createPendingIntent(MusicPlayerAction.Previous)
//            )
//            .addAction(
//                if (exoPlayer.isPlaying) R.drawable.ic_pause else R.drawable.ic_play,
//                MusicPlayerAction.ResumeOrPause.title,
//                createPendingIntent(MusicPlayerAction.ResumeOrPause)
//            )
//            .addAction(
//                R.drawable.ic_next,
//                MusicPlayerAction.Next.title,
//                createPendingIntent(MusicPlayerAction.Next)
//            )
//            .setSmallIcon(R.drawable.ic_launcher_background)
//            .build()
//        if (checkSelfPermission(
//                this,
//                Manifest.permission.POST_NOTIFICATIONS
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            startForeground(SERVICE_ID, notification)
//        }
//    }
//
//    private fun createPendingIntent(musicPlayerAction: MusicPlayerAction): PendingIntent {
//        val intent = Intent(this, MusicPlayerService::class.java).apply {
//            action = musicPlayerAction.title
//        }
//        return PendingIntent.getService(
//            this,
//            RC_CODE,
//            intent,
//            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
//        )
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        exoPlayer.release()
//    }
//
//    companion object {
//        private const val SERVICE_ID = 1
//        private const val RC_CODE = 0
//        private const val SONG_EXTRA = "song"
//        const val CHANNEL_ID = "music_id"
//        const val CHANNEL_NAME = "music_channel"
//
//        fun newIntent(application: Application, action: MusicPlayerAction): Intent =
//            Intent(application, MusicPlayerService::class.java).apply {
//                this.action = action.title
//            }
//
//        fun newIntent(
//            application: Application,
//            action: MusicPlayerAction,
//            song: SongEntity
//        ): Intent =
//            Intent(application, MusicPlayerService::class.java).apply {
//                this.action = action.title
//                putExtra(
//                    SONG_EXTRA, Gson().toJson(song)
//                )
//            }
//    }
//}