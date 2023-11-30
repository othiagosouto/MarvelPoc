package dev.thiagosouto.marvelpoc.data.remote.character

import kotlinx.serialization.Serializable

@Serializable
internal data class Events(
    val available: Int,
    val collectionURI: String,
    val items: List<Item>,
    val returned: Int
)
