package dev.thiagosouto.data.retrofit

import androidx.paging.PositionalDataSource
import dev.thiagosouto.data.retrofit.character.Result
import dev.thiagosouto.data.retrofit.ext.toCharacter
import dev.thiagosouto.marvelpoc.data.model.view.Character
import dev.thiagosouto.marvelpoc.shared.EmptyDataException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

internal class CharactersDataSource(
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
