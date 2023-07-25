package dev.thiagosouto.marvelpoc.home.fakes

import androidx.paging.PagingData
import dev.thiagosouto.marvelpoc.data.FavoritesRepository
import dev.thiagosouto.marvelpoc.data.model.view.Character
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class FavoritesRepositoryFake(
    val favorites: MutableList<Character>
) : FavoritesRepository<Character> {

    override suspend fun fetchFavoriteIds(): List<Long> = favorites.map { it.id }

    override fun favorites(pageSize: Int, maxPageSize: Int): Flow<PagingData<Character>> = flow { }

    override suspend fun favorite(item: Character) {
        favorites.add(item)
    }

    override suspend fun unFavorite(item: Character) {
        favorites.remove(item)
    }
}
