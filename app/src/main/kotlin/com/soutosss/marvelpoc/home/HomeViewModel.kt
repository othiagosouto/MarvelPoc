package com.soutosss.marvelpoc.home

import androidx.lifecycle.*
import com.soutosss.marvelpoc.R
import com.soutosss.marvelpoc.shared.livedata.Result
import com.soutosss.marvelpoc.data.CharactersRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: CharactersRepository) : ViewModel(), LifecycleObserver {
    private val _characters = MutableLiveData<Result>()
    val characters: LiveData<Result> = _characters

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun fetchCharacters() {
        viewModelScope.launch {
            _characters.postValue(Result.Loading)
            try {
                _characters.postValue(
                    Result.Loaded(
                        repository.fetchAllCharacters()
                    )
                )
            } catch (e: Exception) {
                _characters.postValue(
                    Result.Error(
                        R.string.home_error_loading
                    )
                )
            }
        }
    }


}