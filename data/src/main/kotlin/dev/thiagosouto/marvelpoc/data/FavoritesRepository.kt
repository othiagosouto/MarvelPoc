package dev.thiagosouto.marvelpoc.data

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

/**
 * Interface accountable to enable favorites functionality for [T]
 */
interface FavoritesRepository<T: Any> {

    /**
     * return a list of the ids from the favorite [T]
     */
    fun fetchFavoriteIds(): Flow<List<Long>>

    /**
     * Execute action to unFavorite [T]
     */
    suspend fun unFavorite(item: T)

    /**
     * Execute action to favorite [T]
     */
    suspend fun favorite(item: T)

    /**
     * returns a paged source of favorite [T]
     */
    fun favorites(pageSize: Int, maxPageSize: Int): Flow<PagingData<T>>
}
