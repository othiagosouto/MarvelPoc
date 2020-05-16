package com.soutosss.marvelpoc.data.room_source

import androidx.paging.DataSource
import androidx.room.*

@Dao
interface CharacterLocalDAO {

    @Query("SELECT * FROM ${CharacterLocal.TABLE_NAME}")
    fun favoriteList(): DataSource.Factory<Int, CharacterLocal>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun favorite(item: CharacterLocal): Long

    @Query("SELECT id FROM ${CharacterLocal.TABLE_NAME}")
    suspend fun favoriteIds(): List<Int>

    @Delete
    suspend fun unFavorite(item: CharacterLocal)
}

