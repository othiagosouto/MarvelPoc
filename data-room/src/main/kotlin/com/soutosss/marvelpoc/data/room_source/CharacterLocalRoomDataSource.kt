package com.soutosss.marvelpoc.data.room_source

import androidx.paging.DataSource
import com.soutosss.marvelpoc.shared.contracts.character.CharacterLocalContract

class CharacterLocalRoomDataSource(private val characterDAO: CharacterLocalDAO) :
    CharacterLocalContract<CharacterLocal> {

    override fun favoriteList(): DataSource.Factory<Int, CharacterLocal> =
        characterDAO.favoriteList()

    override suspend fun favorite(item: CharacterLocal): Int = characterDAO.favorite(item)

    override suspend fun favoriteIds(): List<Int> = characterDAO.favoriteIds()

    override suspend fun unFavorite(item: CharacterLocal): Int = characterDAO.unFavorite(item)

}
