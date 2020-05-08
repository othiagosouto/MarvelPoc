package com.soutosss.marvelpoc.data

import com.soutosss.marvelpoc.data.local.CharacterHomeDAO
import com.soutosss.marvelpoc.data.model.character.MarvelCharactersResponse
import com.soutosss.marvelpoc.data.model.view.CharacterHome
import com.soutosss.marvelpoc.data.network.CharactersApi

class CharactersRepository(
    private val api: CharactersApi,
    private val characterHomeDAO: CharacterHomeDAO
) {
    suspend fun fetchAllCharacters(): MarvelCharactersResponse = api.listCharacters()

    suspend fun fetchFavoriteCharacters(): List<CharacterHome> = characterHomeDAO.getAll()
    suspend fun favoriteCharacterHome(characterHome: CharacterHome): Unit =
        characterHomeDAO.insertAll(characterHome)

    suspend fun unFavoriteCharacterHome(item: CharacterHome) {
        characterHomeDAO.delete(item)
    }
}