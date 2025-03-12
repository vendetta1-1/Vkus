package com.vendetta.vkus.presentation.player

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.vendetta.domain.entity.SongEntity
import androidx.compose.runtime.getValue

@Composable
fun PlayerContent(
    component: PlayerComponent,
    paddingValues: PaddingValues,
    currentSong: SongEntity,
    previousSong: SongEntity,
    nextSong: SongEntity
) {
    val model by component.model.collectAsState()

}