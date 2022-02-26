package com.soutosss.marvelpoc.data.character

interface CharacterDetailsRemoteContract<T : Any> {
    suspend fun fetchCharacterDetails(characterId: String): T
}
