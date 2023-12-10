package dev.thiagosouto.marvelpoc.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.thiagosouto.marvelpoc.domain.model.Character
import kotlinx.coroutines.flow.Flow

@Dao
internal interface CharacterLocalDAO {

    @Query("SELECT * FROM ${CharacterLocal.TABLE_NAME}")
    fun favoriteList(): Flow<List<CharacterLocal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun favorite(item: CharacterLocal): Long

    @Query("SELECT id FROM ${CharacterLocal.TABLE_NAME}")
    fun favoriteIds(): Flow<List<Long>>

    @Delete
    suspend fun unFavorite(item: CharacterLocal)
}
