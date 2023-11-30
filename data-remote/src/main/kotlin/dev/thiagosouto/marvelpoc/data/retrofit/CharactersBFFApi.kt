package dev.thiagosouto.marvelpoc.data.retrofit

import dev.thiagosouto.marvelpoc.data.retrofit.character.MarvelCharactersResponse
import dev.thiagosouto.marvelpoc.data.retrofit.character.details.DetailsResponse

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
