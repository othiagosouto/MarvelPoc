package dev.thiagosouto.marvelpoc.data

import dev.thiagosouto.marvelpoc.data.character.CharacterLocalContract
import dev.thiagosouto.marvelpoc.domain.data.remote.CharacterDetailsRemoteContract
import dev.thiagosouto.marvelpoc.domain.model.Character
import kotlinx.coroutines.flow.Flow

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
