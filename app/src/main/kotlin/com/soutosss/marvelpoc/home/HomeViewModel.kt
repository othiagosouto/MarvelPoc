package com.soutosss.marvelpoc.home

import androidx.lifecycle.*
import com.soutosss.marvelpoc.R
import com.soutosss.marvelpoc.data.CharactersRepository
import com.soutosss.marvelpoc.data.model.view.CharacterHome
import com.soutosss.marvelpoc.shared.livedata.Result
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: CharactersRepository) : ViewModel(), LifecycleObserver {
    private val _characters = MutableLiveData<Result>()
    val characters: LiveData<Result> = _characters

    private val _favoriteCharacters = MutableLiveData<Result>()
    val favoriteCharacters: LiveData<Result> = _favoriteCharacters

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun fetchCharacters() {
        viewModelScope.launch {
            _characters.postValue(Result.Loading)
            try {
                val results = repository.fetchAllCharacters().data.results
                _characters.postValue(Result.Loaded(results.map { CharacterHome(it) }))
            } catch (e: Exception) {
                _characters.postValue(
                    Result.Error(R.string.home_error_loading)
                )
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun fetchFavoriteCharacters() {
        viewModelScope.launch {
            _favoriteCharacters.postValue(Result.Loading)
            try {
                _favoriteCharacters.postValue(Result.Loaded(repository.fetchFavoriteCharacters()))
            } catch (e: Exception) {
                _favoriteCharacters.postValue(
                    Result.Error(R.string.home_error_loading)
                )
            }
        }
    }

    fun favoriteClick(item: CharacterHome) {
        if (item.favorite) {
            favorite(item)
        }else{
            unFavorite(item)
        }
    }

    private fun favorite(item: CharacterHome) {
        viewModelScope.launch {
            repository.favoriteCharacterHome(item)
            fetchFavoriteCharacters()
        }
    }

    private fun unFavorite(item: CharacterHome) {
        viewModelScope.launch {
            repository.unFavoriteCharacterHome(item)
            fetchFavoriteCharacters()
        }
    }

}