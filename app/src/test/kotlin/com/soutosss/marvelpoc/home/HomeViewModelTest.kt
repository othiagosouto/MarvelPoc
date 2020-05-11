package com.soutosss.marvelpoc.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import androidx.paging.PositionalDataSource
import com.google.gson.Gson
import com.soutosss.marvelpoc.data.CharactersDataSource
import com.soutosss.marvelpoc.data.CharactersRepository
import com.soutosss.marvelpoc.data.local.CharacterHomeDAO
import com.soutosss.marvelpoc.data.model.character.MarvelCharactersResponse
import com.soutosss.marvelpoc.data.model.character.toCharacterHomeList
import com.soutosss.marvelpoc.data.model.view.CharacterHome
import com.soutosss.marvelpoc.data.network.CharactersApi
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
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

    @Before
    fun setup() {
        repository = mockk()
        viewModel = HomeViewModel(repository)
    }

    @Test
    fun `fetchCharacters should post the content from the call when is not empty`() =
        coroutineTestRule.testDispatcher.runBlockingTest {
            val charactersList = parseToJson().toCharacterHomeList()
            val api: CharactersApi = mockk()
            val dao: CharacterHomeDAO = mockk()
            val mockedSource = CharactersDataSource("", this, api, dao, {}, {})
            coEvery { api.listCharacters(null, any(), any()) } returns parseToJson()
            coEvery { dao.favoriteIds() } returns emptyList()
            val callback: PositionalDataSource.LoadInitialCallback<CharacterHome> =
                mockk(relaxed = true)
            every {
                repository.charactersDataSource(
                    null,
                    any(),
                    any(),
                    any()
                )
            } returns mockedSource

            val item = viewModel.charactersPageListContent()

            mockedSource.loadInitial(
                PositionalDataSource.LoadInitialParams(0, 5, 5, false),
                callback
            )

            item.observe(mockLifecycle(), Observer {

                println("EITA 1")
            })
            verify { callback.onResult(charactersList, 0) }
            println("EITA 2")
        }


    private fun mockLifecycle(): LifecycleOwner {
        val owner = mockk<LifecycleOwner>(relaxed = true)
        val lifecycle = LifecycleRegistry(owner)
        lifecycle.currentState = Lifecycle.State.RESUMED
        every { owner.lifecycle } returns lifecycle
        return owner
    }
//
//    @Test
//    fun `fetchCharacters should post an error when the call fail`() =
//        coroutineTestRule.testDispatcher.runBlockingTest {
//
//            coEvery { repository.fetchAllCharacters() } throws Exception()
//
//            viewModel.fetchCharacters()
//
//            assertThat(viewModel.characters.value!!).isEqualTo(
//                Result.Error(
//                    R.string.home_error_loading,
//                    R.drawable.thanos
//                )
//            )
//        }
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
