package com.soutosss.data.retrofit

import com.soutosss.data.retrofit.character.details.DetailsComics
import com.soutosss.data.retrofit.character.details.DetailsResponse
import com.soutosss.marvelpoc.data.CharacterDetails
import com.soutosss.marvelpoc.data.Comics
import com.soutosss.marvelpoc.data.character.CharacterDetailsRemoteContract

class RetrofitCharacterDetailsRemote(private val charactersApi: CharactersBFFApi) :
    CharacterDetailsRemoteContract<CharacterDetails> {
    override suspend fun fetchCharacterDetails(characterId: String): CharacterDetails =
        charactersApi.listCharacters(characterId).toCharacterDetails()
}

fun DetailsResponse.toCharacterDetails(): CharacterDetails = CharacterDetails(
    id = this.id,
    name = this.name,
    description = this.description,
    imageUrl = this.imageUrl,
    comics = this.comics.map(::toComics)
)

fun toComics(item: DetailsComics) = Comics(
    id = item.id,
    imageUrl = item.imageUrl,
    title = item.title
)
