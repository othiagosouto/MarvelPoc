package com.soutosss.marvelpoc.data.room

import androidx.paging.DataSource
import com.soutosss.marvelpoc.data.character.CharacterLocalContract
import com.soutosss.marvelpoc.data.model.view.Character
import com.soutosss.marvelpoc.data.room.ext.toCharacter
import com.soutosss.marvelpoc.data.room.ext.toCharacterLocal

internal class CharacterLocalRoomDataSource(private val characterDAO: CharacterLocalDAO) :
    CharacterLocalContract<Character> {

    override fun favoriteList(): DataSource.Factory<Int, Character> =
        characterDAO.favoriteList().mapByPage(::characterAdapter)

    override suspend fun favorite(item: Character): Long =
        characterDAO.favorite(item.toCharacterLocal())

    override suspend fun favoriteIds(): List<Long> = characterDAO.favoriteIds()

    override suspend fun unFavorite(item: Character): Long {
        characterDAO.unFavorite(item.toCharacterLocal())
        return item.id
    }

    private fun characterAdapter(list: List<CharacterLocal>): MutableList<Character> =
        mutableListOf<Character>().apply {
            addAll(list.map { it.toCharacter() })
        }
}
