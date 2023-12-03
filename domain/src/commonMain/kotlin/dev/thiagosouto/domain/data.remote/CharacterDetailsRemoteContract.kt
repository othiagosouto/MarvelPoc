package dev.thiagosouto.domain.data.remote

/**
 * Remote source contract that fetch character details response
 */
fun interface CharacterDetailsRemoteContract<T : Any> {

    /**
     * return Character details
     */
    suspend fun fetchCharacterDetails(characterId: String): T
}
