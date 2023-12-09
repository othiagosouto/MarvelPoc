package dev.thiagosouto.marvelpoc.home.list

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.thiagosouto.marvelpoc.R
import dev.thiagosouto.marvelpoc.data.FavoritesRepository
import dev.thiagosouto.marvelpoc.domain.exception.EmptyDataException
import dev.thiagosouto.marvelpoc.domain.model.Character
import dev.thiagosouto.marvelpoc.domain.services.CharacterListParams
import dev.thiagosouto.marvelpoc.domain.services.CharacterListService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class CharactersViewModel(
    private val service: CharacterListService,
    private val favoritesRepository: FavoritesRepository<Character>
) : ViewModel() {

    var searchedQuery: String? = null

    private val _state: MutableStateFlow<CharacterViewState> =
        MutableStateFlow(CharacterViewState.Loading)
    val state: StateFlow<CharacterViewState> = _state.asStateFlow()

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
        favoritesRepository.favorite(item)
    }

    private suspend fun unFavorite(character: Character): Unit {
        favoritesRepository.unFavorite(character)
    }

    fun list() {
        viewModelScope.launch {
            try {
                val result = service.fetch(CharacterListParams(pageSize = PAGE_SIZE, searchedQuery))
                val data = _state.value
                val newState = when (data) {
                    is CharacterViewState.Loading -> CharacterViewState.Loaded(result)
                    is CharacterViewState.Loaded -> CharacterViewState.Loaded(data.content + result)
                    is CharacterViewState.Error -> data
                }
                _state.value = newState
            } catch (e: Exception) {
                val (title, image) = handleException(e)
                _state.value = CharacterViewState.Error(title, image)
            }

        }
    }

    private fun handleException(e: Throwable): Pair<Int, Int> {
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
                dev.thiagosouto.marvelpoc.design.R.drawable.ic_deadpool
            )

            else -> Pair(
                R.string.home_error_loading,
                dev.thiagosouto.marvelpoc.design.R.drawable.thanos
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
                dev.thiagosouto.marvelpoc.design.R.drawable.thanos
            )
        }
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}

internal fun SnapshotStateList<Long>.isCharacterFavorite(id: Long): Boolean {
    return this.contains(id)
}
