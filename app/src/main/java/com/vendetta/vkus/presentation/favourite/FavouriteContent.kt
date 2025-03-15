package com.vendetta.vkus.presentation.favourite

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vendetta.domain.entity.SongEntity
import com.vendetta.vkus.core.toImageBitmap

@Composable
fun FavouriteContent(
    component: FavouriteComponent,
    paddingValues: PaddingValues
) {
    val model by component.model.collectAsState()

    Scaffold(
        modifier = Modifier.padding(paddingValues)
    ) { values ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(values)
        ) {
            items(
                items = model.songs,
                key = { it.id }
            ) { songEntity ->
                SongListItem(
                    songEntity = songEntity,
                    onSongClickListener = {
                        component::playSong
                    }, onButtonClickListener = {
                        component::changeLikeStatus
                    }
                )
            }
        }
    }
}

@Composable
private fun SongListItem(
    songEntity: SongEntity,
    onSongClickListener: () -> Unit,
    onButtonClickListener: () -> Unit
) {
    Card(
        onClick = onSongClickListener, modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                bitmap = songEntity.coverBitmap.toImageBitmap(),
                contentDescription = songEntity.songName,
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(20.dp))
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(text = songEntity.songName)
                Text(text = songEntity.artistName)
            }

            IconButton(
                onClick = onButtonClickListener
            ) {
                Icon(
                    imageVector = if (songEntity.isFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    tint = if (songEntity.isFavourite) Color.Green else Color.DarkGray,
                    contentDescription = null
                )
            }
        }
    }
}