package dev.thiagosouto.marvelpoc.data.retrofit.character.details

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer

@Serializable
internal data class DetailsResponse(
    val id: Long,
    val name: String,
    val description: String,
    @SerialName("image_url")
    val imageUrl: String,
    val comics: List<DetailsComics>
)

@Serializable
internal data class DetailsComics(
    val id: Long,
    val title: String,
    @SerialName("image_url") val imageUrl: String
)
