package com.soutosss.data.data_retrofit

import com.soutosss.data.data_retrofit.character.details.DetailsResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface CharactersBFFApi {
    @GET("characters/details/{characterId}")
    suspend fun listCharacters(
        @Path("characterId") characterId: String
    ): DetailsResponse
}
