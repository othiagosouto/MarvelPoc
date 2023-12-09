package dev.thiagosouto.marvelpoc.data.character

import dev.thiagosouto.marvelpoc.domain.model.Character

fun interface CharactersRemoteContract {

    suspend fun listPagingCharacters(
        queryText: String?,
        pageSize: Int,
        provideFavoriteIds: suspend () -> List<Long>
    ): List<Character>
}
