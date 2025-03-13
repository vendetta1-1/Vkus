package com.vendetta.vkus.di

import android.app.Application
import android.media.MediaMetadataRetriever
import com.vendetta.data.local.db.AppDatabase
import com.vendetta.data.local.db.MusicDao
import com.vendetta.data.repository.PlaylistRepositoryImpl
import com.vendetta.domain.repository.PlaylistRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @[Binds ApplicationScope]
    fun bindPlayListRepository(impl: PlaylistRepositoryImpl): PlaylistRepository

    companion object {
        @[Provides ApplicationScope]
        fun providesMusicDao(application: Application): MusicDao {
            return AppDatabase.getInstance(application).musicDao()
        }

        @[Provides ApplicationScope]
        fun provideMediaMetadataRetriever(): MediaMetadataRetriever = MediaMetadataRetriever()
    }
}