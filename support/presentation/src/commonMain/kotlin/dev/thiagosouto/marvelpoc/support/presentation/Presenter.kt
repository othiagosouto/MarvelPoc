package dev.thiagosouto.marvelpoc.support.presentation

import kotlinx.coroutines.CoroutineScope

/**
 * Mvi presenter
 * @param I is the intent
 * @param S is the state
 */
expect abstract class Presenter<I, S>(){
    val presenterScope: CoroutineScope

    /**
     * Process the intent
     */
    abstract fun process(intent: I)

    protected open fun onCleared()
}
