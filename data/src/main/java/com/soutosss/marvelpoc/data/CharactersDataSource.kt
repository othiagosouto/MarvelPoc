package com.soutosss.marvelpoc.data

import androidx.paging.PositionalDataSource
import com.soutosss.marvelpoc.data.local.CharacterHomeDAO
import com.soutosss.marvelpoc.data.model.character.toCharacterHomeList
import com.soutosss.marvelpoc.data.model.view.CharacterHome
import com.soutosss.marvelpoc.data.network.CharactersApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.lang.Exception

class CharactersDataSource(
    private val queryText: String?,
    private val scope: CoroutineScope,
    private val api: CharactersApi,
    private val dao: CharacterHomeDAO,
    private val exceptionHandler: (Exception) -> Unit,
    private val loadFinished: () -> Unit
) :
    PositionalDataSource<CharacterHome>() {

    override fun loadInitial(
        params: LoadInitialParams,
        callback: LoadInitialCallback<CharacterHome>
    ) {
        scope.launch {
            try {
                val response = api.listCharacters(
                    name = queryText,
                    offset = 0,
                    limit = params.requestedLoadSize
                ).toCharacterHomeList()
                callback.onResult(response.checkFavorite(), 0)
                if (response.isNotEmpty()) {
                    loadFinished()
                }
            } catch (e: Exception) {
                exceptionHandler(e)
            }
        }
    }

    private suspend fun List<CharacterHome>.checkFavorite(): List<CharacterHome> {
        val favorites = dao.favoriteIds()
        favorites.forEach { id ->
            this.firstOrNull { it.id == id }?.favorite = true
        }
        return this
    }

    override fun loadRange(
        params: LoadRangeParams,
        callback: LoadRangeCallback<CharacterHome>
    ) {
        scope.launch {
            try {
                val response =
                    api.listCharacters(
                        name = queryText,
                        offset = params.startPosition,
                        limit = params.loadSize
                    )
                callback.onResult(response.toCharacterHomeList().checkFavorite())
            } catch (e: Exception) {
                exceptionHandler(e)
            }
        }
    }
}
