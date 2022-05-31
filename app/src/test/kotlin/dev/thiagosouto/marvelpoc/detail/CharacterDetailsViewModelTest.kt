package dev.thiagosouto.marvelpoc.detail

import dev.thiagosouto.marvelpoc.data.CharacterDetails
import dev.thiagosouto.marvelpoc.data.CharactersRepository
import dev.thiagosouto.marvelpoc.data.Comics
import dev.thiagosouto.marvelpoc.data.mappers.ComicsMapper
import dev.thiagosouto.marvelpoc.home.CoroutineTestRule
import dev.thiagosouto.marvelpoc.shared.mvi.MviView
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verifySequence
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CharacterDetailsViewModelTest {
    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

    private val comicsMapper = ComicsMapper()
    private lateinit var viewModel: CharacterDetailsViewModel
    private lateinit var repository: CharactersRepository
    private val mviView = mockk<MviView<DetailsViewState>>(relaxed = true)

    @Before
    fun setup() {
        repository = mockk(relaxed = true)
        viewModel = CharacterDetailsViewModel(repository, comicsMapper)
    }

    @Test
    fun `should publish expected states`() =  runTest  {
        val ids = listOf<Long>(1, 2, 3, 4)
        val characterDetails = CharacterDetails(
            id = 300L,
            name = "character-name",
            description = "character-description",
            imageUrl = "image-url",
            comics = ids.map(::comicsDomain)
        )
        coEvery { repository.fetchCharacterDetails("300") } returns characterDetails

        viewModel.bind(mviView)
        viewModel.process(Intent.OpenScreen(300L))

        verifySequence {
            mviView.render(DetailsViewState.Idle)
            mviView.render(DetailsViewState.Loading)
            mviView.render(
                DetailsViewState.Loaded(
                    name = characterDetails.name,
                    description = characterDetails.description,
                    imageUrl = characterDetails.imageUrl,
                    comics = ids.map(::comicsView)
                )
            )
        }
    }

    private fun comicsDomain(id: Long) =
        Comics(id = id, title = "title - $id", imageUrl = "thumb-$id")

    private fun comicsView(id: Long) = dev.thiagosouto.marvelpoc.data.model.view.Comics(
        title = "title - $id",
        thumbnailUrl = "thumb-$id"
    )
}
