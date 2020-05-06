package com.soutosss.marvelpoc.data.model.view

import com.soutosss.marvelpoc.data.model.character.Result

data class CharacterHome(
    val id: Int,
    val name: String,
    val thumbnailUrl: String,
    val favorite: Boolean
) {
    constructor(response: Result) : this(
        id = response.id,
        name = response.name,
        thumbnailUrl = response.thumbnail.path + response.thumbnail.extension,
        favorite = false
    )
}