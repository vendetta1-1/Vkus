package com.vendetta.domain.repository.playback

import com.vendetta.domain.entity.SongEntity

interface PlaybackRepository {

    fun play(song: SongEntity)

    fun resumeOrPause()

    fun seekToNext()

    fun seekToPrevious()
}