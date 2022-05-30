package dev.thiagosouto.marvelpoc.data.retrofit.character

internal data class Stories(
    val available: Int,
    val collectionURI: String,
    val items: List<Item>,
    val returned: Int
)
