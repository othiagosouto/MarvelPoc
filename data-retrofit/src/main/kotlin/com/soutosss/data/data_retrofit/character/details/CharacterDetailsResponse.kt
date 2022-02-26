package com.soutosss.data.data_retrofit.character.details

import com.google.gson.annotations.SerializedName
import com.soutosss.data.data_retrofit.character.Thumbnail

data class DetailsResponse(
    val id: Long,
    val name: String,
    val description: String,
    @SerializedName("image_url")
    val imageUrl: String,
    val comics: List<DetailsComics>
)

data class DetailsComics(
    val id: Long,
    val title: String,
    @SerializedName("image_url") val imageUrl: String
)