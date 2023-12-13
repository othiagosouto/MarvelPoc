package dev.thiagosouto.marvelpoc.data.repositories

import kotlinx.coroutines.flow.Flow

/**
 * Interface accountable to enable favorites functionality for [T]
 */
interface FavoritesRepository<T : Any> : FavoritesIdentifierProvider, FavoriteActions<T> {

    /**
     * returns a paged source of favorite [T]
     */
    fun favorites(): Flow<List<T>>
}

fun interface FavoritesIdentifierProvider {
    /**
     * return a list of the ids from the favorite [T]
     */
    fun fetchFavoriteIds(): Flow<List<Long>>
}

interface FavoriteActions<T : Any> {
    /**
     * Execute action to unFavorite [T]
     */
    suspend fun unFavorite(item: T)

    /**
     * Execute action to favorite [T]
     */
    suspend fun favorite(item: T)
}
