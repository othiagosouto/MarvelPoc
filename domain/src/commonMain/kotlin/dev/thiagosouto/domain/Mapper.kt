package dev.thiagosouto.domain

fun interface Mapper<I, O> {

    fun apply(input: I): O
}
