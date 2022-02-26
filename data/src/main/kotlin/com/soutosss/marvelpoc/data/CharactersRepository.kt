package com.soutosss.marvelpoc.data

import com.soutosss.marvelpoc.data.character.CharacterDetailsRemoteContract
import com.soutosss.marvelpoc.data.model.view.Character
import com.soutosss.marvelpoc.data.character.CharacterLocalContract
import com.soutosss.marvelpoc.data.character.CharacterRemoteContract
import kotlinx.coroutines.CoroutineScope

class CharactersRepository(
    private val localDataSource: CharacterLocalContract<Character>,
    private val remoteDataSource: CharacterRemoteContract<Character>,
    private val remoteCharacterDetailsSource: CharacterDetailsRemoteContract<CharacterDetails>
) {
    fun fetchFavoriteCharacters() = localDataSource.favoriteList()

    suspend fun unFavoriteCharacter(
        item: Character,
        list: List<Character>?
    ): Int? {
        val id = localDataSource.unFavorite(item)
        list?.firstOrNull { it.id == id }?.favorite = false
        return list?.indexOf(item)
    }

    suspend fun favoriteCharacter(character: Character) {
        localDataSource.favorite(character)
    }

    fun charactersDataSource(
        queryText: String?,
        scope: CoroutineScope,
        exceptionHandler: (Exception) -> Unit,
        loadFinished: () -> Unit
    ) = remoteDataSource.listCharacters(
        scope,
        queryText,
        exceptionHandler,
        loadFinished,
        localDataSource::favoriteIds
    )

    suspend fun fetchCharacterDetails(characterId: String) = remoteCharacterDetailsSource.fetchCharacterDetails(characterId)
}

