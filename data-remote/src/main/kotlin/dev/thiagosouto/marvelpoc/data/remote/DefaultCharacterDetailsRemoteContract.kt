package dev.thiagosouto.marvelpoc.data.remote

import dev.thiagosouto.marvelpoc.domain.data.remote.CharacterDetailsRemoteContract
import dev.thiagosouto.marvelpoc.data.remote.character.details.DetailsComics
import dev.thiagosouto.marvelpoc.data.remote.character.details.DetailsResponse
import dev.thiagosouto.marvelpoc.data.CharacterDetails
import dev.thiagosouto.marvelpoc.data.Comics
import dev.thiagosouto.marvelpoc.data.remote.interceptors.InternetConnectionException
import java.net.ConnectException
import java.net.UnknownHostException

@Suppress("SwallowedException")
internal class DefaultCharacterDetailsRemoteContract(private val charactersApi: CharactersBFFApi) :
    CharacterDetailsRemoteContract<CharacterDetails> {
    override suspend fun fetchCharacterDetails(characterId: String): CharacterDetails = try {
        charactersApi.listCharacters(characterId).toCharacterDetails()
    } catch (e: ConnectException) {
        throw InternetConnectionException()
    } catch (e: UnknownHostException) {
        throw InternetConnectionException()
    }
}

internal fun DetailsResponse.toCharacterDetails(): CharacterDetails = CharacterDetails(
    id = this.id,
    name = this.name,
    description = this.description,
    imageUrl = this.imageUrl,
    comics = this.comics.map(::toComics)
)

internal fun toComics(item: DetailsComics) = Comics(
    id = item.id,
    imageUrl = item.imageUrl,
    title = item.title
)
