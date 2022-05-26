package com.soutosss.marvelpoc.home

import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.soutosss.marvelpoc.R
import com.soutosss.marvelpoc.data.CharactersRepository
import com.soutosss.marvelpoc.data.model.view.Character
import com.soutosss.marvelpoc.shared.EmptyDataException
import com.soutosss.marvelpoc.shared.livedata.Result
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: CharactersRepository) : ViewModel() {

    private lateinit var charactersPagedLiveData: LiveData<PagedList<Character>>

    private val _characters = MutableLiveData<Result>()
    val characters: LiveData<Result> = _characters

    private val _favoriteCharacters = MutableLiveData<Result>()
    val favoriteCharacters: LiveData<Result> = _favoriteCharacters

    private val _changeAdapter = MutableLiveData<Int>()
    val changeAdapter: LiveData<Int> = _changeAdapter

    var searchedQuery: String? = null

    private val favoritesIds: MutableList<Long> = mutableListOf()

    init {
        viewModelScope.launch {
            fetchFavoriteIds()
        }
    }

    private suspend fun fetchFavoriteIds() {
        favoritesIds.clear()
        favoritesIds.addAll(repository.fetchFavoriteIds())
    }

    fun charactersPageListContent(): LiveData<PagedList<Character>> {
        val config = PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)
            .setEnablePlaceholders(false)
            .build()

        val dataSourceFactory = object : DataSource.Factory<Int, Character>() {
            override fun create(): DataSource<Int, Character> {
                return repository.charactersDataSource(
                    searchedQuery,
                    viewModelScope,
                    charactersErrorHandle(),
                    {
                        _characters.postValue(Result.Loaded)
                    })
            }
        }
        charactersPagedLiveData = LivePagedListBuilder(dataSourceFactory, config)
            .setBoundaryCallback(emptyHandlerSuccess)
            .build()


        return charactersPagedLiveData
    }

    fun charactersErrorHandle() =
        if (searchedQuery != null) ::handleSearchContentException else ::handleHomeCharactersException

    fun charactersFavorite(): LiveData<PagedList<Character>> {
        val config = PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)
            .setEnablePlaceholders(false)
            .build()

        return repository.fetchFavoriteCharacters()
            .toLiveData(config = config, boundaryCallback = emptyFavoritesHandler)
    }

    private val emptyFavoritesHandler by lazy {
        object : PagedList.BoundaryCallback<Character>() {
            override fun onZeroItemsLoaded() {
                _favoriteCharacters.postValue(
                    Result.Error(
                        R.string.empty_characters_favorites,
                        R.drawable.ic_favorites
                    )
                )
            }

            override fun onItemAtFrontLoaded(@NonNull itemAtFront: Character) {
                _favoriteCharacters.postValue(Result.Loaded)
            }
        }
    }

    private val emptyHandlerSuccess by lazy {
        object : PagedList.BoundaryCallback<Character>() {
            override fun onZeroItemsLoaded() {
                _characters.postValue(
                    Result.Error(
                        R.string.empty_characters_favorites,
                        R.drawable.ic_favorites
                    )
                )
            }

            override fun onItemAtFrontLoaded(@NonNull itemAtFront: Character) {
                _characters.postValue(Result.Loaded)
            }
        }
    }

    private fun handleHomeCharactersException(e: Exception) {
        when (e) {
            is EmptyDataException -> _characters.postValue(
                Result.Error(
                    R.string.empty_characters_home,
                    R.drawable.ic_deadpool
                )
            )
            else -> _characters.postValue(
                Result.Error(
                    R.string.home_error_loading,
                    R.drawable.thanos
                )
            )
        }
    }

    private fun handleSearchContentException(e: Exception) {
        when (e) {
            is EmptyDataException -> _characters.postValue(
                Result.Error(
                    R.string.empty_characters_searched,
                    R.drawable.search_not_found
                )
            )
            else -> _characters.postValue(
                Result.Error(
                    R.string.search_error_loading,
                    R.drawable.thanos
                )
            )
        }
    }

    fun favoriteClick(item: Character) {
        viewModelScope.launch {
            if (item.favorite) {
                favorite(item)
            } else {
                unFavorite(item)
            }
            fetchFavoriteIds()
        }
    }

    private suspend fun favorite(item: Character) {
        repository.favoriteCharacter(item)
    }

    private suspend fun unFavorite(item: Character) {
        val items = charactersPagedLiveData.value?.snapshot()
        val position = repository.unFavoriteCharacter(item, items)
        position?.let(_changeAdapter::postValue)
    }

    fun isCharacterFavorite(id: Long): Boolean {
        return favoritesIds.contains(id)
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}
