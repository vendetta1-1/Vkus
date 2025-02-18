package com.vendetta.domain.repository


interface PlaybackRepository {

    fun play(songUri: String)

    fun pause()

    fun seekToNext()

    fun seekToPrevious()
}