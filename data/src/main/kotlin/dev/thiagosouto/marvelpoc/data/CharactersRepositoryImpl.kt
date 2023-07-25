package dev.thiagosouto.marvelpoc.data

import androidx.paging.PagingData
import dev.thiagosouto.marvelpoc.data.character.CharacterDetailsRemoteContract
import dev.thiagosouto.marvelpoc.data.model.view.Character
import dev.thiagosouto.marvelpoc.data.character.CharacterLocalContract
import dev.thiagosouto.marvelpoc.data.character.CharacterRemoteContract
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

/**
 * Fetch data related from character between remote and local source
 */
class CharactersRepositoryImpl(
    private val localDataSource: CharacterLocalContract<Character>,
    private val remoteDataSource: CharacterRemoteContract<Character>,
    private val remoteCharacterDetailsSource: CharacterDetailsRemoteContract<CharacterDetails>
) : FavoritesRepository<Character>{

    /**
     * return a list of favorite characters
     */
    override fun favorites(pageSize: Int, maxPageSize: Int): Flow<PagingData<Character>> =
        localDataSource.favoritesList(pageSize, maxPageSize)

    /**
     * return a list of favorite characters
     */
    fun fetchFavoriteCharacters() = localDataSource.favoriteList()

    /**
     * return a list of the ids from the favorite characters
     */
    override suspend fun fetchFavoriteIds(): List<Long> = localDataSource.favoriteIds()

    /**
     * Execute action to unfavorite character
     */
    override suspend fun unFavorite(
        item: Character
    ) {
        localDataSource.unFavorite(item)
    }

    /**
     * Execute action to favorite character
     */
    override suspend fun favorite(item: Character) {
        localDataSource.favorite(item)
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

