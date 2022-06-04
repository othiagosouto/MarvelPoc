package dev.thiagosouto.marvelpoc.data.retrofit.character.details

import com.google.gson.annotations.SerializedName

internal data class DetailsResponse(
    val id: Long,
    val name: String,
    val description: String,
    @SerializedName("image_url")
    val imageUrl: String,
    val comics: List<DetailsComics>
)

internal data class DetailsComics(
    val id: Long,
    val title: String,
    @SerializedName("image_url") val imageUrl: String
)
