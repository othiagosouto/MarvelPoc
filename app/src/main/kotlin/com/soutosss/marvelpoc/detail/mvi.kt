package com.soutosss.marvelpoc.detail

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface MviView<S> {
    fun render(state: S)
}

interface Presenter<I, S, E> {
    val state: StateFlow<S>
    val effects: StateFlow<E>
    fun process(intent: I)
}

sealed class Intent {
    data class OpenScreen(val characterId: Long) : Intent()

    object CloseScreen : Intent()

    sealed class Internal : Intent() {
        data class LoadScreen(val characterId: Long) : Intent()
    }
}

sealed class DetailsViewState {

    data class Loaded(val name: String, val description: String, val imageUrl: String) :
        DetailsViewState()

    object Loading : DetailsViewState()

    object Idle : DetailsViewState()
}

sealed class Effect {
    data class ShowLoadingError(val message: String) : Effect()
    object Idle : Effect()
    object CloseScreen : Effect()
}