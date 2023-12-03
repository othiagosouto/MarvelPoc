package dev.thiagosouto.marvelpoc.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.thiagosouto.marvelpoc.R
import dev.thiagosouto.marvelpoc.data.FavoritesRepository
import dev.thiagosouto.domain.model.Character
import kotlinx.coroutines.launch

internal class FavoritesViewModel(private val repository: FavoritesRepository<Character>) : ViewModel() {

    fun createPager() = repository.favorites(PAGE_SIZE, MAX_PAGE_SIZE)

    fun handleException(): Pair<Int, Int> {
        return Pair(
            R.string.empty_characters_favorites,
            dev.thiagosouto.marvelpoc.design.R.drawable.ic_favorites
        )
    }

    fun favoriteClick(item: Character) {
        viewModelScope.launch {
            if (item.favorite) {
                favorite(item)
            } else {
                unFavorite(item)
            }
        }
    }

    private suspend fun favorite(item: Character) {
        repository.favorite(item)
    }

    private suspend fun unFavorite(item: Character) {
        repository.unFavorite(item)
    }

    companion object {
        const val PAGE_SIZE = 20
        const val MAX_PAGE_SIZE = 200
    }
}
