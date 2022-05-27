package com.soutosss.data.retrofit.character

internal data class Series(
    val available: Int,
    val collectionURI: String,
    val items: List<Item>,
    val returned: Int
)
