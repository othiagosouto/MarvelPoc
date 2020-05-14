package com.soutosss.marvelpoc.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.soutosss.marvelpoc.data.CharactersRepository
import com.soutosss.marvelpoc.data.network.character.toCharacterList
import com.soutosss.marvelpoc.data.model.view.Character
import com.soutosss.marvelpoc.home.CoroutineTestRule
import com.soutosss.marvelpoc.parseToJson
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CharacterDetailsViewModelTest {
    private lateinit var viewModel: CharacterDetailsViewModel
    private lateinit var repository: CharactersRepository

    @Before
    fun setup() {
        repository = mockk()
        viewModel = CharacterDetailsViewModel(repository)
    }

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

    @Test
    fun `favoriteClick should favorite item when favorite flag is true`() =
        coroutineTestRule.testDispatcher.runBlockingTest {
            val item = Character(30, "", "", "", true)

            coEvery { repository.favoriteCharacter(item) } returns Unit

            viewModel.favoriteClick(item)

            coVerify { repository.favoriteCharacter(item) }
        }

    @Test
    fun `favoriteClick should unfavorite when the flag is false`() =
        coroutineTestRule.testDispatcher.runBlockingTest {
            val item = parseToJson().toCharacterList().first()

            viewModel.favoriteClick(item)

            coVerify { repository.unFavoriteCharacter(item, emptyList()) }
        }

}