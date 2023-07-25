package dev.thiagosouto.marvelpoc.data

/**
 * Interface accountable to enable favorites functionality for [T]
 */
interface Favorites<T: Any> {

    /**
     * return a list of the ids from the favorite [T]
     */
    suspend fun fetchFavoriteIds(): List<Long>

    /**
     * Execute action to unFavorite [T]
     */
    suspend fun unFavorite(item: T)

    /**
     * Execute action to favorite [T]
     */
    suspend fun favorite(item: T)
}
