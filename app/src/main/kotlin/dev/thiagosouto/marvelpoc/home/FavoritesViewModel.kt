package dev.thiagosouto.marvelpoc.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.thiagosouto.marvelpoc.R
import dev.thiagosouto.marvelpoc.data.CharactersRepository
import dev.thiagosouto.marvelpoc.data.model.view.Character
import kotlinx.coroutines.launch

internal class FavoritesViewModel(private val repository: CharactersRepository) : ViewModel() {

    fun createPager() = repository.fetchFavoritesCharacters(PAGE_SIZE, MAX_PAGE_SIZE)

    fun handleException(): Pair<Int, Int> {
        return Pair(
            R.string.empty_characters_favorites,
            R.drawable.ic_favorites
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
        repository.favoriteCharacter(item)
    }

    private suspend fun unFavorite(item: Character) {
        repository.unFavoriteCharacter(item)
    }

    companion object {
        const val PAGE_SIZE = 20
        const val MAX_PAGE_SIZE = 200
    }
}
