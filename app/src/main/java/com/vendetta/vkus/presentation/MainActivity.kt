package com.vendetta.vkus.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext
import com.vendetta.vkus.presentation.favourite.FavouriteFactory
import com.vendetta.vkus.presentation.player.PlayerFactory
import com.vendetta.vkus.presentation.root.DefaultRootComponent
import com.vendetta.vkus.presentation.root.RootContent
import com.vendetta.vkus.presentation.song_list.SongListFactory
import com.vendetta.vkus.presentation.ui.theme.VkusTheme
import org.koin.java.KoinJavaComponent.get

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VkusTheme {
                RootContent(
                    DefaultRootComponent(
                        componentContext = defaultComponentContext(),
                        favouriteFactory = get(clazz = FavouriteFactory::class.java),
                        songListFactory = get(clazz = SongListFactory::class.java),
                        playerFactory = get(clazz = PlayerFactory::class.java),
                    )
                )
            }
        }
    }

}


