package dev.thiagosouto.marvelpoc.data.remote

import dev.thiagosouto.marvelpoc.data.remote.ext.toCharacter
import dev.thiagosouto.marvelpoc.domain.data.remote.CharactersRemoteContract
import dev.thiagosouto.marvelpoc.domain.exception.EmptyDataException
import dev.thiagosouto.marvelpoc.domain.model.Character

internal class DefaultCharactersRemoteContract(private val charactersApi: CharactersBFFApi) :
    CharactersRemoteContract {
    private var pageNumber = 0

    override suspend fun listPagingCharacters(
        queryText: String?,
        pageSize: Int
    ): List<Character> {
        val response = charactersApi.listCharacters(
            name = queryText,
            offset = pageNumber,
            limit = pageSize
        ).data.results.map { it.toCharacter() }

        return if (response.isEmpty()) {
            throw EmptyDataException()
        } else {
            pageNumber += pageSize
            response
        }
    }
}
