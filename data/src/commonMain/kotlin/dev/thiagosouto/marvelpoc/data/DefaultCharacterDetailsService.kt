package dev.thiagosouto.marvelpoc.data

import dev.thiagosouto.marvelpoc.domain.data.remote.CharacterDetailsRemoteContract

/**
 * Fetch data related from character between remote and local source
 */
internal class DefaultCharacterDetailsService(
    private val remoteCharacterDetailsSource: CharacterDetailsRemoteContract<CharacterDetails>
) : CharacterDetailsService {

    /**
     * Fetch character details
     */
    override suspend fun fetch(input: String): CharacterDetails =
        remoteCharacterDetailsSource.fetchCharacterDetails(input)
}
