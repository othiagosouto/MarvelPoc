package com.soutosss.marvelpoc.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soutosss.marvelpoc.data.CharacterDetails
import com.soutosss.marvelpoc.data.CharactersRepository
import com.soutosss.marvelpoc.data.model.view.Character
import kotlinx.coroutines.launch

class CharacterDetailsViewModel(private val repository: CharactersRepository) : ViewModel() {


    private val _characterDetail = MutableLiveData<CharacterDetails>()
    val characterDetails: LiveData<CharacterDetails> = _characterDetail

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

    fun loadFavoriteData(characterId: String) {
        viewModelScope.launch {
            val details = repository.fetchCharacterDetails(characterId)
            _characterDetail.postValue(details)
        }
    }
}
