package dev.thiagosouto.marvelpoc.domain.service

fun interface ServiceList<I, O> {
    suspend fun fetch(input: I): List<O>
}
