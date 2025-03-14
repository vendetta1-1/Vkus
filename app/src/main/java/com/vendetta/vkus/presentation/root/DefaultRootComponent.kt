package com.vendetta.vkus.presentation.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.active
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.popToFirst
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.vendetta.domain.entity.SongEntity
import com.vendetta.vkus.presentation.favourite.DefaultFavouriteComponent
import com.vendetta.vkus.presentation.player.DefaultPlayerComponent
import com.vendetta.vkus.presentation.song_list.DefaultSongListComponent
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer

class DefaultRootComponent @AssistedInject constructor(
    @Assisted("componentContext") componentContext: ComponentContext,
    private val playerComponentFactory: DefaultPlayerComponent.Factory,
    private val favouriteComponentFactory: DefaultFavouriteComponent.Factory,
    private val songListComponentFactory: DefaultSongListComponent.Factory,

    ) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()


    override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        initialConfiguration = Config.SongList,
        handleBackButton = true,
        childFactory = ::child,
        serializer = serializer<Config>(),
    )

    override fun onHomeClicked() {
        navigation.popToFirst()
    }

    @OptIn(DelicateDecomposeApi::class)
    override fun onFavouriteClicked() {
        if (stack.active.instance !is RootComponent.Child.Favourite) {
            navigation.push(Config.FavouriteSongs)
        }
    }

    private fun child(
        config: Config,
        componentContext: ComponentContext
    ): RootComponent.Child {
        return when (config) {
            Config.FavouriteSongs -> {
                val component = favouriteComponentFactory.create(componentContext)
                RootComponent.Child.Favourite(component)
            }

            is Config.Player -> {
                val component = playerComponentFactory.create(
                    componentContext = componentContext,
                    currentSong = config.currentSong,
                    nextSong = config.nextSong,
                    previousSong = config.previousSong
                )
                RootComponent.Child.Player(component)
            }

            Config.SongList -> {
                val component = songListComponentFactory.crete(componentContext)
                RootComponent.Child.SongList(component)
            }
        }

    }

    @Serializable
    sealed interface Config {

        @Serializable
        data object SongList : Config

        @Serializable
        data object FavouriteSongs : Config

        @Serializable
        data class Player(
            val currentSong: SongEntity,
            val nextSong: SongEntity,
            val previousSong: SongEntity
        ) : Config
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultRootComponent
    }
}