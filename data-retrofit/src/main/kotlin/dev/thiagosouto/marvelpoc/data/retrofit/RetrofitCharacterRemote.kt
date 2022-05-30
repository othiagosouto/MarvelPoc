package dev.thiagosouto.marvelpoc.data.retrofit

import androidx.paging.PositionalDataSource
import dev.thiagosouto.marvelpoc.data.character.CharacterRemoteContract
import dev.thiagosouto.marvelpoc.data.model.view.Character
import kotlinx.coroutines.CoroutineScope

internal class RetrofitCharacterRemote(private val charactersApi: CharactersBFFApi) :
    CharacterRemoteContract<Character> {
    override fun listCharacters(
        scope: CoroutineScope,
        queryText: String?,
        exceptionHandler: (Exception) -> Unit,
        loadFinished: () -> Unit,
        provideFavoriteIds: suspend () -> List<Long>
    ): PositionalDataSource<Character> {
        return CharactersDataSource(queryText, scope, charactersApi, exceptionHandler, loadFinished, provideFavoriteIds)
    }
}
