package dev.thiagosouto.marvelpoc.data.room.ext

import dev.thiagosouto.domain.model.Character
import dev.thiagosouto.marvelpoc.data.room.CharacterLocal

internal fun CharacterLocal.toCharacter() = dev.thiagosouto.domain.model.Character(
    this.id,
    this.name,
    this.thumbnailUrl,
    this.description,
    this.favorite
)
