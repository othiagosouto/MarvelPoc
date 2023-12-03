package dev.thiagosouto.marvelpoc.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.thiagosouto.marvelpoc.data.CharacterDetails
import dev.thiagosouto.marvelpoc.data.Comics
import dev.thiagosouto.marvelpoc.data.Dispatchers
import dev.thiagosouto.marvelpoc.data.mappers.ComicsMapper
import dev.thiagosouto.marvelpoc.detail.domain.DetailsViewStateMapper
import dev.thiagosouto.marvelpoc.home.CoroutineTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
internal class CharacterDetailsViewModelTest {
    private val mapper = DetailsViewStateMapper(ComicsMapper())
    private lateinit var viewModel: CharacterDetailsViewModel

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

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
        viewModel = CharacterDetailsViewModel({ characterDetails }, mapper, Dispatchers())


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

                assertThat(emissions).isEqualTo(viewStates)
            }
        }
    }

    @Test
    fun `should emit closed state`() = runBlocking {
        viewModel = CharacterDetailsViewModel({ throw IOException() }, mapper, Dispatchers())

        viewModel.run {
            state.test {
                viewModel.process(Intent.CloseScreen)
                val emissions = listOf(awaitItem(), awaitItem())
                val viewStates = listOf(
                    DetailsViewState.Idle,
                    DetailsViewState.Closed
                )

                assertThat(emissions).isEqualTo(viewStates)
            }
        }
    }

}

private fun comicsDomain(id: Long) =
    Comics(id = id, title = "title - $id", imageUrl = "thumb-$id")

private fun comicsView(id: Long) = dev.thiagosouto.domain.model.Comics(
    title = "title - $id",
    thumbnailUrl = "thumb-$id"
)
