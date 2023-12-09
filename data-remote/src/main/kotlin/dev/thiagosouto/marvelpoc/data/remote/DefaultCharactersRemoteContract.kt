package dev.thiagosouto.marvelpoc.data.remote

import dev.thiagosouto.marvelpoc.domain.data.remote.CharactersRemoteContract
import dev.thiagosouto.marvelpoc.data.remote.character.Result
import dev.thiagosouto.marvelpoc.data.remote.ext.toCharacter
import dev.thiagosouto.marvelpoc.data.remote.interceptors.HttpException
import dev.thiagosouto.marvelpoc.data.remote.interceptors.InternetConnectionException
import dev.thiagosouto.marvelpoc.domain.exception.EmptyDataException
import dev.thiagosouto.marvelpoc.domain.exception.ServerException
import dev.thiagosouto.marvelpoc.domain.model.Character
import java.net.ConnectException
import java.net.UnknownHostException

internal class DefaultCharactersRemoteContract(private val charactersApi: CharactersBFFApi) :
    CharactersRemoteContract {
    private var pageNumber = 0


    override suspend fun listPagingCharacters(
        queryText: String?,
        pageSize: Int,
        provideFavoriteIds: suspend () -> List<Long>
    ): List<Character> {
        return try {
            val favoriteIds = provideFavoriteIds()
            val response = charactersApi.listCharacters(
                name = queryText,
                offset = pageNumber,
                limit = pageSize
            ).data.results.map { mapToFavoriteCharacter(it, favoriteIds) }

            if (response.isEmpty()) {
                throw EmptyDataException()
            } else {
                pageNumber += pageSize
                response
            }
        } catch (e: HttpException) {
            throw ServerException(e)
        } catch (e: ConnectException) {
            throw InternetConnectionException(e)
        } catch (e: UnknownHostException) {
            throw InternetConnectionException(e)
        }
    }


    private fun mapToFavoriteCharacter(result: Result, favoriteIds: List<Long>): Character {
        val character = result.toCharacter()
        return character.copy(favorite = favoriteIds.contains(character.id))
    }
}
