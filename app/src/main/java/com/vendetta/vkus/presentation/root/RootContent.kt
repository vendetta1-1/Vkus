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
import androidx.compose.ui.res.stringResource
import com.arkivanov.decompose.extensions.compose.pages.ChildPages
import com.arkivanov.decompose.extensions.compose.pages.PagesScrollAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.vendetta.vkus.R
import com.vendetta.vkus.core.RoundedHome
import com.vendetta.vkus.presentation.favourite.FavouriteContent
import com.vendetta.vkus.presentation.song_list.SongListContent

@Composable
fun RootContent(
    component: RootComponent
) {
    val pages = component.pages.subscribeAsState()
    Scaffold(
        bottomBar = {
            NavigationBar {
                pages.value.items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = pages.value.selectedIndex == index,
                        onClick = { component.selectPage(index) },
                        icon = {
                            Icon(
                                imageVector = when (item.instance!!) {
                                    is RootComponent.Page.Favourite -> Icons.Filled.FavoriteBorder
                                    is RootComponent.Page.SongList -> Icons.Filled.RoundedHome
                                },
                                contentDescription = when (item.instance!!) {
                                    is RootComponent.Page.Favourite -> stringResource(R.string.favourite)
                                    is RootComponent.Page.SongList -> stringResource(R.string.home)
                                },
                                tint = if (index == pages.value.selectedIndex) Color.Green else Color.DarkGray
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
