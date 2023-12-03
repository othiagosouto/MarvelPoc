package dev.thiagosouto.marvelpoc.domain

fun interface Mapper<I, O> {

    fun apply(input: I): O
}
