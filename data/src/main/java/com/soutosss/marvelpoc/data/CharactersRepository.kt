package com.soutosss.marvelpoc.data

import com.soutosss.marvelpoc.data.local.CharacterHomeDAO
import com.soutosss.marvelpoc.data.model.view.CharacterHome
import com.soutosss.marvelpoc.data.network.CharactersApi

class CharactersRepository(
    private val api: CharactersApi,
    private val characterHomeDAO: CharacterHomeDAO
) {
    suspend fun fetchAllCharacters(querySearch: String ? = null): List<CharacterHome> {
        val results = api.listCharacters(querySearch).data.results.map { CharacterHome(it) }
        val favorites = fetchFavoriteCharacters()
        favorites.forEach { favorite ->
            results.firstOrNull { it.id == favorite.id }?.favorite = true
        }
        return results
    }

    suspend fun fetchFavoriteCharacters(): List<CharacterHome> = characterHomeDAO.getAll()

    suspend fun unFavoriteCharacterHome(
        item: CharacterHome,
        list: List<CharacterHome>?
    ): Int? {
        characterHomeDAO.delete(item)
        list?.firstOrNull { it.id == item.id }?.favorite = false
        return list?.indexOf(item)
    }

    suspend fun favoriteCharacterHome(characterHome: CharacterHome): Unit =
        characterHomeDAO.insertAll(characterHome)
}