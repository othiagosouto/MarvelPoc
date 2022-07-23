package dev.thiagosouto.marvelpoc.data

import dev.thiagosouto.marvelpoc.data.character.CharacterDetailsRemoteContract
import dev.thiagosouto.marvelpoc.data.model.view.Character
import dev.thiagosouto.marvelpoc.data.character.CharacterLocalContract
import dev.thiagosouto.marvelpoc.data.character.CharacterRemoteContract
import kotlinx.coroutines.CoroutineScope

/**
 * Fetch data related from character between remote and local source
 */
class CharactersRepository(
    private val localDataSource: CharacterLocalContract<Character>,
    private val remoteDataSource: CharacterRemoteContract<Character>,
    private val remoteCharacterDetailsSource: CharacterDetailsRemoteContract<CharacterDetails>
) {
    /**
     * return a list of favorite characters
     */
    fun fetchFavoriteCharacters() = localDataSource.favoriteList()

    /**
     * return a list of the ids from the favorite characters
     */
    suspend fun fetchFavoriteIds(): List<Long> = localDataSource.favoriteIds()

    /**
     * Execute action to unfavorite character
     */
    suspend fun unFavoriteCharacter(
        item: Character,
        list: List<Character>?
    ): Int? {
        val id = localDataSource.unFavorite(item)
        list?.firstOrNull { it.id == id }?.favorite = false
        return list?.indexOf(item)
    }

    /**
     * Execute action to unfavorite character
     */
    suspend fun unFavoriteCharacter(
        item: Character
    ){
      localDataSource.unFavorite(item)
    }

    /**
     * Execute action to favorite character
     */
    suspend fun favoriteCharacter(character: Character) {
        localDataSource.favorite(character)
    }

    /**
     * return paged data source for characters
     */
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

    fun charactersPagingDataSource(
        queryText: String?,
        pageSize: Int
    ) = remoteDataSource.listPagingCharacters(
        queryText,
        pageSize,
        localDataSource::favoriteIds
    )

    /**
     * Fetch character details
     */
    suspend fun fetchCharacterDetails(characterId: String) =
        remoteCharacterDetailsSource.fetchCharacterDetails(characterId)
}

