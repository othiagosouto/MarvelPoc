package com.soutosss.marvelpoc.detail

import app.cash.turbine.test
import dev.thiagosouto.marvelpoc.data.CharacterDetails
import dev.thiagosouto.marvelpoc.data.CharactersRepository
import dev.thiagosouto.marvelpoc.data.Comics
import dev.thiagosouto.marvelpoc.data.mappers.ComicsMapper
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CharacterDetailsViewModelTest {
    private val comicsMapper = ComicsMapper()
    private lateinit var viewModel: CharacterDetailsViewModel
    private lateinit var repository: CharactersRepository

    @Before
    fun setup() {
        repository = mockk(relaxed = true)
        viewModel = CharacterDetailsViewModel(repository, comicsMapper)
    }

    @Test
    fun `favoriteClick should favorite item when favorite flag is true`() = runBlocking {
        val ids = listOf<Long>(1, 2, 3, 4)
        val characterDetails = CharacterDetails(
            id = 300L,
            name = "character-name",
            description = "character-description",
            imageUrl = "image-url",
            comics = ids.map(::comicsDomain)
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
                        imageUrl = characterDetails.imageUrl,
                        comics = ids.map(::comicsView)
                    ), awaitItem()
                )
            }
        }
    }


    private fun comicsDomain(id: Long) =
        Comics(id = id, title = "title - $id", imageUrl = "thumb-$id")

    private fun comicsView(id: Long) = dev.thiagosouto.marvelpoc.data.model.view.Comics(
        title = "title - $id",
        thumbnailUrl = "thumb-$id"
    )
}
