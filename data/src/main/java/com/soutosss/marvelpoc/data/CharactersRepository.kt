package com.soutosss.marvelpoc.data

import com.soutosss.marvelpoc.data.local.CharacterHomeDAO
import com.soutosss.marvelpoc.data.model.view.CharacterHome
import com.soutosss.marvelpoc.data.network.CharactersApi
import kotlinx.coroutines.CoroutineScope
import java.lang.Exception

class CharactersRepository(
    private val api: CharactersApi,
    private val characterHomeDAO: CharacterHomeDAO
) {
    fun fetchFavoriteCharacters() = characterHomeDAO.getAll()

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

    fun charactersDataSource(
        queryText: String?,
        scope: CoroutineScope,
        exceptionHandler: (Exception) -> Unit,
        loadFinished: () -> Unit
    ) = CharactersDataSource(
        queryText,
        scope,
        api,
        characterHomeDAO,
        exceptionHandler,
        loadFinished
    )
}