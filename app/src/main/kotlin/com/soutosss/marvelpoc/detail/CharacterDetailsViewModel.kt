package com.soutosss.marvelpoc.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soutosss.marvelpoc.data.CharactersRepository
import com.soutosss.marvelpoc.data.model.view.Character
import kotlinx.coroutines.launch

class CharacterDetailsViewModel(private val repository: CharactersRepository) : ViewModel() {

    fun favoriteClick(item: Character) {
        if (item.favorite) {
            favorite(item)
        } else {
            unFavorite(item)
        }
    }

    private fun favorite(item: Character) {
        viewModelScope.launch {
            repository.favoriteCharacter(item)
        }
    }

    private fun unFavorite(item: Character) {
        viewModelScope.launch {
            repository.unFavoriteCharacter(item, emptyList())
        }
    }
}
