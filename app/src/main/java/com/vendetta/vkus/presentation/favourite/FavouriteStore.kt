package com.vendetta.vkus.presentation.favourite

import com.arkivanov.mvikotlin.core.store.Store
import com.vendetta.domain.entity.SongEntity

interface FavouriteStore :
    Store<FavouriteStore.Intent, FavouriteStore.State, Nothing> {

    data class State(
        val songs: List<SongEntity>,
        val nowPlayingPath: String? = null
    )

    sealed interface Intent {
        data class ChangeLikeStatus(val song: SongEntity) : Intent
        data class PlaySong(val path: String) : Intent
    }


}