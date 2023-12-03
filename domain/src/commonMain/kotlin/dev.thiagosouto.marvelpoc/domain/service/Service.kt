package dev.thiagosouto.marvelpoc.domain.service

fun interface Service<I, O> {
    suspend fun fetch(input: I): O
}
