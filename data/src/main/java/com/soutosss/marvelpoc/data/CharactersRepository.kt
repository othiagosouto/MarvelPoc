package com.soutosss.marvelpoc.data

import com.soutosss.marvelpoc.data.network.CharactersApi
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class CharactersRepository(private val api: CharactersApi) {
    suspend fun fetchAllCharacters(): MarvelCharactersResponse = api.listCharacters()
}