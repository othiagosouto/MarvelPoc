package dev.thiagosouto.marvelpoc.home

import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import androidx.paging.toLiveData
import dev.thiagosouto.marvelpoc.R
import dev.thiagosouto.marvelpoc.data.CharactersRepository
import dev.thiagosouto.marvelpoc.data.model.view.Character
import dev.thiagosouto.marvelpoc.shared.livedata.Result
import kotlinx.coroutines.launch

internal class FavoritesViewModel(private val repository: CharactersRepository) : ViewModel() {
    private val _favoriteCharacters = MutableLiveData<Result>()
    val favoriteCharacters: LiveData<Result> = _favoriteCharacters

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

    fun favoriteClick(item: Character) {
        viewModelScope.launch {
            if (item.favorite) {
                favorite(item)
            } else {
                unFavorite(item)
            }
        }
    }

    private suspend fun favorite(item: Character) {
        repository.favoriteCharacter(item)
    }

    private suspend fun unFavorite(item: Character) {
        repository.unFavoriteCharacter(item)
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}
