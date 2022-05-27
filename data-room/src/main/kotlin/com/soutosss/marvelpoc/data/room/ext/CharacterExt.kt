package com.soutosss.marvelpoc.data.room.ext

import com.soutosss.marvelpoc.data.model.view.Character
import com.soutosss.marvelpoc.data.room.CharacterLocal

internal fun Character.toCharacterLocal() =
    CharacterLocal(
        this.id,
        this.name,
        this.thumbnailUrl,
        this.description,
        this.favorite
    )
