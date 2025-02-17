package com.vendetta.domain.repository


interface PlaybackRepository {

  suspend  fun play(songUri: String)

   suspend fun pause()

   suspend fun seekToNext()

   suspend fun seekToPrevious()
}