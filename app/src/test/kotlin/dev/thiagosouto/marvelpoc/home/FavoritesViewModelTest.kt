package dev.thiagosouto.marvelpoc.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PositionalDataSource
import dev.thiagosouto.marvelpoc.data.CharactersRepository
import dev.thiagosouto.marvelpoc.data.character.CharacterLocalContract
import dev.thiagosouto.marvelpoc.data.character.CharacterRemoteContract
import dev.thiagosouto.marvelpoc.data.model.view.Character
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class FavoritesViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

    private lateinit var repository: CharactersRepository
    private lateinit var viewModel: FavoritesViewModel
    private lateinit var exceptionHandler: (Exception) -> Unit
    private lateinit var successHandler: () -> Unit
    private lateinit var remotePageSource: PositionalDataSource<Character>
    private lateinit var charactersList: List<Character>
    private val character = Character(1011334, "name", "thumbnail", "description", false)
    private val characterFavorite = Character(1011334, "name", "thumbnail", "description", true)

    private lateinit var characterRemoteContract: CharacterRemoteContract<Character>
    private lateinit var characterLocalContract: CharacterLocalContract<Character>

    @Before
    fun setup() {
        exceptionHandler = mockk(relaxed = true)
        successHandler = mockk(relaxed = true)

        charactersList = listOf(character)
        remotePageSource = FakeDataSource(charactersList)

        characterRemoteContract = mockk(relaxed = true)
        characterLocalContract = mockk(relaxed = true)

        repository =
            spyk(CharactersRepository(characterLocalContract, characterRemoteContract, mockk()))
        viewModel = spyk(FavoritesViewModel(repository))
    }

    @Test
    fun `favoriteClick should favorite item when favorite flag is true`() = runTest {

        viewModel.favoriteClick(characterFavorite)

        coVerify { repository.favoriteCharacter(characterFavorite) }
    }

    @Test
    fun `favoriteClick should post the position of the item that was unfavorited`() = runTest {
        viewModel.favoriteClick(character)

        coVerify { repository.unFavoriteCharacter(character) }
    }

    private companion object Mock {
        const val content = "content"
        const val ops = "Ops"
    }
}
