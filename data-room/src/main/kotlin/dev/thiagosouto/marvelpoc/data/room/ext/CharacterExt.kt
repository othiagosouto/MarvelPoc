package dev.thiagosouto.marvelpoc.data.room.ext

import dev.thiagosouto.marvelpoc.data.model.view.Character
import dev.thiagosouto.marvelpoc.data.room.CharacterLocal

internal fun Character.toCharacterLocal() =
    CharacterLocal(
        this.id,
        this.name,
        this.thumbnailUrl,
        this.description,
        this.favorite
    )