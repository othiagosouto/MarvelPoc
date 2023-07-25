package dev.thiagosouto.marvelpoc.data.retrofit

import androidx.paging.PagingSource
import dev.thiagosouto.marvelpoc.data.character.CharacterRemoteContract
import dev.thiagosouto.marvelpoc.data.model.view.Character

internal class RetrofitCharacterRemote(private val charactersApi: CharactersBFFApi) :
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
