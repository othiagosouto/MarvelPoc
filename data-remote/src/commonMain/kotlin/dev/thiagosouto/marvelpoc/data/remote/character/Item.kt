package dev.thiagosouto.marvelpoc.data.remote.character

import kotlinx.serialization.Serializable

@Serializable
internal data class Item(
    val name: String,
    val resourceURI: String,
    val type: String? = null
)
