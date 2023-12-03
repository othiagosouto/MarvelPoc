package dev.thiagosouto.marvelpoc.data.room.ext

import dev.thiagosouto.domain.model.Character
import dev.thiagosouto.marvelpoc.data.room.CharacterLocal

internal fun dev.thiagosouto.domain.model.Character.toCharacterLocal() =
    CharacterLocal(
        this.id,
        this.name,
        this.thumbnailUrl,
        this.description,
        this.favorite
    )
