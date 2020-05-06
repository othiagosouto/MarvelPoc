package com.soutosss.marvelpoc

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soutosss.marvelpoc.data.CharactersRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class HomeViewModel(private val repository: CharactersRepository) : ViewModel() {
    private val _characters = MutableLiveData<Result>()
    val characters: LiveData<Result> = _characters

    fun fetchCharacters() {
        viewModelScope.launch {
            _characters.postValue(Result.Loaded(repository.fetchAllCharacters()))
            try {
                _characters.postValue(Result.Loaded(repository.fetchAllCharacters()))
            } catch (e: Exception) {

            }
        }
    }


}