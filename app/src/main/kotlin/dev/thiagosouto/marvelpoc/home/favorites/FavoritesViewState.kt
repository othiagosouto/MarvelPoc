package dev.thiagosouto.marvelpoc.home.favorites

import dev.thiagosouto.marvelpoc.domain.model.Character

sealed class FavoritesViewState {

    data object Loading : FavoritesViewState()

    data class Loaded(val content: List<Character>) : FavoritesViewState()

    data class Error(val message: Int, val image: Int) : FavoritesViewState()
}
