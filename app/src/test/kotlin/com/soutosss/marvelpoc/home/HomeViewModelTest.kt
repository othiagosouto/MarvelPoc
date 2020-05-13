package com.soutosss.marvelpoc.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import com.soutosss.marvelpoc.R
import com.soutosss.marvelpoc.data.CharactersRepository
import com.soutosss.marvelpoc.data.local.CharacterHomeDAO
import com.soutosss.marvelpoc.data.model.EmptyDataException
import com.soutosss.marvelpoc.data.model.character.MarvelCharactersResponse
import com.soutosss.marvelpoc.data.model.character.toCharacterHomeList
import com.soutosss.marvelpoc.data.model.view.CharacterHome
import com.soutosss.marvelpoc.data.network.CharactersApi
import com.soutosss.marvelpoc.shared.livedata.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

    private lateinit var repository: CharactersRepository
    private lateinit var viewModel: HomeViewModel
    private lateinit var exceptionHandler: (Exception) -> Unit
    private lateinit var successHandler: () -> Unit
    private lateinit var api: CharactersApi
    private lateinit var dao: CharacterHomeDAO
    private lateinit var charactersList: List<CharacterHome>

    @Before
    fun setup() {
        exceptionHandler = mockk(relaxed = true)
        successHandler = mockk(relaxed = true)
        charactersList = parseToJson().toCharacterHomeList()
        api = mockk()
        dao = mockk()
        repository = CharactersRepository(api, dao)
        viewModel = HomeViewModel(repository)
    }

    @Test
    fun `charactersPageListContent should post the characters from datasource`() =
        coroutineTestRule.testDispatcher.runBlockingTest {

            coEvery { api.listCharacters(null, any(), any()) } returns parseToJson()
            coEvery { dao.favoriteIds() } returns emptyList()

            val item = viewModel.charactersPageListContent()

            var list: List<CharacterHome>? = null
            item.observe(provideLifecycleState(Lifecycle.State.RESUMED), Observer {
                list = it.snapshot()
            })
            assertThat(list).isEqualTo(parseToJson().toCharacterHomeList())
            assertThat(viewModel.characters.value!!).isEqualTo(Result.Loaded)
        }

    @Test
    fun `charactersPageListContent should post an expected message for Exception`() =
        coroutineTestRule.testDispatcher.runBlockingTest {
            val exception = Exception()

            coEvery { api.listCharacters(null, any(), any()) } throws exception

            viewModel.charactersPageListContent()
                .observe(provideLifecycleState(Lifecycle.State.RESUMED), Observer {
                })

            assertThat(viewModel.characters.value!!).isEqualTo(
                Result.Error(
                    R.string.home_error_loading,
                    R.drawable.thanos
                )
            )
        }

    @Test
    fun `charactersPageListContent should post error when there is no characters available`() =
        coroutineTestRule.testDispatcher.runBlockingTest {
            val data = parseToJson().data
            coEvery {
                api.listCharacters(
                    null,
                    any(),
                    any()
                )
            } returns parseToJson().copy(data = data.copy(results = emptyList()))
            coEvery { dao.favoriteIds() } returns emptyList()

            val item = viewModel.charactersPageListContent()

            var list: List<CharacterHome>? = null
            item.observe(provideLifecycleState(Lifecycle.State.RESUMED), Observer {
                list = it.snapshot()
            })

            assertThat(list).isEqualTo(emptyList<CharacterHome>())

            assertThat(viewModel.characters.value!!).isEqualTo(
                Result.Error(
                    R.string.empty_characters_home,
                    R.drawable.ic_deadpool
                )
            )
        }

    @Test
    fun `charactersPageListContent should post error search error when didn't found any characters from search`() =
        coroutineTestRule.testDispatcher.runBlockingTest {
            val data = parseToJson().data
            coEvery {
                api.listCharacters(
                    "content",
                    any(),
                    any()
                )
            } returns parseToJson().copy(data = data.copy(results = emptyList()))
            coEvery { dao.favoriteIds() } returns emptyList()

            viewModel.initSearchQuery("content")
            val item = viewModel.charactersPageListContent()

            var list: List<CharacterHome>? = null
            item.observe(provideLifecycleState(Lifecycle.State.RESUMED), Observer {
                list = it.snapshot()
            })

            assertThat(list).isEqualTo(emptyList<CharacterHome>())

            assertThat(viewModel.characters.value!!).isEqualTo(
                Result.Error(
                    R.string.empty_characters_searched,
                    R.drawable.search_not_found
                )
            )
        }


    @Test
    fun `charactersPageListContent should post search fail error when search wasn't finished as expected`() =
        coroutineTestRule.testDispatcher.runBlockingTest {
            coEvery { api.listCharacters("content", any(), any()) } throws Exception()

            viewModel.initSearchQuery("content")
            val item = viewModel.charactersPageListContent()

            var list: List<CharacterHome>? = null
            item.observe(provideLifecycleState(Lifecycle.State.RESUMED), Observer {
                list = it.snapshot()
            })

            assertThat(list).isEqualTo(emptyList<CharacterHome>())

            assertThat(viewModel.characters.value!!).isEqualTo(
                Result.Error(
                    R.string.search_error_loading,
                    R.drawable.thanos
                )
            )
        }


    @Test
    fun `charactersPageListContent should post an item item with favorite when id its stored in the favorites table`() =
        coroutineTestRule.testDispatcher.runBlockingTest {

            coEvery { api.listCharacters(null, any(), any()) } returns parseToJson()
            coEvery { dao.favoriteIds() } returns listOf(1011334)

            val item = viewModel.charactersPageListContent()

            var list: List<CharacterHome>? = null
            item.observe(provideLifecycleState(Lifecycle.State.RESUMED), Observer {
                list = it.snapshot()
            })
            val expectedResult = parseToJson().toCharacterHomeList()
            expectedResult.first().favorite = true
            assertThat(list).isEqualTo(expectedResult)
            assertThat(viewModel.characters.value!!).isEqualTo(Result.Loaded)
        }

    @Test
    fun `charactersPageListContent should post an expected error message for EmptyDataException`() =
        coroutineTestRule.testDispatcher.runBlockingTest {
            val exception = EmptyDataException()

            coEvery { api.listCharacters(null, any(), any()) } throws exception

            viewModel.charactersPageListContent()
                .observe(provideLifecycleState(Lifecycle.State.RESUMED), Observer {
                })

            assertThat(viewModel.characters.value!!).isEqualTo(
                Result.Error(
                    R.string.empty_characters_home,
                    R.drawable.ic_deadpool
                )
            )
        }

    @Test
    fun `charactersFavorite should post all favorite characters available`() =
        coroutineTestRule.testDispatcher.runBlockingTest {

            val favoriteCharacters = parseToJson().toCharacterHomeList()
            favoriteCharacters.first().favorite = true

            coEvery { dao.getAll() } returns FakeHomeDataSource(favoriteCharacters)

            val item = viewModel.charactersFavorite()
            var list: List<CharacterHome>? = null
            item.observe(provideLifecycleState(Lifecycle.State.RESUMED), Observer {
                list = it.snapshot()
            })
            assertThat(list).isEqualTo(favoriteCharacters)
        }

    @Test
    fun `charactersFavorite should post error with expected content when there's no favorite characters available`() =
        coroutineTestRule.testDispatcher.runBlockingTest {
            coEvery { dao.getAll() } returns FakeHomeDataSource(emptyList())

            val item = viewModel.charactersFavorite()
            var list: List<CharacterHome>? = null
            item.observe(provideLifecycleState(Lifecycle.State.RESUMED), Observer {
                list = it.snapshot()
            })
            assertThat(list).isEqualTo(emptyList<CharacterHome>())
            assertThat(viewModel.favoriteCharacters.value).isEqualTo(
                Result.Error(
                    R.string.empty_characters_favorites,
                    R.drawable.ic_favorites
                )
            )
        }

    @Test
    fun `favoriteClick should favorite item when favorite flag is true`() =
        coroutineTestRule.testDispatcher.runBlockingTest {
            val item = CharacterHome(30, "", "", "", true)

            coEvery { repository.favoriteCharacterHome(item) } returns Unit

            viewModel.favoriteClick(item)

            coVerify { repository.favoriteCharacterHome(item) }

        }

    @Test
    fun `favoriteClick should post the position of the item that was unfavorited`() =
        coroutineTestRule.testDispatcher.runBlockingTest {
            val item = parseToJson().toCharacterHomeList().first()

            coEvery { api.listCharacters(null, any(), any()) } returns parseToJson()
            coEvery { dao.favoriteIds() } returns listOf(1011334)
            coEvery { dao.insertAll(item) } returns Unit
            coEvery { dao.delete(item) } returns Unit

            viewModel.charactersPageListContent()
                .observe(provideLifecycleState(Lifecycle.State.RESUMED), Observer {
                })

            viewModel.favoriteClick(item)

            assertThat(viewModel.changeAdapter.value).isEqualTo(0)
        }

    @Test
    fun `initSearchQuery should init searchContent and do search for characters starting expected name`() =
        coroutineTestRule.testDispatcher.runBlockingTest {

            coEvery { api.listCharacters("Ops", any(), any()) } returns parseToJson()
            coEvery { dao.favoriteIds() } returns emptyList()

            val item = viewModel.charactersPageListContent()

            viewModel.initSearchQuery("Ops")
            item.observe(provideLifecycleState(Lifecycle.State.RESUMED), Observer {
            })

            coVerify { api.listCharacters("Ops", any(), any()) }
        }

}

private fun parseToJson(): MarvelCharactersResponse {
    return Gson().fromJson(
        "/characters/characters_response_ok.json".toJson(),
        MarvelCharactersResponse::class.java
    )
}

private fun String.toJson(): String {
    return this::class.java.javaClass.getResource(this)!!.readText()
}
