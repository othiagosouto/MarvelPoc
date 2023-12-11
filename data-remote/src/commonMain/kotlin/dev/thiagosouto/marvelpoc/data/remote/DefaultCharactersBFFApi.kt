package dev.thiagosouto.marvelpoc.data.remote

import dev.thiagosouto.marvelpoc.data.remote.character.MarvelCharactersResponse
import dev.thiagosouto.marvelpoc.data.remote.character.details.DetailsResponse
import dev.thiagosouto.marvelpoc.data.remote.interceptors.ClientException
import dev.thiagosouto.marvelpoc.data.remote.interceptors.ExceptionHandler
import dev.thiagosouto.marvelpoc.data.remote.interceptors.HttpException
import dev.thiagosouto.marvelpoc.data.remote.interceptors.InternetConnectionException
import dev.thiagosouto.marvelpoc.data.remote.interceptors.ServerException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse

internal class DefaultCharactersBFFApi(
    private val httpClient: HttpClient,
    private val defaultUrl: String
) : CharactersBFFApi {
    private val exceptionHandler: ExceptionHandler = ExceptionHandler()
    override suspend fun listCharacters(characterId: String): DetailsResponse =
        exceptionHandler.handle<DetailsResponse> {
            httpClient.get(defaultUrl + "characters/details/$characterId")
                .result()
        }

    override suspend fun listCharacters(
        name: String?,
        offset: Int?,
        limit: Int?
    ): MarvelCharactersResponse = exceptionHandler.handle<MarvelCharactersResponse> {
        httpClient.get(defaultUrl + "characters/home") {
            this.parameter("nameStartsWith", name)
            this.parameter("offset", offset)
            this.parameter("limit", limit)
        }.result()
    }

    private suspend inline fun <reified T> HttpResponse.result(): T = try {
        when (this.status.value) {
            in SUCCESS_START..SUCCESS_END -> throw HttpException(this.status.value)
            in BAD_REQUEST_START..BAD_REQUEST_END -> throw ClientException(
                this.status.value,
                HttpException(this.status.value)
            )

            in SERVER_ERROR_START..SERVER_ERROR_END -> throw ServerException(
                this.status.value,
                HttpException(this.status.value)
            )

            else -> this.body<T>()
        }
    } catch (e: ClientRequestException) {
        throw InternetConnectionException(e)
    } catch (e: ServerResponseException) {
        throw InternetConnectionException(e)
    } catch (e: HttpException) {
        throw InternetConnectionException(e)
    }

    private companion object {
        const val SUCCESS_START = 201
        const val SUCCESS_END = 299
        const val BAD_REQUEST_START = 400
        const val BAD_REQUEST_END = 499
        const val SERVER_ERROR_START = 500
        const val SERVER_ERROR_END = 500
    }
}
