package com.vendetta.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class SongEntity(
    val id: Int,
    val uri: String,
    val isFavourite: Boolean,
    val durationInMillis: Long,
    val coverBitmap: ByteArray,
    val songName: String,
    val artistName: String,
    val albumName: String,
)