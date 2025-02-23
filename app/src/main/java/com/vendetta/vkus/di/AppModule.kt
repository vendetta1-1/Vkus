package com.vendetta.vkus.di

import android.media.MediaMetadataRetriever
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.vendetta.data.local.db.AppDatabase
import com.vendetta.data.local.db.MusicDao
import com.vendetta.data.repository.playback.PlaybackRepositoryImpl
import com.vendetta.data.repository.playlist.PlaylistRepositoryImpl
import com.vendetta.domain.entity.SongEntity
import com.vendetta.domain.repository.PlaybackRepository
import com.vendetta.domain.repository.PlaylistRepository
import com.vendetta.domain.usecase.playback.PauseUseCase
import com.vendetta.domain.usecase.playback.PlaySongUseCase
import com.vendetta.domain.usecase.playback.SeekToNextUseCase
import com.vendetta.domain.usecase.playback.SeekToPreviousUseCase
import com.vendetta.domain.usecase.playlist.AddSongUseCase
import com.vendetta.domain.usecase.playlist.ChangeLikeStatusUseCase
import com.vendetta.domain.usecase.playlist.DeleteSongUseCase
import com.vendetta.domain.usecase.playlist.GetFavouriteSongsUseCase
import com.vendetta.domain.usecase.playlist.GetSongsUseCase
import com.vendetta.vkus.presentation.player.DefaultPlayerComponent
import com.vendetta.vkus.presentation.player.PlayerComponent
import com.vendetta.vkus.presentation.song_list.DefaultSongListComponent
import com.vendetta.vkus.presentation.song_list.SongListComponent
import com.vendetta.vkus.presentation.song_list.SongListStoreFactory
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val appModule = module {

    //Playlistrepository
    factoryOf(::GetSongsUseCase)
    factoryOf(::DeleteSongUseCase)
    factoryOf(::AddSongUseCase)
    factoryOf(::ChangeLikeStatusUseCase)
    factoryOf(::GetFavouriteSongsUseCase)

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
    
    factoryOf<StoreFactory>(::DefaultStoreFactory)

    //SongList
    factory<SongListComponent> { (componentContext: ComponentContext, onAddSong: (String) -> Unit) ->
        DefaultSongListComponent(
            componentContext = componentContext,
            storeFactory = get(),
            onAddSong = onAddSong
        )
    }

    single<SongListStoreFactory> {
        SongListStoreFactory(
            storeFactory = get(),
            getSongsUseCase = get(),
            playSongUseCase = get(),
            deleteSongUseCase = get(),
            addSongUseCase = get(),
            changeLikeStatusUseCase = get()
        )
    }

    //Player
    factory<PlayerComponent> { (componentContext: ComponentContext, song: SongEntity) ->
        DefaultPlayerComponent(
            componentContext = componentContext,
            songEntity = song,
            playSongUseCase = get(),
            seekToNextUseCase = get(),
            seekToPreviousUseCase = get(),
            pauseUseCase = get(),
            changeLikeStatusUseCase = get()
        )
    }

}