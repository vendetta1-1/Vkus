package com.vendetta.vkus.presentation

import com.vendetta.domain.entity.SongEntity

sealed class ScreenState {

    data object Initial : ScreenState()

    data class Songs(val songs: List<com.vendetta.domain.entity.SongEntity>) : ScreenState()
}