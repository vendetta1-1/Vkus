package com.vendetta.vkus.presentation.player

import com.vendetta.domain.entity.SongEntity

class PlayerStore {

    data class State(
        val nowPlaying: SongEntity? = null,
        val progress: Long? = null,
        val isPause: Boolean? = null
    )

}