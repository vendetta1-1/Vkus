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
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SongEntity

        if (id != other.id) return false
        if (uri != other.uri) return false
        if (durationInMillis != other.durationInMillis) return false
        if (!coverBitmap.contentEquals(other.coverBitmap)) return false
        if (songName != other.songName) return false
        if (artistName != other.artistName) return false
        if (albumName != other.albumName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + uri.hashCode()
        result = 31 * result + durationInMillis.hashCode()
        result = 31 * result + coverBitmap.contentHashCode()
        result = 31 * result + songName.hashCode()
        result = 31 * result + artistName.hashCode()
        result = 31 * result + albumName.hashCode()
        return result
    }
}