package dev.thiagosouto.marvelpoc.data.room

import androidx.paging.DataSource
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import dev.thiagosouto.marvelpoc.domain.model.Character
import dev.thiagosouto.marvelpoc.data.character.CharacterLocalContract
import dev.thiagosouto.marvelpoc.data.room.ext.toCharacter
import dev.thiagosouto.marvelpoc.data.room.ext.toCharacterLocal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

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

    override fun favoritesList(pageSize: Int, maxSize: Int): Flow<PagingData<Character>> {
        return Pager(
            PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = true,
                maxSize = maxSize
            )
        ) { characterDAO.favoritesList() }
            .flow
            .map { pagingData -> pagingData.map { characterLocal -> characterLocal.toCharacter() } }
    }
}
