package dev.thiagosouto.marvelpoc.home.list

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import dev.thiagosouto.marvelpoc.R
import dev.thiagosouto.marvelpoc.data.CharactersRepository
import dev.thiagosouto.marvelpoc.data.model.view.Character
import dev.thiagosouto.marvelpoc.home.FavoritesViewModel
import dev.thiagosouto.marvelpoc.shared.EmptyDataException
import kotlinx.coroutines.launch

class CharactersViewModel(private val repository: CharactersRepository) : ViewModel() {

    var searchedQuery: String? = null

    val favoritesIds: SnapshotStateList<Long> = mutableStateListOf<Long>()

    init {
        viewModelScope.launch {
            fetchFavoriteIds()
        }
    }

    private suspend fun fetchFavoriteIds() {
        favoritesIds.clear()
        favoritesIds.addAll(repository.fetchFavoriteIds())
    }

    fun handleException(e: Throwable): Pair<Int, Int> {
        return if (searchedQuery.isNullOrEmpty()) {
            handleHomeCharactersException(e)
        } else {
            handleSearchContentException(e)
        }
    }

    private fun handleHomeCharactersException(e: Throwable): Pair<Int, Int> {
        return when (e) {
            is EmptyDataException -> Pair(
                R.string.empty_characters_home,
                R.drawable.ic_deadpool
            )

            else -> Pair(
                R.string.home_error_loading,
                R.drawable.thanos
            )
        }
    }

    private fun handleSearchContentException(e: Throwable): Pair<Int, Int> {
        return when (e) {
            is EmptyDataException -> Pair(
                R.string.empty_characters_searched,
                R.drawable.search_not_found
            )
            else -> Pair(
                R.string.search_error_loading,
                R.drawable.thanos
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

    private suspend fun unFavorite(character: Character): Unit {
        repository.unFavoriteCharacter(character)
    }

    fun createPager(): Pager<Int, Character> {
        return Pager<Int, Character>(
            PagingConfig(
                pageSize = FavoritesViewModel.PAGE_SIZE,
                enablePlaceholders = true,
                maxSize = 200
            )
        ) {
            repository.charactersPagingDataSource(searchedQuery, FavoritesViewModel.PAGE_SIZE)
        }
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}

fun SnapshotStateList<Long>.isCharacterFavorite(id: Long): Boolean {
    return this.contains(id)
}
