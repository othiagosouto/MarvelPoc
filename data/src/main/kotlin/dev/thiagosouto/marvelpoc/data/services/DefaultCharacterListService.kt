package dev.thiagosouto.marvelpoc.data.services

import dev.thiagosouto.marvelpoc.data.character.CharacterLocalContract
import dev.thiagosouto.marvelpoc.domain.data.remote.CharactersRemoteContract
import dev.thiagosouto.marvelpoc.domain.model.Character
import dev.thiagosouto.marvelpoc.domain.services.CharacterListParams
import dev.thiagosouto.marvelpoc.domain.services.CharacterListService

internal class DefaultCharacterListService(
    private val provideFavoriteIds: suspend () -> List<Long>,
    private val charactersRemoteContract: CharactersRemoteContract
) :
    CharacterListService {
    override suspend fun fetch(input: CharacterListParams): List<Character> =
        charactersRemoteContract.listPagingCharacters(
            queryText = input.queryText,
            pageSize = input.pageSize,
            provideFavoriteIds = provideFavoriteIds
        )
}
