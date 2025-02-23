package com.vendetta.vkus.presentation.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.vendetta.vkus.presentation.favourite.FavouriteComponent
import com.vendetta.vkus.presentation.player.PlayerComponent
import com.vendetta.vkus.presentation.profile.ProfileComponent
import com.vendetta.vkus.presentation.song_list.SongListComponent

interface RootComponent {

    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {
        data class Favourite(val component: FavouriteComponent) : Child
        data class Player(val component: PlayerComponent) : Child
        data class Profile(val component: ProfileComponent) : Child
        data class SongList(val component: SongListComponent) : Child
    }
}