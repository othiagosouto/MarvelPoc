package dev.thiagosouto.marvelpoc.data.character

import androidx.paging.DataSource

/**
 * Local source contract that interact with characters
 */
interface CharacterLocalContract<T: Any> {

    /**
     * returns a paged source of favorte characters
     */
    fun favoriteList(): DataSource.Factory<Int, T>

    /**
     * favorite character and returns its id
     */
    suspend fun favorite(item: T): Long

    /**
     * return a list of favorite ids
     */
    suspend fun favoriteIds(): List<Long>

    /**
     * unfavorite character and returns its id
     */
    suspend fun unFavorite(item: T) : Long
}
