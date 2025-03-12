package com.vendetta.data.mapper

import androidx.core.net.toUri
import com.vendetta.data.local.model.SongDbModel
import com.vendetta.domain.entity.SongEntity

fun SongEntity.toDbModel() = SongDbModel(
    id = this.id,
    uri = this.uri.toString(),
    isFavourite = this.isFavourite,
    durationInMillis = this.durationInMillis,
    coverBitmap = this.coverBitmap,
    songName = this.songName,
    artistName = this.artistName,
    albumName = this.artistName
)

fun SongDbModel.toEntity() = SongEntity(
    id = this.id,
    uri = this.uri.toUri(),
    isFavourite = this.isFavourite,
    durationInMillis = this.durationInMillis,
    coverBitmap = this.coverBitmap,
    songName = this.songName,
    artistName = this.artistName,
    albumName = this.artistName
)

fun List<SongDbModel>.toEntity(): List<SongEntity> = this.map { it.toEntity() }