package com.vendetta.vkus.presentation.song_list

import com.arkivanov.mvikotlin.core.store.Store
import com.vendetta.domain.entity.SongEntity

interface SongListStore : Store<SongListStore.Intent, SongListStore.State, SongListStore.Label> {

    data class State(
        val songs: List<SongEntity>,
        val nowPlayingPath: String? = null
    )

    sealed interface Intent {
        data class PlaySong(val path: String) : Intent
        data class DeleteSong(val song: SongEntity) : Intent
        data class ChangeLikeStatus(val song: SongEntity) : Intent
        data class AddSong(val path: String) : Intent
    }

    sealed interface Label {
        data class AddSong(val path: String) : Label
    }
}