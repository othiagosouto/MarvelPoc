package dev.thiagosouto.marvelpoc.support.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

/**
 * Mvi presenter
 * @param I is the intent
 * @param S is the state
 */
actual abstract class Presenter<I, S> {
    actual val presenterScope: CoroutineScope = MainScope()

    /**
     * Process the intent
     */
    actual abstract fun process(intent: I)

    protected actual open fun onCleared() {}

    fun clear() {
        onCleared()
        presenterScope.cancel()
    }
}