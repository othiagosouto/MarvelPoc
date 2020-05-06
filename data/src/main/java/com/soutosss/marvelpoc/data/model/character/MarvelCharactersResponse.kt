package com.soutosss.marvelpoc.data.model.character

import java.io.Serializable

data class MarvelCharactersResponse(
    val attributionHTML: String,
    val attributionText: String,
    val code: Int,
    val copyright: String,
    val data: Data,
    val etag: String,
    val status: String
): Serializable