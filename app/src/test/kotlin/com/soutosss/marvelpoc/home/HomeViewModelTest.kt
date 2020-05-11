package com.soutosss.marvelpoc.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import com.soutosss.marvelpoc.R
import com.soutosss.marvelpoc.data.CharactersDataSource
import com.soutosss.marvelpoc.data.CharactersRepository
import com.soutosss.marvelpoc.data.local.CharacterHomeDAO
import com.soutosss.marvelpoc.data.model.EmptyDataException
import com.soutosss.marvelpoc.data.model.character.MarvelCharactersResponse
import com.soutosss.marvelpoc.data.model.character.toCharacterHomeList
import com.soutosss.marvelpoc.data.model.view.CharacterHome
import com.soutosss.marvelpoc.data.network.CharactersApi
import com.soutosss.marvelpoc.shared.livedata.Result
import io.mockk.*
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
    private lateinit var dataSource: CharactersDataSource
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
            dataSource =
                CharactersDataSource(null, this, api, dao, exceptionHandler, successHandler)

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
            dataSource =
                CharactersDataSource(null, this, api, dao, exceptionHandler, successHandler)
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
    fun `charactersPageListContent should post an item item with favorite when id its stored in the favorites table`() =
        coroutineTestRule.testDispatcher.runBlockingTest {
            dataSource =
                CharactersDataSource(null, this, api, dao, exceptionHandler, successHandler)

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
            dataSource =
                CharactersDataSource(null, this, api, dao, exceptionHandler, successHandler)
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
//
//    @Test
//    fun `fetchFavoriteCharacters should post all favorite characters available`() =
//        coroutineTestRule.testDispatcher.runBlockingTest {
//            val favoriteCharacters = parseToJson().toCharacterHomeList()
//
//            coEvery { repository.fetchFavoriteCharacters() } returns favoriteCharacters
//
//            viewModel.fetchFavoriteCharacters()
//
//            assertThat(viewModel.favoriteCharacters.value!!).isEqualTo(
//                Result.Loaded(
//                    favoriteCharacters
//                )
//            )
//        }
//
//    @Test
//    fun `fetchFavoriteCharacters should post error when the call fail`() =
//        coroutineTestRule.testDispatcher.runBlockingTest {
//
//            coEvery { repository.fetchFavoriteCharacters() } throws Exception()
//
//            viewModel.fetchFavoriteCharacters()
//
//            assertThat(viewModel.favoriteCharacters.value!!).isEqualTo(
//                Result.Error(
//                    R.string.favorite_error_loading,
//                    R.drawable.thanos
//                )
//            )
//        }
//
//    @Test
//    fun `fetchFavoriteCharacters should post error with expected content when there's no favorite characters available` () =
//        coroutineTestRule.testDispatcher.runBlockingTest {
//
//            coEvery { repository.fetchFavoriteCharacters() } returns emptyList()
//
//            viewModel.fetchFavoriteCharacters()
//
//            assertThat(viewModel.favoriteCharacters.value!!).isEqualTo(
//                Result.Error(
//                    R.string.empty_characters_favorites,
//                    R.drawable.ic_favorites
//                )
//            )
//        }
//
//    @Test
//    fun `fetchCharacters should post error when there is no characters available`() =
//        coroutineTestRule.testDispatcher.runBlockingTest {
//
//            coEvery { repository.fetchAllCharacters() } returns emptyList()
//
//            viewModel.fetchCharacters()
//
//            assertThat(viewModel.characters.value!!).isEqualTo(
//                Result.Error(
//                    R.string.empty_characters_home,
//                    R.drawable.ic_deadpool
//                )
//            )
//        }
//
//    @Test
//    fun `favoriteClick should favorite item when favorite flag is true`() =
//        coroutineTestRule.testDispatcher.runBlockingTest {
//            val item = CharacterHome(30, "", "", true)
//
//            coEvery { repository.favoriteCharacterHome(item) } returns Unit
//            coEvery { repository.fetchFavoriteCharacters() } returns listOf(item)
//
//            viewModel.favoriteClick(item)
//
//            coVerifyOrder {
//                repository.favoriteCharacterHome(item)
//                repository.fetchFavoriteCharacters()
//            }
//
//        }
//
//    @Test
//    fun `favoriteClick should unfavorite item when favoriteClick is false`() =
//        coroutineTestRule.testDispatcher.runBlockingTest {
//            val favoriteItem = CharacterHome(30, "", "", true)
//            val item = CharacterHome(30, "", "", false)
//            val list  =  listOf(favoriteItem)
//
//            coEvery { repository.fetchFavoriteCharacters() } returns emptyList()
//            coEvery { repository.fetchAllCharacters() } returns list
//            coEvery { repository.unFavoriteCharacterHome(item, list) } returns 30
//
//            viewModel.fetchCharacters()
//            viewModel.favoriteClick(item)
//
//            assertThat(viewModel.changeAdapter.value).isEqualTo(30)
//
//        }
//
//    @Test
//    fun `initSearchQuery should init searchContent and do search for characters starting expected name`() =
//        coroutineTestRule.testDispatcher.runBlockingTest {
//            val charactersList = parseToJson().toCharacterHomeList()
//            coEvery { repository.fetchAllCharacters("Ops") } returns charactersList
//
//            viewModel.initSearchQuery("Ops")
//            val value = viewModel.characters.value!! as Result.Loaded
//            assertThat(value).isEqualTo(Result.Loaded(charactersList))
//        }

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
