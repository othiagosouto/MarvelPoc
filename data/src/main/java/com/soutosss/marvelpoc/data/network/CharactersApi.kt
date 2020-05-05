package com.soutosss.marvelpoc.data.network

import com.soutosss.marvelpoc.data.MarvelCharactersResponse
import retrofit2.http.GET

interface CharactersApi {
    @GET("characters")
    suspend fun listCharacters(): MarvelCharactersResponse
}