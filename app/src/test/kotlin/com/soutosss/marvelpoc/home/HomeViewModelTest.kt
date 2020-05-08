package com.soutosss.marvelpoc.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import com.soutosss.marvelpoc.R
import com.soutosss.marvelpoc.shared.livedata.Result
import com.soutosss.marvelpoc.data.CharactersRepository
import com.soutosss.marvelpoc.data.model.character.MarvelCharactersResponse
import com.soutosss.marvelpoc.data.model.character.toCharacterHomeList
import com.soutosss.marvelpoc.data.model.view.CharacterHome
import io.mockk.coEvery
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

    @Before
    fun setup() {
        repository = mockk()
        viewModel = HomeViewModel(repository)
    }

    @Test
    fun fetchCharacters_shouldPostListWhenApiReturnsOK() =
        coroutineTestRule.testDispatcher.runBlockingTest {
            val charactersList = parseToJson().toCharacterHomeList()
            coEvery { repository.fetchAllCharacters() } returns charactersList

            viewModel.fetchCharacters()
            val value = viewModel.characters.value!! as Result.Loaded
            assertThat(value).isEqualTo(Result.Loaded(charactersList))
        }

    @Test
    fun fetchCharacters_shouldPostErrorResWhenFailed() =
        coroutineTestRule.testDispatcher.runBlockingTest {

            coEvery { repository.fetchAllCharacters() } throws Exception()

            viewModel.fetchCharacters()

            assertThat(viewModel.characters.value!!).isEqualTo(Result.Error(R.string.home_error_loading, R.drawable.thanos))
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
