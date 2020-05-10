package com.soutosss.marvelpoc.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.soutosss.marvelpoc.data.model.view.CharacterHome

@Dao
interface CharacterHomeDAO {

    @Query("SELECT * FROM ${CharacterHome.TABLE_NAME}")
    suspend fun getAll(): List<CharacterHome>

    @Insert
    suspend fun insertAll(vararg item: CharacterHome)

    @Query("SELECT id FROM ${CharacterHome.TABLE_NAME}")
    suspend fun favoriteIds(): List<Int>

    @Delete
    suspend fun delete(item: CharacterHome)
}

