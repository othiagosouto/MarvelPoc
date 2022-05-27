package com.soutosss.data.retrofit

import androidx.paging.PositionalDataSource
import com.soutosss.data.retrofit.character.Result
import com.soutosss.data.retrofit.ext.toCharacter
import com.soutosss.marvelpoc.data.model.view.Character
import com.soutosss.marvelpoc.shared.EmptyDataException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class CharactersDataSource(
    private val queryText: String?,
    private val scope: CoroutineScope,
    private val api: CharactersBFFApi,
    private val exceptionHandler: (Exception) -> Unit,
    private val loadFinished: () -> Unit,
    private val provideFavoriteIds: suspend () -> List<Long>
) :
    PositionalDataSource<Character>() {

    override fun loadInitial(
        params: LoadInitialParams,
        callback: LoadInitialCallback<Character>
    ) {
        scope.launch {
            try {
                val favoriteIds = provideFavoriteIds()
                val response = api.listCharacters(
                    name = queryText,
                    offset = 0,
                    limit = params.requestedLoadSize
                ).data.results.map { mapToFavoriteCharacter(it, favoriteIds) }
                if (response.isEmpty()) {
                    throw EmptyDataException()
                }
                callback.onResult(response, 0)
                loadFinished()
            } catch (e: Exception) {
                exceptionHandler(e)
            }
        }
    }

    private fun mapToFavoriteCharacter(result: Result, favoriteIds: List<Long>): Character {
        val character = result.toCharacter()
        character.checkIfFavorite(favoriteIds)
        return character
    }

    private fun Character.checkIfFavorite(list: List<Long>) {
        this.favorite = list.contains(this.id)
    }

    override fun loadRange(
        params: LoadRangeParams,
        callback: LoadRangeCallback<Character>
    ) {
        scope.launch {
            try {
                val favoriteIds = provideFavoriteIds()
                val response =
                    api.listCharacters(
                        name = queryText,
                        offset = params.startPosition,
                        limit = params.loadSize
                    ).data.results.map { mapToFavoriteCharacter(it, favoriteIds) }
                callback.onResult(response)
            } catch (e: Exception) {
                exceptionHandler(e)
            }
        }
    }

}
