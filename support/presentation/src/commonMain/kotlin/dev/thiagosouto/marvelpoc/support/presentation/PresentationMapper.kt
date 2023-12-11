package dev.thiagosouto.marvelpoc.support.presentation

/*
Mapper abstraction for Presentation layer
 */
fun interface PresentationMapper<I, O> {

    /*
     * Maps [I] into [O]
     */
    fun apply(input: I): O
}
