package com.soutosss.marvelpoc.data.model.character

data class Stories(
    val available: Int,
    val collectionURI: String,
    val items: List<Item>,
    val returned: Int
)