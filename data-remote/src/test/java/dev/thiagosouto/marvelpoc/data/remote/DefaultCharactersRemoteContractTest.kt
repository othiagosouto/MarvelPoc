package dev.thiagosouto.marvelpoc.data.remote

import com.google.common.truth.Truth.assertThat
import dev.thiagosouto.marvelpoc.data.remote.character.MarvelCharactersResponse
import dev.thiagosouto.marvelpoc.data.remote.character.details.DetailsResponse
import dev.thiagosouto.marvelpoc.data.remote.ext.toCharacter
import dev.thiagosouto.marvelpoc.data.remote.interceptors.HttpException
import dev.thiagosouto.marvelpoc.data.remote.interceptors.InternetConnectionException
import dev.thiagosouto.marvelpoc.domain.exception.ServerException
import dev.thiagosouto.marvelpoc.domain.model.Character
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.net.ConnectException
import java.net.UnknownHostException

internal class DefaultCharactersRemoteContractTest {

    private val charactersList = parseToJson()
    private val api = FakeBff()
    private lateinit var characterList: List<Character>
    private lateinit var characterFavoredList: List<Character>
    private val ids = mutableListOf<Long>()
    private val provideFavoriteIds: suspend () -> List<Long> = { ids }

    @Before
    fun setup() {
        val character = charactersList.data.results.first().toCharacter()
        characterList = listOf(character)
        characterFavoredList = listOf(character.copy(favorite = true))
    }

    @After
    fun tearDown() {
        api.exception = null
        api.detailsResponse = null
        api.marvelCharactersResponse = null
        api.page = 0
        api.offset = 0
        ids.clear()
    }

    @Test
    fun `Returns CharactersList`() = runBlocking {
        val remote = DefaultCharactersRemoteContract(api)

        api.marvelCharactersResponse = parseToJson()

        runTest {
            val result = remote.listPagingCharacters("queryText", 10, provideFavoriteIds)

            assertThat(result).isEqualTo(charactersList.data.results.map { it.toCharacter() })
        }
    }

    @Test(expected = ServerException::class)
    fun `Given HttpException Then throws ConnectException`() = runBlocking {
        val remote = DefaultCharactersRemoteContract(api)

        api.exception = HttpException(400)

        runTest {
            remote.listPagingCharacters("queryText", 10, provideFavoriteIds)
        }
    }

    @Test(expected = InternetConnectionException::class)
    fun `Given UnknownHostException Then throws InternetConnectionException`() = runBlocking {
        val remote = DefaultCharactersRemoteContract(api)

        api.exception = UnknownHostException("")

        runTest {
            remote.listPagingCharacters("queryText", 10, provideFavoriteIds)
        }
    }


    @Test(expected = InternetConnectionException::class)
    fun `Given ConnectException Then throws InternetConnectionException`() = runBlocking {
        val remote = DefaultCharactersRemoteContract(api)

        api.exception = ConnectException("")

        runTest {
            remote.listPagingCharacters("queryText", 10, provideFavoriteIds)
        }
    }

    @Test
    fun `Given requests Then page correctly`() = runBlocking {
        val remote = DefaultCharactersRemoteContract(api)

        api.marvelCharactersResponse = parseToJson()

        runTest {
            remote.listPagingCharacters("queryText", 10, provideFavoriteIds)
            remote.listPagingCharacters("queryText", 10, provideFavoriteIds)
            remote.listPagingCharacters("queryText", 10, provideFavoriteIds)

            assertThat(api.offset).isEqualTo(20)
            assertThat(api.page).isEqualTo(10)
        }
    }

    internal class FakeBff : CharactersBFFApi {
        var detailsResponse: DetailsResponse? = null
        var marvelCharactersResponse: MarvelCharactersResponse? = null
        var exception: Exception? = null
        var page: Int? = 0
        var offset: Int? = 0
        override suspend fun listCharacters(characterId: String): DetailsResponse {
            if (exception != null) {
                throw exception!!
            }
            return detailsResponse!!.copy(id = characterId.toLong())
        }

        override suspend fun listCharacters(
            name: String?,
            offset: Int?,
            limit: Int?
        ): MarvelCharactersResponse {
            this.page = limit
            this.offset = offset
            if (exception != null) {
                throw exception!!
            }
            return marvelCharactersResponse!!
        }
    }

    private fun parseToJson(): MarvelCharactersResponse {
        return Json.decodeFromString<MarvelCharactersResponse>(
            ClassLoader.getSystemResource("characters/characters_response_ok.json").readText()
        )
    }
}
