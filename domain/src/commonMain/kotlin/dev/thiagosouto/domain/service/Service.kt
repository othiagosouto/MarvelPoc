package dev.thiagosouto.domain.service

fun interface Service<I, O> {
    suspend fun fetch(input: I): O
}
