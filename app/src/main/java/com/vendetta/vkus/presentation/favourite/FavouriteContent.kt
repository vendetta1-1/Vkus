package com.vendetta.vkus.presentation.favourite

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun FavouriteContent(
    component: FavouriteComponent,
    paddingValues: PaddingValues
) {
    val model by component.model.collectAsState()

}