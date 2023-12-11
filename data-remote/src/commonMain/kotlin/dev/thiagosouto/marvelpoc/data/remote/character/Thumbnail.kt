package dev.thiagosouto.marvelpoc.data.remote.character

import kotlinx.serialization.Serializable

@Serializable
internal data class Thumbnail(
    val extension: String,
    val path: String
)
