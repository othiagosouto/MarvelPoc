package dev.thiagosouto.marvelpoc.data.local

import dev.thiagosouto.marvelpoc.data.character.CharacterLocalContract
import dev.thiagosouto.marvelpoc.data.local.dao.CharacterLocalDAO
import dev.thiagosouto.marvelpoc.domain.model.Character
import kotlinx.coroutines.flow.Flow

internal class DefaultCharacterLocalContract(private val characterLocalDAO: CharacterLocalDAO) :
    CharacterLocalContract<Character> {
    override fun favoritesList(): Flow<List<Character>> = characterLocalDAO.listAll()

    override fun favoriteIds(): Flow<List<Long>> = characterLocalDAO.favoriteIds()

    override suspend fun unFavorite(item: Character) = characterLocalDAO.unFavorite(item.id)

    override suspend fun favorite(item: Character) = characterLocalDAO.favorite(item)
}