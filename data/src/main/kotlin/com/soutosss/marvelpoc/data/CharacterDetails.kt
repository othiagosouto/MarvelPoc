package com.soutosss.marvelpoc.data


class CharacterDetails(
    val id: Long,
    val name: String,
    val description: String,
    val imageUrl: String,
    val comics: List<Comics>
) {
    companion object {
        val empty = CharacterDetails(-1, "", "", "", emptyList())
    }
}

data class Comics(
    val id: Long,
    val title: String,
    val imageUrl: String
)