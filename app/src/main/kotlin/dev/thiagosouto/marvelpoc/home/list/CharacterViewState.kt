package dev.thiagosouto.marvelpoc.home.list

import dev.thiagosouto.marvelpoc.domain.model.Character

sealed interface CharacterViewState {

    data object Loading : CharacterViewState

    data class Error(val title: Int, val image: Int) : CharacterViewState

    data class Loaded(val content: List<Character>) : CharacterViewState
}