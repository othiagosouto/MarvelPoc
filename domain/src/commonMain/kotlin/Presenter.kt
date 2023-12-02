/**
 * Mvi presenter
 * @param I is the intent
 * @param S is the state
 */
fun interface Presenter<I, S> {

    /**
     * Process the intent
     */
    fun process(intent: I)
}
