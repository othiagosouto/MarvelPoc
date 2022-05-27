package com.soutosss.data.retrofit.ext

import com.soutosss.data.retrofit.character.Result
import com.soutosss.marvelpoc.data.model.view.Character

internal fun Result.toCharacter() =
    Character(
        id = this.id,
        name = this.name,
        thumbnailUrl = this.thumbnail.path + "." + this.thumbnail.extension,
        favorite = false,
        description = this.description
    )
