package dev.thiagosouto.marvelpoc.data.remote

import dev.thiagosouto.marvelpoc.data.remote.character.MarvelCharactersResponse
import dev.thiagosouto.marvelpoc.data.remote.character.details.DetailsResponse

internal interface CharactersBFFApi {
    suspend fun listCharacters(
        characterId: String
    ): DetailsResponse

    suspend fun listCharacters(
        name: String? = null,
        offset: Int? = null,
        limit: Int? = 30
    ): MarvelCharactersResponse
}
