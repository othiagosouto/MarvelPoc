package dev.thiagosouto.marvelpoc.home.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.thiagosouto.marvelpoc.R
import dev.thiagosouto.marvelpoc.data.repositories.FavoriteActions
import dev.thiagosouto.marvelpoc.domain.exception.EmptyDataException
import dev.thiagosouto.marvelpoc.domain.model.Character
import dev.thiagosouto.marvelpoc.domain.services.CharacterListParams
import dev.thiagosouto.marvelpoc.domain.services.CharacterListService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class CharactersViewModel(
    private val service: CharacterListService,
    private val favoriteActions: FavoriteActions<Character>
) : ViewModel() {

    var searchedQuery: String? = null

    val state: Flow<CharacterViewState> = service.source
        .map {
            CharacterViewState.Loaded(it)
        }.catch {
            val (title, image) = handleException(it)
            CharacterViewState.Error(title, image)
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, CharacterViewState.Loading)

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
        favoriteActions.favorite(item)
    }

    private suspend fun unFavorite(character: Character): Unit {
        favoriteActions.unFavorite(character)
    }

    fun load() {
        viewModelScope.launch {
            service.fetch(CharacterListParams(pageSize = PAGE_SIZE, searchedQuery))
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
