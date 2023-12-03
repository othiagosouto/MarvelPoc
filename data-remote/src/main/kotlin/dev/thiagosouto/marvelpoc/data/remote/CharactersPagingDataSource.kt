package dev.thiagosouto.marvelpoc.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import dev.thiagosouto.marvelpoc.domain.model.Character
import dev.thiagosouto.marvelpoc.data.remote.character.Result
import dev.thiagosouto.marvelpoc.data.remote.ext.toCharacter
import dev.thiagosouto.marvelpoc.data.remote.interceptors.HttpException
import dev.thiagosouto.marvelpoc.data.remote.interceptors.InternetConnectionException
import dev.thiagosouto.marvelpoc.domain.exception.EmptyDataException
import java.io.IOException
import java.net.ConnectException
import java.net.UnknownHostException

@Suppress("SwallowedException")
internal class CharactersPagingDataSource(
    private val queryText: String?,
    private val pageSize: Int,
    private val api: CharactersBFFApi,
    private val provideFavoriteIds: suspend () -> List<Long>
) : PagingSource<Int, Character>() {

    override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(pageSize)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(pageSize)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {

        return try {
            val pageNumber = params.key ?: 0
            val favoriteIds = provideFavoriteIds()
            val response = api.listCharacters(
                name = queryText,
                offset = pageNumber,
                limit = pageSize
            ).data.results.map { mapToFavoriteCharacter(it, favoriteIds) }

            if (response.isEmpty()) {
                LoadResult.Error(EmptyDataException())
            } else {
                val prevKey = if (pageNumber > 0) pageNumber - pageSize else null
                val nextKey = pageNumber + pageSize
                LoadResult.Page(
                    data = response,
                    prevKey = prevKey,
                    nextKey = nextKey
                )
            }
        } catch (e: HttpException) {
            LoadResult.Error(e)
        } catch (e: ConnectException) {
            LoadResult.Error(InternetConnectionException())
        } catch (e: UnknownHostException) {
            LoadResult.Error(InternetConnectionException())
        } catch (e: IOException) {
            LoadResult.Error(e)
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
}
