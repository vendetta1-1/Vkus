package com.vendetta.vkus.presentation.root

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.arkivanov.decompose.extensions.compose.pages.ChildPages
import com.arkivanov.decompose.extensions.compose.pages.PagesScrollAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.vendetta.vkus.R
import com.vendetta.vkus.presentation.favourite.FavouriteContent
import com.vendetta.vkus.presentation.song_list.SongListContent

@Composable
fun RootContent(
    component: RootComponent
) {
    val pages = component.pages.subscribeAsState()
    Scaffold(
        bottomBar = {
            NavigationBarComponent(
                onHomeClicked = component::selectSongList,
                onFavouriteClicked = component::selectFavourite,
                selectedIndex = pages.value.selectedIndex
            )
        }
    ) { values ->
        ChildPages(
            pages = pages.value,
            onPageSelected = component::selectPage,
            scrollAnimation = PagesScrollAnimation.Custom(
                spring(
                    stiffness = Spring.StiffnessMediumLow,
                    dampingRatio = Spring.DampingRatioLowBouncy
                )
            ),
            modifier = Modifier.padding(values),
            pageContent = { _, page ->
                when (page) {
                    is RootComponent.Page.Favourite -> FavouriteContent(page.component)
                    is RootComponent.Page.SongList -> SongListContent(page.component)
                }
            }
        )
    }
}

@Composable
private fun NavigationBarComponent(
    onHomeClicked: () -> Unit,
    onFavouriteClicked: () -> Unit,
    selectedIndex: Int
) {
    NavigationBar {
        NavigationBarItem(
            selected = selectedIndex == 0,
            onClick = {
                if (selectedIndex != 0) {
                    onHomeClicked()
                }
            },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.home),
                    contentDescription = stringResource(R.string.home),
                    tint = if (selectedIndex == 0) {
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
            selected = selectedIndex == 1,
            onClick = {
                if (selectedIndex != 1) {
                    onFavouriteClicked()
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = stringResource(R.string.favourite),
                    tint = if (selectedIndex == 1) {
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
