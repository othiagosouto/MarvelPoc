package com.soutosss.marvelpoc.shared.contracts.character

import androidx.paging.DataSource

interface CharacterLocalContract<T> {

    fun favoriteList(): DataSource.Factory<Int, T>

    suspend fun favorite(item: T): Long

    suspend fun favoriteIds(): List<Long>

    suspend fun unFavorite(item: T) : Long
}