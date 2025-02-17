package com.vendetta.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songs")
data class SongDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val uri: String,
    val durationInMillis: Long,
    val coverPath: ByteArray,
    val songName: String,
    val artistName: String,
    val albumName: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SongDbModel

        if (id != other.id) return false
        if (uri != other.uri) return false
        if (durationInMillis != other.durationInMillis) return false
        if (!coverPath.contentEquals(other.coverPath)) return false
        if (songName != other.songName) return false
        if (artistName != other.artistName) return false
        if (albumName != other.albumName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + uri.hashCode()
        result = 31 * result + durationInMillis.hashCode()
        result = 31 * result + coverPath.contentHashCode()
        result = 31 * result + songName.hashCode()
        result = 31 * result + artistName.hashCode()
        result = 31 * result + albumName.hashCode()
        return result
    }
}