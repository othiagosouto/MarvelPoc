package dev.thiagosouto.marvelpoc.data.retrofit

import dev.thiagosouto.marvelpoc.data.retrofit.character.details.DetailsComics
import dev.thiagosouto.marvelpoc.data.retrofit.character.details.DetailsResponse
import dev.thiagosouto.marvelpoc.data.CharacterDetails
import dev.thiagosouto.marvelpoc.data.Comics
import dev.thiagosouto.marvelpoc.data.character.CharacterDetailsRemoteContract

internal class RetrofitCharacterDetailsRemote(private val charactersApi: CharactersBFFApi) :
    CharacterDetailsRemoteContract<CharacterDetails> {
    override suspend fun fetchCharacterDetails(characterId: String): CharacterDetails =
        charactersApi.listCharacters(characterId).toCharacterDetails()
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
