package com.soutosss.data.data_retrofit.ext

import com.soutosss.data.data_retrofit.character.Result
import com.soutosss.marvelpoc.data.model.view.Character

fun Result.toCharacter() =
    Character(
        id = this.id,
        name = this.name,
        thumbnailUrl = this.thumbnail.path + "." + this.thumbnail.extension,
        favorite = false,
        description = this.description
    )
