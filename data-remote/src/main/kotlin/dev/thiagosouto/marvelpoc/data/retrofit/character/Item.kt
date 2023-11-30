package dev.thiagosouto.marvelpoc.data.retrofit.character

import kotlinx.serialization.Serializable

@Serializable
internal data class Item(
    val name: String,
    val resourceURI: String,
    val type: String? = null
)
