package dev.thiagosouto.marvelpoc.domain.data.remote

import dev.thiagosouto.marvelpoc.domain.model.Character

fun interface CharactersRemoteContract {

    suspend fun listPagingCharacters(
        queryText: String?,
        pageSize: Int
    ): List<Character>
}
