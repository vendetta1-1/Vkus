package com.vendetta.vkus.presentation.root

import android.os.Parcelable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
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
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.serializer

class DefaultRootComponent @AssistedInject constructor(
    @Assisted("componentContext") componentContext: ComponentContext,
    private val playerComponentFactory: DefaultPlayerComponent.Factory,
    private val favouriteComponentFactory: DefaultFavouriteComponent.Factory,
    private val songListComponentFactory: DefaultSongListComponent.Factory
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
        navigation.popToFirst()
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

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultRootComponent
    }
}