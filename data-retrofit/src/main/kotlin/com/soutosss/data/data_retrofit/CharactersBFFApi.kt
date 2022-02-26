package com.soutosss.data.data_retrofit

import com.soutosss.data.data_retrofit.character.details.DetailsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CharactersBFFApi {
    @GET("characters/{characterId}/details}")
    suspend fun listCharacters(
        @Path("characterId") characterId: String,
        @Query("offset") offset: Int? = null,
        @Query("limit") limit: Int? = 30
    ): DetailsResponse
}
