package dev.thiagosouto.marvelpoc.data.remote

import androidx.paging.PagingSource
import dev.thiagosouto.marvelpoc.data.character.CharacterRemoteContract
import dev.thiagosouto.domain.model.Character

internal class DefaultCharacterRemoteContract(private val charactersApi: CharactersBFFApi) :
    CharacterRemoteContract<Character> {

    override fun listPagingCharacters(
        queryText: String?,
        pageSize: Int,
        provideFavoriteIds: suspend () -> List<Long>
    ): PagingSource<Int, Character> {
        return CharactersPagingDataSource(
            queryText,
            pageSize,
            charactersApi,
            provideFavoriteIds
        )
    }
}
