package com.soutosss.marvelpoc.data.character

import androidx.paging.DataSource

interface CharacterLocalContract<T: Any> {

    fun favoriteList(): DataSource.Factory<Int, T>

    suspend fun favorite(item: T): Long

    suspend fun favoriteIds(): List<Long>

    suspend fun unFavorite(item: T) : Long
}
