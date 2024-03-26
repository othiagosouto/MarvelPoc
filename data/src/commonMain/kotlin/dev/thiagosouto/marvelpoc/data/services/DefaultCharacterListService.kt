package dev.thiagosouto.marvelpoc.data.services

import dev.thiagosouto.marvelpoc.data.repositories.FavoritesIdentifierProvider
import dev.thiagosouto.marvelpoc.domain.data.remote.CharactersRemoteContract
import dev.thiagosouto.marvelpoc.domain.exception.EmptyDataException
import dev.thiagosouto.marvelpoc.domain.model.Character
import dev.thiagosouto.marvelpoc.domain.services.CharacterListParams
import dev.thiagosouto.marvelpoc.domain.services.CharacterListService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.combine

internal class DefaultCharacterListService(
    private val charactersRemoteContract: CharactersRemoteContract,
    private val favoritesIdentifierProvider: FavoritesIdentifierProvider
) : CharacterListService {

    private val characters: MutableSharedFlow<List<Character>> = MutableSharedFlow()
    private val items = mutableListOf<Character>()

    override val source: Flow<List<Character>>
        get() = characters.combine(favoritesIdentifierProvider.fetchFavoriteIds()) { characters, favoriteIds ->
            characters.map {
                it.copy(favorite = favoriteIds.contains(it.id))
            }
        }

    override suspend fun fetch(input: CharacterListParams): Unit {
        try {
            charactersRemoteContract.listPagingCharacters(
                queryText = input.queryText,
                pageSize = input.pageSize
            ).apply {
                items.addAll(this)
                characters.emit(items)
            }
        } catch (exception: EmptyDataException) {
            characters.emit(items)
        }
    }
}
