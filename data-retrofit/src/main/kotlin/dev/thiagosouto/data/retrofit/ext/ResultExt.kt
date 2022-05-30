package dev.thiagosouto.data.retrofit.ext

import dev.thiagosouto.data.retrofit.character.Result
import dev.thiagosouto.marvelpoc.data.model.view.Character

internal fun Result.toCharacter() =
    Character(
        id = this.id,
        name = this.name,
        thumbnailUrl = this.thumbnail.path + "." + this.thumbnail.extension,
        favorite = false,
        description = this.description
    )
