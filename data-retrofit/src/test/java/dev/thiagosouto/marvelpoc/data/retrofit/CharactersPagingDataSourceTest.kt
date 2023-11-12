package dev.thiagosouto.marvelpoc.data.retrofit

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.testing.TestPager
import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import dev.thiagosouto.marvelpoc.data.model.view.Character
import dev.thiagosouto.marvelpoc.data.retrofit.character.MarvelCharactersResponse
import dev.thiagosouto.marvelpoc.data.retrofit.character.details.DetailsResponse
import dev.thiagosouto.marvelpoc.data.retrofit.ext.toCharacter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
internal class CharactersPagingDataSourceTest {

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
        ids.clear()
    }

    @Test
    fun `refresh should call callback with expected list and position`() = runBlocking {
        val source = CharactersPagingDataSource(
            null,
            5,
            api,
            provideFavoriteIds
        )
        api.marvelCharactersResponse = parseToJson()

        val pager = TestPager(PagingConfig(5), source)

        runTest {

            val result = pager.refresh()
            assertThat(result).isInstanceOf(PagingSource.LoadResult.Page::class.java)
            assertThat(pager.getLastLoadedPage()!!.data).isEqualTo(charactersList.data.results.map { it.toCharacter() })
            val page = pager.getLastLoadedPage()
            assertThat(page).isNotNull()
        }
    }

    @Test
    fun `refresh should call callback with expected transformed list with favorite and position`() {
        ids.add(1011334L)
        api.marvelCharactersResponse = parseToJson()

        val source = CharactersPagingDataSource(
            null,
            5,
            api,
            provideFavoriteIds
        )
        val pager = TestPager(PagingConfig(5), source)

        runTest {

            val result = pager.refresh()
            assertThat(result).isInstanceOf(PagingSource.LoadResult.Page::class.java)
            assertThat(pager.getLastLoadedPage()!!.data).isEqualTo(characterFavoredList)
            val page = pager.getLastLoadedPage()
            assertThat(page).isNotNull()
        }
    }

    @Test
    fun `refresh should call error callback when an HttpException occurs`() {
        val httException = HttpException(
            Response.error<ResponseBody>(
                500,
                "some content".toResponseBody("plain/text".toMediaTypeOrNull())
            )
        )
        api.exception = httException

        val source = CharactersPagingDataSource(
            null,
            5,
            api,
            provideFavoriteIds
        )
        val pager = TestPager(PagingConfig(5), source)

        runTest {
            val result = pager.refresh()
            assertThat(result).isInstanceOf(PagingSource.LoadResult.Error::class.java)
            assertThat(pager.getLastLoadedPage()).isNull()
        }
    }

    @Test
    fun `refresh should call error callback when an IOException occurs`() {
        val exception = IOException()
        api.exception = exception

        val source = CharactersPagingDataSource(
            null,
            5,
            api,
            provideFavoriteIds
        )
        val pager = TestPager(PagingConfig(5), source)

        runTest {
            val result = pager.refresh()
            assertThat(result).isInstanceOf(PagingSource.LoadResult.Error::class.java)
            assertThat(pager.getLastLoadedPage()).isNull()
        }
    }


    private fun parseToJson(): MarvelCharactersResponse {
        return Gson().fromJson(
            ClassLoader.getSystemResource("characters/characters_response_ok.json").readText(),
            MarvelCharactersResponse::class.java
        )
    }
}

internal class FakeBff : CharactersBFFApi {
    var detailsResponse: DetailsResponse? = null
    var marvelCharactersResponse: MarvelCharactersResponse? = null
    var exception: Exception? = null
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
        if (exception != null) {
            throw exception!!
        }
        return marvelCharactersResponse!!
    }
}
