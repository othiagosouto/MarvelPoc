package dev.thiagosouto.marvelpoc.shared

fun interface Mapper<I, O> {

    fun apply(input: I): O
}
