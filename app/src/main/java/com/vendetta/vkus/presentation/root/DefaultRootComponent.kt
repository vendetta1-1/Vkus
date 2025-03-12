package com.vendetta.vkus.presentation.root

import android.os.Parcelable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.vendetta.domain.entity.SongEntity
import com.vendetta.vkus.presentation.favourite.DefaultFavouriteComponent
import com.vendetta.vkus.presentation.favourite.FavouriteComponent
import com.vendetta.vkus.presentation.favourite.FavouriteFactory
import com.vendetta.vkus.presentation.player.DefaultPlayerComponent
import com.vendetta.vkus.presentation.player.PlayerComponent
import com.vendetta.vkus.presentation.player.PlayerFactory
import com.vendetta.vkus.presentation.root.RootComponent.Child.Favourite
import com.vendetta.vkus.presentation.root.RootComponent.Child.Player
import com.vendetta.vkus.presentation.root.RootComponent.Child.SongList
import com.vendetta.vkus.presentation.song_list.DefaultSongListComponent
import com.vendetta.vkus.presentation.song_list.SongListComponent
import com.vendetta.vkus.presentation.song_list.SongListFactory
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.serializer

class DefaultRootComponent(
    componentContext: ComponentContext,
    private val favouriteFactory: FavouriteFactory,
    private val songListFactory: SongListFactory,
    private val playerFactory: PlayerFactory
) : RootComponent, ComponentContext by componentContext {


    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        initialConfiguration = Config.SongList,
        handleBackButton = true,
        childFactory = ::child,
        serializer = serializer<Config>(),
    )

    @OptIn(DelicateDecomposeApi::class)
    override fun onSongListClicked() {
        navigation.push(Config.SongList)
    }

    @OptIn(DelicateDecomposeApi::class)
    override fun onFavouriteClicked() {
        navigation.push(Config.FavouriteSongs)
    }

    @OptIn(DelicateDecomposeApi::class)
    override fun onPlayerClicked(
        currentSong: SongEntity,
        nextSong: SongEntity,
        previousSong: SongEntity
    ) {
        navigation.push(
            Config.Player(
                currentSong = currentSong,
                nextSong = nextSong,
                previousSong = previousSong
            )
        )
    }

    private fun child(
        config: Config, componentContext: ComponentContext
    ): RootComponent.Child {
        return when (config) {
            Config.FavouriteSongs -> {
                val factory = FavouriteComponentFactory
                val component = factory(componentContext, favouriteFactory)
                Favourite(component)
            }

            is Config.Player -> {
                val factory = PlayerComponentFactory
                val component = factory(
                    componentContext = componentContext,
                    storeFactory = playerFactory,
                    currentSong = config.currentSong,
                    previousSong = config.previousSong,
                    nextSong = config.nextSong
                )
                return Player(
                    component = component,
                    currentSong = config.currentSong,
                    nextSong = config.nextSong,
                    previousSong = config.previousSong
                )
            }

            Config.SongList -> {
                val factory = SongListComponentFactory
                val component = factory(
                    componentContext = componentContext,
                    songListFactory = songListFactory
                )
                return SongList(component)
            }
        }

    }

    private object FavouriteComponentFactory : FavouriteComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            storeFactory: FavouriteFactory
        ): FavouriteComponent {
            return DefaultFavouriteComponent(
                componentContext = componentContext,
                favouriteStoreFactory = storeFactory
            )
        }

    }
}

private object SongListComponentFactory : SongListComponent.Factory {
    override fun invoke(
        componentContext: ComponentContext,
        songListFactory: SongListFactory
    ): SongListComponent {
        return DefaultSongListComponent(
            songListStoreFactory = songListFactory,
            componentContext = componentContext
        )
    }

}


private object PlayerComponentFactory : PlayerComponent.Factory {
    override fun invoke(
        componentContext: ComponentContext,
        storeFactory: PlayerFactory,
        currentSong: SongEntity,
        previousSong: SongEntity,
        nextSong: SongEntity
    ): PlayerComponent {
        return DefaultPlayerComponent(
            componentContext = componentContext,
            currentSong = currentSong,
            nextSong = nextSong,
            previousSong = previousSong,
            playerStoreFactory = storeFactory
        )
    }
}


@Parcelize
sealed interface Config : Parcelable {

    @Parcelize
    data object SongList : Config

    @Parcelize
    data object FavouriteSongs : Config

    @Parcelize
    data class Player(
        val currentSong: SongEntity,
        val nextSong: SongEntity,
        val previousSong: SongEntity
    ) : Config
}