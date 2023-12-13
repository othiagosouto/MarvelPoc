package dev.thiagosouto.marvelpoc.data.repositories

import dev.thiagosouto.marvelpoc.data.repositories.FavoritesRepository
import dev.thiagosouto.marvelpoc.data.character.CharacterLocalContract
import dev.thiagosouto.marvelpoc.domain.model.Character
import kotlinx.coroutines.flow.Flow

internal class DefaultFavoritesRepository(
    private val localDataSource: CharacterLocalContract<Character>
) : FavoritesRepository<Character> {
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
    ) : Unit  {
        localDataSource.unFavorite(item)
    }

    /**
     * Execute action to favorite character
     */
    override suspend fun favorite(item: Character) {
        localDataSource.favorite(item)
    }
}
