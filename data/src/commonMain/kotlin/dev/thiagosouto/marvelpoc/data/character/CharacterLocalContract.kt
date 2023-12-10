package dev.thiagosouto.marvelpoc.data.character

import dev.thiagosouto.marvelpoc.domain.model.Character
import kotlinx.coroutines.flow.Flow

/**
 * Local source contract that interact with characters
 */
interface CharacterLocalContract<T : Any> {

    /**
     * returns a paged source of favorte characters
     */
    fun favoritesList(): Flow<List<Character>>

    /**
     * favorite character and returns its id
     */
    suspend fun favorite(item: T): Long

    /**
     * return a list of favorite ids
     */
    fun favoriteIds(): Flow<List<Long>>

    /**
     * unfavorite character and returns its id
     */
    suspend fun unFavorite(item: T): Long
}
