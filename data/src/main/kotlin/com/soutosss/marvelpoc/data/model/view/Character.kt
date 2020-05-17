package com.soutosss.marvelpoc.data.model.view

import java.io.Serializable

data class Character(
    val id: Long,
    val name: String,
    val thumbnailUrl: String,
    val description: String,
    var favorite: Boolean
) : Serializable