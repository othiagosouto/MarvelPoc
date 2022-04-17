package com.soutosss.marvelpoc.detail

import app.cash.turbine.test
import com.soutosss.marvelpoc.data.CharacterDetails
import com.soutosss.marvelpoc.data.CharactersRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CharacterDetailsViewModelTest {
    private lateinit var viewModel: CharacterDetailsViewModel
    private lateinit var repository: CharactersRepository

    @Before
    fun setup() {
        repository = mockk(relaxed = true)
        viewModel = CharacterDetailsViewModel(repository)
    }

    @Test
    fun `favoriteClick should favorite item when favorite flag is true`() = runBlocking {
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
