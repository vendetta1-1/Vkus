package com.vendetta.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vendetta.data.local.model.SongDbModel

@Dao
interface MusicDao {

    @Query("SELECT * FROM songs")
    fun getSongs(): List<SongDbModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = SongDbModel::class)
    suspend fun addSong(songDbModel: SongDbModel)

    @Query("DELETE FROM songs WHERE id=:songId")
    suspend fun deleteSong(songId: Int)
}