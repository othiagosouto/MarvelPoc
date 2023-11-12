package dev.thiagosouto.marvelpoc.data

/**
 * Interface to delivery [T]
 */
fun interface CharacterDetailsService<T : Any> {

    suspend fun fetchCharacterDetails(characterId: String): T
}
