package dev.thiagosouto.marvelpoc.data.room

import androidx.paging.DataSource
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
internal interface CharacterLocalDAO {

    @Query("SELECT * FROM ${CharacterLocal.TABLE_NAME}")
    fun favoritesList(): PagingSource<Int, CharacterLocal>

    @Query("SELECT * FROM ${CharacterLocal.TABLE_NAME}")
    fun favoriteList(): DataSource.Factory<Int, CharacterLocal>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun favorite(item: CharacterLocal): Long

    @Query("SELECT id FROM ${CharacterLocal.TABLE_NAME}")
    fun favoriteIds(): Flow<List<Long>>

    @Delete
    suspend fun unFavorite(item: CharacterLocal)
}
