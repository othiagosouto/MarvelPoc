package dev.thiagosouto.marvelpoc.features.character.detais

import app.cash.turbine.test
import dev.thiagosouto.marvelpoc.data.CharacterDetails
import dev.thiagosouto.marvelpoc.data.Comics
import dev.thiagosouto.marvelpoc.data.Dispatchers
import dev.thiagosouto.marvelpoc.data.mappers.ComicsMapper
import dev.thiagosouto.marvelpoc.features.character.details.CharacterDetailsViewModel
import dev.thiagosouto.marvelpoc.features.character.details.DetailsViewState
import dev.thiagosouto.marvelpoc.features.character.details.Intent
import dev.thiagosouto.marvelpoc.features.character.details.domain.DetailsViewStateMapper
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

internal class CharacterDetailsViewModelTest {
    private val mapper = DetailsViewStateMapper(ComicsMapper())
    private lateinit var viewModel: CharacterDetailsViewModel

    @Test
    fun `should publish expected states`() = runBlocking {
        val ids = listOf<Long>(1, 2, 3, 4)
        val characterDetails = CharacterDetails(
            id = 300L,
            name = "character-name",
            description = "character-description",
            imageUrl = "image-url",
            comics = ids.map(::comicsDomain)
        )
        viewModel = CharacterDetailsViewModel(
            { characterDetails },
            mapper,
            Dispatchers()
        )


        viewModel.run {
            state.test {
                viewModel.process(Intent.OpenScreen(300L))
                val emissions = listOf(awaitItem(), awaitItem(), awaitItem())
                val viewStates = listOf(
                    DetailsViewState.Idle,
                    DetailsViewState.Loading,
                    DetailsViewState.Loaded(
                        name = characterDetails.name,
                        description = characterDetails.description,
                        imageUrl = characterDetails.imageUrl,
                        comics = ids.map(::comicsView)
                    )
                )

                assertEquals(viewStates, actual = emissions)
            }
        }
    }

    @Test
    fun `should emit closed state`() = runBlocking {
        viewModel = CharacterDetailsViewModel(
            { throw Exception() },
            mapper,
            Dispatchers()
        )

        viewModel.run {
            state.test {
                viewModel.process(Intent.CloseScreen)
                val emissions = listOf(awaitItem(), awaitItem())
                val viewStates = listOf(
                    DetailsViewState.Idle,
                    DetailsViewState.Closed
                )

                assertEquals(viewStates, actual = emissions)
            }
        }
    }

}

private fun comicsDomain(id: Long) =
    Comics(id = id, title = "title - $id", imageUrl = "thumb-$id")

private fun comicsView(id: Long) = dev.thiagosouto.marvelpoc.domain.model.Comics(
    title = "title - $id",
    thumbnailUrl = "thumb-$id"
)
