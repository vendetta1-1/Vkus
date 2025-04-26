package com.vendetta.vkus.presentation.root

import com.arkivanov.decompose.router.pages.ChildPages
import com.arkivanov.decompose.value.Value
import com.vendetta.vkus.presentation.favourite.FavouriteComponent
import com.vendetta.vkus.presentation.song_list.SongListComponent

interface RootComponent {

    val pages: Value<ChildPages<*, Page>>

    fun selectPage(index: Int)

    sealed interface Page {
        data class Favourite(val component: FavouriteComponent) : Page
        data class SongList(val component: SongListComponent) : Page
    }
}