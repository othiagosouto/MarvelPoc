package dev.thiagosouto.data.retrofit.character

internal data class Item(
    val name: String,
    val resourceURI: String,
    val type: String? = null
)
