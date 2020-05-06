package com.soutosss.marvelpoc

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soutosss.marvelpoc.data.CharactersRepository
import com.soutosss.marvelpoc.data.model.character.MarvelCharactersResponse
import kotlinx.coroutines.launch
import java.lang.Exception

class HomeViewModel(private val repository: CharactersRepository) : ViewModel() {
    private val _mutable = MutableLiveData<Result>()
    val mutable: LiveData<Result> = _mutable

    fun fetchCharacters() {
        viewModelScope.launch {
            _mutable.postValue(Result.Loaded(repository.fetchAllCharacters()))
            try {
                _mutable.postValue(Result.Loaded(repository.fetchAllCharacters()))
            } catch (e: Exception) {

            }
        }
    }


}