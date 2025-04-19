package com.vendetta.vkus.presentation.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.pages.ChildPages
import com.arkivanov.decompose.router.pages.Pages
import com.arkivanov.decompose.router.pages.PagesNavigation
import com.arkivanov.decompose.router.pages.childPages
import com.arkivanov.decompose.router.pages.select
import com.arkivanov.decompose.value.Value
import com.vendetta.vkus.presentation.favourite.DefaultFavouriteComponent
import com.vendetta.vkus.presentation.song_list.DefaultSongListComponent
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.serialization.Serializable

class DefaultRootComponent @AssistedInject constructor(
    @Assisted("componentContext") componentContext: ComponentContext,
    private val favouriteComponentFactory: DefaultFavouriteComponent.Factory,
    private val songListComponentFactory: DefaultSongListComponent.Factory
) : RootComponent, ComponentContext by componentContext {

    private val navigation = PagesNavigation<PageConfig>()

    override val pages: Value<ChildPages<*, RootComponent.Page>> = childPages(
        source = navigation,
        serializer = PageConfig.serializer(),
        handleBackButton = true,
        initialPages = {
            Pages(
                items = listOf<PageConfig>(PageConfig.SongList, PageConfig.Favourite),
                selectedIndex = 0
            )
        },
        childFactory = { config, componentContext ->
            when (config) {
                PageConfig.Favourite -> {
                    val component = favouriteComponentFactory.create(componentContext)
                    RootComponent.Page.Favourite(component)
                }

                PageConfig.SongList -> {
                    val component = songListComponentFactory.create(componentContext)
                    RootComponent.Page.SongList(component)
                }
            }
        }
    )

    override fun selectPage(index: Int) {
        navigation.select(index = index)
    }

    override fun selectSongList() {
        selectPage(0)
    }

    override fun selectFavourite() {
        selectPage(1)
    }

    @Serializable
    private sealed interface PageConfig {

        @Serializable
        data object SongList : PageConfig

        @Serializable
        data object Favourite : PageConfig
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultRootComponent
    }
}