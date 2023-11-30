package dev.thiagosouto.marvelpoc.data.remote.ext

import dev.thiagosouto.marvelpoc.data.remote.character.Result
import dev.thiagosouto.marvelpoc.data.model.view.Character

internal fun Result.toCharacter() =
    Character(
        id = this.id,
        name = this.name,
        thumbnailUrl = this.thumbnail.path + "." + this.thumbnail.extension,
        favorite = false,
        description = this.description
    )
