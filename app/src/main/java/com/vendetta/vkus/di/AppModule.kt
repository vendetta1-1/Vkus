package com.vendetta.vkus.di

import android.media.MediaMetadataRetriever
import com.arkivanov.decompose.ComponentContext
import com.vendetta.data.local.db.AppDatabase
import com.vendetta.data.local.db.MusicDao
import com.vendetta.data.repository.playback.PlaybackRepositoryImpl
import com.vendetta.data.repository.playlist.PlaylistRepositoryImpl
import com.vendetta.domain.repository.PlaybackRepository
import com.vendetta.domain.repository.PlaylistRepository
import com.vendetta.domain.usecase.playback.PauseUseCase
import com.vendetta.domain.usecase.playback.PlaySongUseCase
import com.vendetta.domain.usecase.playback.SeekToNextUseCase
import com.vendetta.domain.usecase.playback.SeekToPreviousUseCase
import com.vendetta.domain.usecase.playlist.AddSongUseCase
import com.vendetta.domain.usecase.playlist.DeleteSongUseCase
import com.vendetta.domain.usecase.playlist.GetSongsUseCase
import com.vendetta.vkus.presentation.home.DefaultHomeComponent
import com.vendetta.vkus.presentation.home.HomeComponent
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val appModule = module {

    //Playlistrepository
    factoryOf(::GetSongsUseCase)
    factoryOf(::DeleteSongUseCase)
    factoryOf(::AddSongUseCase)

    single<MusicDao> {
        AppDatabase.getInstance(androidApplication()).musicDao()
    }

    single<MediaMetadataRetriever> {
        MediaMetadataRetriever()
    }

    single<PlaylistRepository> {
        PlaylistRepositoryImpl(
            retriever = get(),
            musicDao = get()
        )
    }

    //PlayBackRepository
    factoryOf(::PlaySongUseCase)
    factoryOf(::PauseUseCase)
    factoryOf(::SeekToNextUseCase)
    factoryOf(::SeekToPreviousUseCase)

    single<PlaybackRepository> {
        PlaybackRepositoryImpl(
            application = androidApplication()
        )
    }

    factory<HomeComponent> { (componentContext: ComponentContext) ->
        DefaultHomeComponent(
            getSongsUseCase = get(),
            addSongUseCase = get(),
            deleteSongUseCase = get(),
            playSongUseCase = get(),
            componentContext = componentContext
        )
    }

}