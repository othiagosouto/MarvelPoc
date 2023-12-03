package dev.thiagosouto.marvelpoc.domain

fun interface MapperList<I, O> {

    fun apply(input: List<I>): List<O>
}
