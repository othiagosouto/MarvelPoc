package com.soutosss.marvelpoc.data.room

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
internal interface CharacterLocalDAO {

    @Query("SELECT * FROM ${CharacterLocal.TABLE_NAME}")
    fun favoriteList(): DataSource.Factory<Int, CharacterLocal>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun favorite(item: CharacterLocal): Long

    @Query("SELECT id FROM ${CharacterLocal.TABLE_NAME}")
    suspend fun favoriteIds(): List<Long>

    @Delete
    suspend fun unFavorite(item: CharacterLocal)
}
