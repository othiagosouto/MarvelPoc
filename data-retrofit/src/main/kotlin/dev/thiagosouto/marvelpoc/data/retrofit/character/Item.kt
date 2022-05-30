package dev.thiagosouto.marvelpoc.data.retrofit.character

internal data class Item(
    val name: String,
    val resourceURI: String,
    val type: String? = null
)
