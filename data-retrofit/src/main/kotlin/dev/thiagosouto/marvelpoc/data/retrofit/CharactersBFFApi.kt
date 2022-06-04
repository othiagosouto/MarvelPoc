package dev.thiagosouto.marvelpoc.data.retrofit

import dev.thiagosouto.marvelpoc.data.retrofit.character.MarvelCharactersResponse
import dev.thiagosouto.marvelpoc.data.retrofit.character.details.DetailsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface CharactersBFFApi {
    @GET("characters/details/{characterId}")
    suspend fun listCharacters(
        @Path("characterId") characterId: String
    ): DetailsResponse

    @GET("characters/home")
    suspend fun listCharacters(
        @Query("nameStartsWith") name: String? = null,
        @Query("offset") offset: Int? = null,
        @Query("limit") limit: Int? = 30
    ): MarvelCharactersResponse
}
