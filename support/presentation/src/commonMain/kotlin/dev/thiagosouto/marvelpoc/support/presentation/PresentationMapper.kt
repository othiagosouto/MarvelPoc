package dev.thiagosouto.marvelpoc.support.presentation

fun interface PresentationMapper<I, O> {

    fun apply(input: I): O
}
