package com.vendetta.vkus.di

import android.media.MediaMetadataRetriever
import com.vendetta.data.local.db.AppDatabase
import com.vendetta.data.local.db.MusicDao
import com.vendetta.data.repository.PlaybackRepositoryImpl
import com.vendetta.data.repository.PlaylistRepositoryImpl
import com.vendetta.domain.repository.playback.PlaybackRepository
import com.vendetta.domain.repository.playlist.PlaylistRepository
import com.vendetta.domain.usecase.playback.PlaySongUseCase
import com.vendetta.domain.usecase.playback.ResumeOrPauseUseCase
import com.vendetta.domain.usecase.playback.SeekToNextUseCase
import com.vendetta.domain.usecase.playback.SeekToPreviousUseCase
import com.vendetta.domain.usecase.playlist.AddSongUseCase
import com.vendetta.domain.usecase.playlist.ChangeLikeStatusUseCase
import com.vendetta.domain.usecase.playlist.DeleteSongUseCase
import com.vendetta.domain.usecase.playlist.GetFavouriteSongsUseCase
import com.vendetta.domain.usecase.playlist.GetSongsUseCase
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModule = module {

    factoryOf(::GetSongsUseCase)
    factoryOf(::DeleteSongUseCase)
    factoryOf(::AddSongUseCase)
    factoryOf(::ChangeLikeStatusUseCase)
    factoryOf(::GetFavouriteSongsUseCase)
    factoryOf(::PlaySongUseCase)
    factoryOf(::SeekToNextUseCase)
    factoryOf(::SeekToPreviousUseCase)
    factoryOf(::ResumeOrPauseUseCase)

    single<PlaybackRepository> {
        PlaybackRepositoryImpl(
            application = androidApplication()
        )
    }

    single<PlaylistRepository> {
        PlaylistRepositoryImpl(
            retriever = MediaMetadataRetriever(),
            musicDao = AppDatabase.getInstance(androidApplication()).musicDao()
        )
    }


}