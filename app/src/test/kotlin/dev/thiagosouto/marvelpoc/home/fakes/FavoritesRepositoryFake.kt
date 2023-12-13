package dev.thiagosouto.marvelpoc.home.fakes

import dev.thiagosouto.marvelpoc.data.FavoritesRepository
import dev.thiagosouto.marvelpoc.domain.model.Character
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class FavoritesRepositoryFake(
    val favorites: MutableList<Character>
) : FavoritesRepository<Character> {

    override fun fetchFavoriteIds(): Flow<List<Long>> = flowOf(favorites.map { it.id })

    override fun favorites(): Flow<List<Character>> = flowOf(favorites)

    override suspend fun favorite(item: Character) {
        favorites.add(item)
    }

    override suspend fun unFavorite(item: Character) {
        favorites.remove(item)
    }
}
