package dev.thiagosouto.marvelpoc.shared.mvi

/**
 * Mvi presenter
 * @param I is the intent
 * @param S is the state
 */
interface Presenter<I, S> {

    /**
     * Process the intent
     */
    fun process(intent: I)
}
