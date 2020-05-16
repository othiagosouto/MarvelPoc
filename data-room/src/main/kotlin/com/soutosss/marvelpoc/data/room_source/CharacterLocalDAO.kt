package com.soutosss.marvelpoc.data.room_source

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CharacterLocalDAO {

    @Query("SELECT * FROM ${CharacterLocal.TABLE_NAME}")
    fun favoriteList(): DataSource.Factory<Int, CharacterLocal>

    @Insert
    suspend fun favorite(vararg item: CharacterLocal): Int

    @Query("SELECT id FROM ${CharacterLocal.TABLE_NAME}")
    suspend fun favoriteIds(): List<Int>

    @Delete
    suspend fun unFavorite(item: CharacterLocal) : Int
}

