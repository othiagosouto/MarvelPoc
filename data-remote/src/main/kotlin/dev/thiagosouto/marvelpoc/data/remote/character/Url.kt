package dev.thiagosouto.marvelpoc.data.remote.character

import kotlinx.serialization.Serializable

@Serializable
internal data class Url(
    val type: String,
    val url: String
)
