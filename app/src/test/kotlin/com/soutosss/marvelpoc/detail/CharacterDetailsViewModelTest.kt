package com.soutosss.marvelpoc.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import app.cash.turbine.test
import com.soutosss.marvelpoc.data.CharacterDetails
import com.soutosss.marvelpoc.data.CharactersRepository
import com.soutosss.marvelpoc.data.Comics
import com.soutosss.marvelpoc.data.model.view.Character
import com.soutosss.marvelpoc.home.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
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
    fun `favoriteClick should favorite item when favorite flag is true`() = runTest {
            val characterDetails = CharacterDetails(
                id = 300L,
                name = "character-name",
                description = "character-description",
                imageUrl = "image-url",
                comics = emptyList()
            )
            coEvery { repository.fetchCharacterDetails("300") } returns characterDetails

            viewModel.run {
                this.state.test {
                    process(Intent.OpenScreen(300L))
                    assertEquals(DetailsViewState.Idle, awaitItem())
                    assertEquals(DetailsViewState.Loading, awaitItem())
                    assertEquals(
                        DetailsViewState.Loaded(
                            name = characterDetails.name,
                            description = characterDetails.description,
                            imageUrl = characterDetails.imageUrl
                        ), awaitItem()
                    )
                }
            }
        }
}
