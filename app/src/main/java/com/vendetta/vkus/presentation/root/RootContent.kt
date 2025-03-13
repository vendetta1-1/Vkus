package com.vendetta.vkus.presentation.root

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.router.stack.active
import com.vendetta.vkus.presentation.favourite.FavouriteContent
import com.vendetta.vkus.presentation.player.PlayerContent
import com.vendetta.vkus.presentation.song_list.SongListContent

@Composable
fun RootContent(
    component: RootComponent,
) {
    val stack = component.stack

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = stack.active.instance is RootComponent.Child.SongList,
                    onClick = component::onSongListClicked,
                    icon = {},
                    label = {}
                )
                NavigationBarItem(
                    selected = stack.active.instance is RootComponent.Child.Favourite,
                    onClick = component::onFavouriteClicked,
                    icon = {},
                    label = {}
                )
            }
        }
    ) { values ->
        Children(stack) {
            when (val instance = it.instance) {
                is RootComponent.Child.Favourite -> {
                    FavouriteContent(
                        component = instance.component,
                        paddingValues = values
                    )
                }

                is RootComponent.Child.Player -> {
                    PlayerContent(
                        component = instance.component,
                        paddingValues = values
                    )
                }

                is RootComponent.Child.SongList -> {
                    SongListContent(
                        component = instance.component,
                        paddingValues = values
                    )
                }
            }
        }
    }
}