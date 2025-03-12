package com.vendetta.data.repository

import android.app.Application
import com.vendetta.data.service.MusicPlayerAction
import com.vendetta.domain.entity.SongEntity
import com.vendetta.domain.repository.playback.PlaybackRepository

class PlaybackRepositoryImpl(
    private val application: Application
) : PlaybackRepository {


    override fun play(song: SongEntity) {
//        application.startService(
//            MusicPlayerService.newIntent(
//                application,
//                MusicPlayerAction.Play,
//                song
//            )
//        )
    }

    override fun resumeOrPause() {
//        application.startService(
//            MusicPlayerService.newIntent(
//                application,
//                MusicPlayerAction.ResumeOrPause
//            )
//        )
    }

    override fun seekToNext() {
//        application.startService(
//            MusicPlayerService.newIntent(
//                application,
//                MusicPlayerAction.Next
//            )
//        )
    }

    override fun seekToPrevious() {
//        application.startService(
//            MusicPlayerService.newIntent(
//                application,
//                MusicPlayerAction.Previous
//            )
//        )
    }

}