package dev.thiagosouto.marvelpoc.data

import androidx.paging.PagingSource

/**
 * Interface accountable to enable favorites functionality for [T]
 */
fun interface PagingService<T : Any> {

    fun charactersPagingDataSource(
        queryText: String?,
        pageSize: Int
    ): PagingSource<Int, T>
}
