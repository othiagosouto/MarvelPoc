package dev.thiagosouto.domain

fun interface MapperList<I, O> {

    fun apply(input: List<I>): List<O>
}
