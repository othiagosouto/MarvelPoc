package com.soutosss.marvelpoc.data

import com.soutosss.marvelpoc.data.model.character.MarvelCharactersResponse
import com.soutosss.marvelpoc.data.network.CharactersApi

class CharactersRepository(private val api: CharactersApi) {
    suspend fun fetchAllCharacters(): MarvelCharactersResponse = api.listCharacters()
}