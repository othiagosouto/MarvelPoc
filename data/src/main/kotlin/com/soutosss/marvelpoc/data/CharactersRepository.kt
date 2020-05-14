package com.soutosss.marvelpoc.data

import com.soutosss.marvelpoc.data.local.CharacterDAO
import com.soutosss.marvelpoc.data.model.view.Character
import com.soutosss.marvelpoc.data.network.CharactersApi
import kotlinx.coroutines.CoroutineScope
import java.lang.Exception

class CharactersRepository(
    private val api: CharactersApi,
    private val characterDAO: CharacterDAO
) {
    fun fetchFavoriteCharacters() = characterDAO.getAll()

    suspend fun unFavoriteCharacter(
        item: Character,
        list: List<Character>?
    ): Int? {
        characterDAO.delete(item)
        list?.firstOrNull { it.id == item.id }?.favorite = false
        return list?.indexOf(item)
    }

    suspend fun favoriteCharacter(character: Character): Unit =
        characterDAO.insertAll(character)

    fun charactersDataSource(
        queryText: String?,
        scope: CoroutineScope,
        exceptionHandler: (Exception) -> Unit,
        loadFinished: () -> Unit
    ) = CharactersDataSource(
        queryText,
        scope,
        api,
        characterDAO,
        exceptionHandler,
        loadFinished
    )
}