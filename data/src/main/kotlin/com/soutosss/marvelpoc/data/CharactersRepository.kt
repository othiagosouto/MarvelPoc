package com.soutosss.marvelpoc.data

import com.soutosss.marvelpoc.data.model.view.Character
import com.soutosss.marvelpoc.data.network.CharactersApi
import com.soutosss.marvelpoc.data.room_source.CharacterLocal
import com.soutosss.marvelpoc.shared.contracts.character.CharacterLocalContract
import kotlinx.coroutines.CoroutineScope

class CharactersRepository(
    private val api: CharactersApi,
    private val dataSource: CharacterLocalContract<CharacterLocal>
) {
    fun fetchFavoriteCharacters() = dataSource.favoriteList().mapByPage(::characterAdapter)

    private fun characterAdapter(list: MutableList<CharacterLocal>): MutableList<Character> =
        mutableListOf<Character>().apply {
            addAll(list.map { it.toCharacter() })
        }


    suspend fun unFavoriteCharacter(
        item: Character,
        list: List<Character>?
    ): Int? {
        val id = dataSource.unFavorite(item.toCharacterLocal())
        list?.firstOrNull { it.id ==id }?.favorite = false
        return list?.indexOf(item)
    }

    suspend fun favoriteCharacter(character: Character) {
        dataSource.favorite(character.toCharacterLocal())
    }

    fun charactersDataSource(
        queryText: String?,
        scope: CoroutineScope,
        exceptionHandler: (Exception) -> Unit,
        loadFinished: () -> Unit
    ) = CharactersDataSource(
        queryText,
        scope,
        api,
        dataSource,
        exceptionHandler,
        loadFinished
    )

}


fun Character.toCharacterLocal() =
    CharacterLocal(
        this.id.toLong(),
        this.name,
        this.thumbnailUrl,
        this.description,
        this.favorite
    )

fun CharacterLocal.toCharacter() =
    Character(
        this.id,
        this.name,
        this.thumbnailUrl,
        this.description,
        this.favorite
    )