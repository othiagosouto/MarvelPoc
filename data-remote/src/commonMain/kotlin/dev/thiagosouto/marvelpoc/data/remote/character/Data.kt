package dev.thiagosouto.marvelpoc.data.remote.character

import kotlinx.serialization.Serializable

@Serializable
internal data class Data(
    val count: Int,
    val limit: Int,
    val offset: Int,
    val results: List<Result>,
    val total: Int
)
