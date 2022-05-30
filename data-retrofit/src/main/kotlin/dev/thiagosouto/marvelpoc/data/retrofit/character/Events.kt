package dev.thiagosouto.marvelpoc.data.retrofit.character

internal data class Events(
    val available: Int,
    val collectionURI: String,
    val items: List<Item>,
    val returned: Int
)
