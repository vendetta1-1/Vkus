package com.vendetta.vkus.presentation.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.pages.ChildPages
import com.arkivanov.decompose.router.pages.Pages
import com.arkivanov.decompose.router.pages.PagesNavigation
import com.arkivanov.decompose.router.pages.childPages
import com.arkivanov.decompose.router.pages.select
import com.arkivanov.decompose.value.Value
import com.vendetta.vkus.presentation.favourite.DefaultFavouriteComponent
import com.vendetta.vkus.presentation.home.DefaultHomeComponent
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.serialization.Serializable

class DefaultRootComponent @AssistedInject constructor(
    @Assisted("componentContext") componentContext: ComponentContext,
    private val favouriteComponentFactory: DefaultFavouriteComponent.Factory,
    private val homeComponentFactory: DefaultHomeComponent.Factory
) : RootComponent, ComponentContext by componentContext {

    private val navigation = PagesNavigation<PageConfig>()

    override val pages: Value<ChildPages<*, RootComponent.Page>> = childPages(
        source = navigation,
        serializer = PageConfig.serializer(),
        handleBackButton = true,
        initialPages = {
            Pages(
                items = listOf<PageConfig>(PageConfig.Home, PageConfig.Favourite),
                selectedIndex = 0
            )
        },
        childFactory = { config, componentContext ->
            when (config) {
                PageConfig.Favourite -> {
                    val component = favouriteComponentFactory(componentContext)
                    RootComponent.Page.Favourite(component)
                }

                PageConfig.Home -> {
                    val component = homeComponentFactory(componentContext)
                    RootComponent.Page.Home(component)
                }
            }
        }
    )

    override fun selectPage(index: Int) {
        navigation.select(index = index)
    }

    @Serializable
    private sealed interface PageConfig {

        @Serializable
        data object Home : PageConfig

        @Serializable
        data object Favourite : PageConfig
    }

    @AssistedFactory
    interface Factory {
        operator fun invoke(
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultRootComponent
    }
}