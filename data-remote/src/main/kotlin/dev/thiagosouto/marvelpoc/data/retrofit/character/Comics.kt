package dev.thiagosouto.marvelpoc.data.retrofit.character

import kotlinx.serialization.Serializable

@Serializable
internal data class Comics(
    val available: Int,
    val collectionURI: String,
    val items: List<Item>,
    val returned: Int
)
