package dev.thiagosouto.marvelpoc.shared.mvi

interface MviView<S> {
    fun render(state: S)
}

interface Presenter<I, S> {
    fun bind(view: MviView<S>)
    fun process(intent: I)
}
