package dev.thiagosouto.marvelpoc.domain.service

import kotlinx.coroutines.flow.Flow

interface ServiceList<I, O> {

    val source: Flow<List<O>>

    suspend fun fetch(input: I): Unit
}
