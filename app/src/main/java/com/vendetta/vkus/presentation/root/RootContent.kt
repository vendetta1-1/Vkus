package com.vendetta.vkus.presentation.root

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.router.stack.active
import com.vendetta.vkus.R
import com.vendetta.vkus.presentation.favourite.FavouriteContent
import com.vendetta.vkus.presentation.player.PlayerContent
import com.vendetta.vkus.presentation.song_list.SongListContent

@Composable
fun RootContent(
    component: RootComponent
) {
    val stack = component.stack

    Children(stack) { child ->
        Scaffold(
            bottomBar = {
                NavigationBarComponent(
                    onHomeClicked = component::onHomeClicked,
                    onFavouriteClicked = component::onFavouriteClicked,
                    selectedChild = stack.active.instance
                )
            }
        ) { values ->
            when (val instance = child.instance) {
                is RootComponent.Child.Favourite -> {
                    FavouriteContent(instance.component, values)
                }

                is RootComponent.Child.Player -> {
                    PlayerContent(instance.component, values)
                }

                is RootComponent.Child.SongList -> {
                    SongListContent(instance.component, values)
                }
            }
        }
    }
}

@Composable
private fun NavigationBarComponent(
    onHomeClicked: () -> Unit,
    onFavouriteClicked: () -> Unit,
    selectedChild: RootComponent.Child
) {
    NavigationBar {
        NavigationBarItem(
            selected = selectedChild is RootComponent.Child.SongList,
            onClick = {
                if (selectedChild !is RootComponent.Child.SongList) {
                    onHomeClicked()
                }
            },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.home),
                    contentDescription = stringResource(R.string.home),
                    tint = if (selectedChild is RootComponent.Child.SongList) {
                        Color.Green
                    } else {
                        Color.DarkGray
                    }
                )
            },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color.Transparent,
                selectedIconColor = Color.Transparent,
                unselectedIconColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = selectedChild is RootComponent.Child.Favourite,
            onClick = {
                if (selectedChild !is RootComponent.Child.Favourite) {
                    onFavouriteClicked()
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = stringResource(R.string.favourite),
                    tint = if (selectedChild is RootComponent.Child.Favourite) {
                        Color.Green
                    } else {
                        Color.DarkGray
                    }
                )
            },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color.Transparent,
                selectedIconColor = Color.Transparent,
                unselectedIconColor = Color.Transparent
            )
        )
    }
}