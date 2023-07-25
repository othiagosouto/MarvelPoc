package dev.thiagosouto.marvelpoc.data.retrofit

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.testing.TestPager
import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import dev.thiagosouto.marvelpoc.data.model.view.Character
import dev.thiagosouto.marvelpoc.data.retrofit.character.MarvelCharactersResponse
import dev.thiagosouto.marvelpoc.data.retrofit.ext.toCharacter
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
internal class CharactersPagingDataSourceTest {

    private val charactersList = parseToJson()
    private lateinit var api: CharactersBFFApi
    private val exception = Exception()
    private lateinit var errorCallback: (Exception) -> Unit
    private lateinit var characterList: List<Character>
    private lateinit var characterFavoredList: List<Character>
    private lateinit var provideFavoriteIds: suspend () -> List<Long>

    @Before
    fun setup() {
        api = mockk()
        val character = charactersList.data.results.first().toCharacter()
        characterList = listOf(character)
        characterFavoredList = listOf(character.copy(favorite = true))
        errorCallback = mockk(relaxed = true)
        provideFavoriteIds = mockk()
        coEvery { provideFavoriteIds.invoke() } returns emptyList()
    }

    @Test
    fun `refresh should call callback with expected list and position`() = runBlocking {
        val source = CharactersPagingDataSource(
            null,
            5,
            api,
            provideFavoriteIds
        )
        coEvery { api.listCharacters(null, 0, 5) } returns parseToJson()

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
        val loadFinishMock: () -> Unit = mockk(relaxed = true)
        coEvery { provideFavoriteIds.invoke() } returns listOf(1011334L)
        coEvery { api.listCharacters(null, 0, 5) } returns parseToJson()

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
    fun `refresh should call error callback when an HttpException occurs`()  {
        val httException =  HttpException(Response.error<ResponseBody>(500 , "some content".toResponseBody("plain/text".toMediaTypeOrNull())))
        coEvery { api.listCharacters(null, 0, 5) } throws httException

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
    fun `refresh should call error callback when an IOException occurs`()  {
        val exception =  IOException()
        coEvery { api.listCharacters(null, 0, 5) } throws exception

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
