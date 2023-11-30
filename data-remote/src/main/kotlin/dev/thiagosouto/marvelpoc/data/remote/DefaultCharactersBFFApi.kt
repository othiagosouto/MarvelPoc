package dev.thiagosouto.marvelpoc.data.remote

import dev.thiagosouto.marvelpoc.data.remote.character.MarvelCharactersResponse
import dev.thiagosouto.marvelpoc.data.remote.character.details.DetailsResponse
import dev.thiagosouto.marvelpoc.data.remote.interceptors.HttpException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse

internal class DefaultCharactersBFFApi(
    private val httpClient: HttpClient,
    private val defaultUrl: String
) : CharactersBFFApi {

    override suspend fun listCharacters(characterId: String): DetailsResponse =
        httpClient.get(defaultUrl + "characters/details/$characterId")
            .result()

    override suspend fun listCharacters(
        name: String?,
        offset: Int?,
        limit: Int?
    ): MarvelCharactersResponse = httpClient.get(defaultUrl + "characters/home") {
        this.parameter("nameStartsWith", name)
        this.parameter("offset", offset)
        this.parameter("limit", limit)
    }.result()

    private suspend inline fun <reified T> HttpResponse.result(): T = when (this.status.value) {
        in 201..510 -> throw HttpException(this.status.value)
        else -> this.body<T>()
    }
}