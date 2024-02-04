package dev.thiagosouto.marvelpoc.support.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope

/**
 * Mvi presenter
 * @param I is the intent
 * @param S is the state
 */
actual abstract class Presenter<I, S> : ViewModel() {
    actual val presenterScope: CoroutineScope = viewModelScope


    actual override fun onCleared() {
        super.onCleared()
    }

    /**
     * Process the intent
     */
    actual abstract fun process(intent: I)
}