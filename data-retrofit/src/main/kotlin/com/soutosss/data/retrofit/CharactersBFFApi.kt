package com.soutosss.data.retrofit

import com.soutosss.data.retrofit.character.MarvelCharactersResponse
import com.soutosss.data.retrofit.character.details.DetailsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CharactersBFFApi {
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
