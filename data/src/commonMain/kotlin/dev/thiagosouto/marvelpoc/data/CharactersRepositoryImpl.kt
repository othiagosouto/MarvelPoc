package dev.thiagosouto.marvelpoc.data

import dev.thiagosouto.marvelpoc.data.character.CharacterLocalContract
import dev.thiagosouto.marvelpoc.domain.data.remote.CharacterDetailsRemoteContract
import dev.thiagosouto.marvelpoc.domain.model.Character
import kotlinx.coroutines.flow.Flow

/**
 * Fetch data related from character between remote and local source
 */
internal class CharactersRepositoryImpl(
    private val localDataSource: CharacterLocalContract<Character>,
    private val remoteCharacterDetailsSource: CharacterDetailsRemoteContract<CharacterDetails>
) : FavoritesRepository<Character>, CharacterDetailsService {

    /**
     * return a list of favorite characters
     */
    override fun favorites(): Flow<List<Character>> =
        localDataSource.favoritesList()

    /**
     * return a list of the ids from the favorite characters
     */
    override fun fetchFavoriteIds(): Flow<List<Long>> = localDataSource.favoriteIds()

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
     * Fetch character details
     */
    override suspend fun fetch(input: String): CharacterDetails =
        remoteCharacterDetailsSource.fetchCharacterDetails(input)

}

