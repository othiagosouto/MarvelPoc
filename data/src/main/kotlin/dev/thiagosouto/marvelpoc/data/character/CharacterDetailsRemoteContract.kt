package dev.thiagosouto.marvelpoc.data.character

/**
 * Remote source contract that fetchs character details response
 */
interface CharacterDetailsRemoteContract<T : Any> {

    /**
     * return Character details
     */
    suspend fun fetchCharacterDetails(characterId: String): T
}
