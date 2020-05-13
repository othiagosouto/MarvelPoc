package com.soutosss.marvelpoc.data.local

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.soutosss.marvelpoc.data.model.view.Character

@Dao
interface CharacterDAO {

    @Query("SELECT * FROM ${Character.TABLE_NAME}")
    fun getAll(): DataSource.Factory<Int, Character>

    @Insert
    suspend fun insertAll(vararg item: Character)

    @Query("SELECT id FROM ${Character.TABLE_NAME}")
    suspend fun favoriteIds(): List<Int>

    @Delete
    suspend fun delete(item: Character)
}

