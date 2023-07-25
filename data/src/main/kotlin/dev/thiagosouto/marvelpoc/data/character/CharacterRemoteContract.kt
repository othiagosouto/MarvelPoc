package dev.thiagosouto.marvelpoc.data.character

import androidx.paging.PagingSource

interface CharacterRemoteContract<T : Any> {

    fun listPagingCharacters(
        queryText: String?,
        pageSize: Int,
        provideFavoriteIds: suspend () -> List<Long>
    ): PagingSource<Int, T>
}
