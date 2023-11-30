package dev.thiagosouto.marvelpoc.data.retrofit.character

import kotlinx.serialization.Serializable

@Serializable
internal data class Url(
    val type: String,
    val url: String
)
