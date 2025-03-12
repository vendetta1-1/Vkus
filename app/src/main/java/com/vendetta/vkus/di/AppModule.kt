package com.vendetta.vkus.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.vendetta.vkus.presentation.favourite.FavouriteFactory
import com.vendetta.vkus.presentation.player.PlayerFactory
import com.vendetta.vkus.presentation.song_list.SongListFactory
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


val appModule = module {

    includes(domainModule)

    single<StoreFactory> { DefaultStoreFactory() }

    singleOf(::FavouriteFactory)
    singleOf(::SongListFactory)
    singleOf(::PlayerFactory)

}

