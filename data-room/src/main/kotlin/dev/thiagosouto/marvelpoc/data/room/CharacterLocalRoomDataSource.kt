package dev.thiagosouto.marvelpoc.data.room

import dev.thiagosouto.marvelpoc.data.character.CharacterLocalContract
import dev.thiagosouto.marvelpoc.data.room.ext.toCharacter
import dev.thiagosouto.marvelpoc.data.room.ext.toCharacterLocal
import dev.thiagosouto.marvelpoc.domain.model.Character
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class CharacterLocalRoomDataSource(private val characterDAO: CharacterLocalDAO) :
    CharacterLocalContract<Character> {

    override suspend fun favorite(item: Character): Unit =
        characterDAO.favorite(item.toCharacterLocal())

    override fun favoriteIds(): Flow<List<Long>> = characterDAO.favoriteIds()

    override suspend fun unFavorite(item: Character) =
        characterDAO.unFavorite(item.toCharacterLocal())

    override fun favoritesList(): Flow<List<Character>> =
        characterDAO.favoriteList().map(::characterAdapter)

    private fun characterAdapter(list: List<CharacterLocal>): MutableList<Character> =
        mutableListOf<Character>().apply {
            addAll(list.map { it.toCharacter() })
        }
}
