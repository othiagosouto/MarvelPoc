package com.soutosss.marvelpoc.data

class CharacterDetails(
    val id: Long,
    val name: String,
    val description: String,
    val imageUrl: String,
    val comics: List<Comics>
)

data class Comics(
    val id: Long,
    val title: String,
    val imageUrl: String
)
