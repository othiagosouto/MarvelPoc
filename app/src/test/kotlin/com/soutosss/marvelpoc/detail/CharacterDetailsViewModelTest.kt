package com.soutosss.marvelpoc.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.soutosss.marvelpoc.data.CharactersRepository
import com.soutosss.marvelpoc.data.model.view.Character
import com.soutosss.marvelpoc.home.CoroutineTestRule
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

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

    @Before
    fun setup() {
        repository = mockk(relaxed = true)
        viewModel = CharacterDetailsViewModel(repository)
    }

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
            val item = Character(300, "name", "url", "Description", false)

            viewModel.favoriteClick(item)

            coVerify { repository.unFavoriteCharacter(item, emptyList()) }
        }

}