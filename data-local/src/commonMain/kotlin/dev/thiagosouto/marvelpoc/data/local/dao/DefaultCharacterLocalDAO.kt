package dev.thiagosouto.marvelpoc.data.local.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import dev.thiagosouto.marvelpoc.data.local.CharacterLocal
import dev.thiagosouto.marvelpoc.data.local.MarvelDatabase
import dev.thiagosouto.marvelpoc.domain.model.Character
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class DefaultCharacterLocalDAO(private val database: MarvelDatabase) : CharacterLocalDAO {
    override fun listAll(): Flow<List<Character>> {
        return database.characterLocalQueries.selectAll().asFlow().mapToList(Dispatchers.IO)
            .map {
                it.map {
                    Character(
                        id = it.id,
                        name = it.name,
                        thumbnailUrl = it.thumbnailUrl,
                        description = it.description,
                        favorite = it.favorite ?: false
                    )
                }
            }
    }

    override fun favoriteIds(): Flow<List<Long>> {
        return database.characterLocalQueries.favoriteIds().asFlow().mapToList(Dispatchers.IO)
    }

    override fun favorite(character: Character) {
        database.characterLocalQueries.favorite(
            CharacterLocal(
                id = character.id,
                name = character.name,
                thumbnailUrl = character.thumbnailUrl,
                description = character.description,
                favorite = character.favorite ?: false
            )
        )
    }

    override fun unFavorite(id: Long) {
        database.characterLocalQueries.unFavorite(id)
    }
}
