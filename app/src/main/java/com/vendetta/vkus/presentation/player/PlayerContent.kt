package com.vendetta.vkus.presentation.player

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun PlayerContent(
    component: PlayerComponent,
    paddingValues: PaddingValues,
) {
    val model by component.model.collectAsState()

}