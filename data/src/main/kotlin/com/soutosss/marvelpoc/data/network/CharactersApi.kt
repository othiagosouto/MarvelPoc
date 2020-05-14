package com.soutosss.marvelpoc.data.network

import com.soutosss.marvelpoc.data.network.character.MarvelCharactersResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CharactersApi {
    @GET("characters")
    suspend fun listCharacters(
        @Query("nameStartsWith") name: String? = null,
        @Query("offset") offset: Int? = null,
        @Query("limit") limit: Int? = 30
    ): MarvelCharactersResponse
}