package com.soutosss.marvelpoc.home

import androidx.lifecycle.*
import com.soutosss.marvelpoc.R
import com.soutosss.marvelpoc.data.CharactersRepository
import com.soutosss.marvelpoc.data.model.view.CharacterHome
import com.soutosss.marvelpoc.shared.livedata.Result
import kotlinx.coroutines.launch
import kotlin.reflect.KSuspendFunction0

class HomeViewModel(private val repository: CharactersRepository) : ViewModel(), LifecycleObserver {
    private val _characters = MutableLiveData<Result>()
    val characters: LiveData<Result> = _characters

    private val _favoriteCharacters = MutableLiveData<Result>()
    val favoriteCharacters: LiveData<Result> = _favoriteCharacters

    private val _changeAdapter = MutableLiveData<Int>()
    val changeAdapter: LiveData<Int> = _changeAdapter

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun fetchCharacters() {
        fetchListRequest(
            _characters,
            repository::fetchAllCharacters,
            R.string.home_error_loading,
            R.string.empty_characters_home,
            R.drawable.ic_deadpool
        )
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun fetchFavoriteCharacters() {
        fetchListRequest(
            _favoriteCharacters,
            repository::fetchFavoriteCharacters,
            R.string.favorite_error_loading,
            R.string.empty_characters_favorites,
            R.drawable.ic_favorites
        )
    }

    private fun fetchListRequest(
        liveData: MutableLiveData<Result>,
        retrieveList: KSuspendFunction0<List<CharacterHome>>,
        errorMessageRes: Int,
        emptyErrorMessRes: Int,
        emptyDrawableRes: Int
    ) {
        viewModelScope.launch {
            liveData.postValue(Result.Loading)
            try {
                val list = retrieveList()
                if (list.isEmpty()) {
                    throw EmptyDataException()
                }
                liveData.postValue(Result.Loaded(list))
            } catch (e: EmptyDataException) {
                liveData.postValue(Result.Error(emptyErrorMessRes, emptyDrawableRes))
            } catch (e: Exception) {
                liveData.postValue(Result.Error(errorMessageRes, R.drawable.thanos))
            }
        }
    }

    fun favoriteClick(item: CharacterHome) {
        if (item.favorite) {
            favorite(item)
        } else {
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
            val result = _characters.value as? Result.Loaded
            val items = result?.item as? List<*>
            val position = repository.unFavoriteCharacterHome(item, items?.filterIsInstance<CharacterHome>())
            fetchFavoriteCharacters()
            _changeAdapter.postValue(position)
        }
    }

    private inner class EmptyDataException : Exception()
}
