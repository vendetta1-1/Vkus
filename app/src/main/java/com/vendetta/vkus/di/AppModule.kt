package com.vendetta.vkus.di

import android.media.MediaMetadataRetriever
import com.vendetta.data.local.db.AppDatabase
import com.vendetta.data.local.db.MusicDao
import com.vendetta.data.repository.playback.DefaultPlaybackRepository
import com.vendetta.data.repository.playlist.DefaultPlaylistRepository
import com.vendetta.domain.repository.PlaybackRepository
import com.vendetta.domain.repository.PlaylistRepository
import com.vendetta.domain.usecase.playback.PauseUseCase
import com.vendetta.domain.usecase.playback.PlayUseCase
import com.vendetta.domain.usecase.playback.SeekToNextUseCase
import com.vendetta.domain.usecase.playback.SeekToPreviousUseCase
import com.vendetta.domain.usecase.playlist.AddSongUseCase
import com.vendetta.domain.usecase.playlist.DeleteSongUseCase
import com.vendetta.domain.usecase.playlist.GetSongsUseCase
import com.vendetta.vkus.presentation.MainViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModel
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
        DefaultPlaylistRepository(
            retriever = get(),
            application = androidApplication(),
            musicDao = get()
        )
    }

    //PlayBackRepository
    factoryOf(::PlayUseCase)
    factoryOf(::PauseUseCase)
    factoryOf(::SeekToNextUseCase)
    factoryOf(::SeekToPreviousUseCase)

    single<PlaybackRepository> {
        DefaultPlaybackRepository(
            application = androidApplication()
        )
    }

    viewModel<MainViewModel> {
        MainViewModel(
            get(),
            get(),
            get()
        )
    }
}