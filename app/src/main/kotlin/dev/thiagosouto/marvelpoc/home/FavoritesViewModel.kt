package dev.thiagosouto.marvelpoc.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.thiagosouto.marvelpoc.R
import dev.thiagosouto.marvelpoc.data.FavoritesRepository
import dev.thiagosouto.marvelpoc.domain.model.Character
import dev.thiagosouto.marvelpoc.home.favorites.FavoritesViewState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

internal class FavoritesViewModel(private val repository: FavoritesRepository<Character>) :
    ViewModel() {

    fun list(): Flow<FavoritesViewState> = repository.favorites().map {
        if (it.isEmpty()) {
            FavoritesViewState.Error(
                message = R.string.empty_characters_favorites,
                image = dev.thiagosouto.marvelpoc.design.R.drawable.ic_favorites
            )
        } else {
            FavoritesViewState.Loaded(it)
        }
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
}
