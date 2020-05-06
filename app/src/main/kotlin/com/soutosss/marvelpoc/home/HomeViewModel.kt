package com.soutosss.marvelpoc.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soutosss.marvelpoc.R
import com.soutosss.marvelpoc.Result
import com.soutosss.marvelpoc.data.CharactersRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: CharactersRepository) : ViewModel() {
    private val _characters = MutableLiveData<Result>()
    val characters: LiveData<Result> = _characters

    fun fetchCharacters() {
        viewModelScope.launch {
            _characters.postValue(Result.Loaded(repository.fetchAllCharacters()))
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