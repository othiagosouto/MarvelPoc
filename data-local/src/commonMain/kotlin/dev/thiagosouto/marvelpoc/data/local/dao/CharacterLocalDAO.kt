package dev.thiagosouto.marvelpoc.data.local.dao

import dev.thiagosouto.marvelpoc.domain.model.Character
import kotlinx.coroutines.flow.Flow

internal interface CharacterLocalDAO {

    fun listAll(): Flow<List<Character>>

    fun favoriteIds(): Flow<List<Long>>

    fun favorite(character: Character)

    fun unFavorite(id: Long)

}
