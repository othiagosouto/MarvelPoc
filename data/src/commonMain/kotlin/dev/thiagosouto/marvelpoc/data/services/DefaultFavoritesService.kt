package dev.thiagosouto.marvelpoc.data.services

import dev.thiagosouto.marvelpoc.data.FavoritesService
import dev.thiagosouto.marvelpoc.data.character.CharacterLocalContract
import dev.thiagosouto.marvelpoc.domain.model.Character
import kotlinx.coroutines.flow.Flow

internal class DefaultFavoritesService(
    private val localDataSource: CharacterLocalContract<Character>
) : FavoritesService<Character> {
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
