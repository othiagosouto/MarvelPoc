package dev.thiagosouto.marvelpoc.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import dev.thiagosouto.marvelpoc.data.FavoritesRepository
import dev.thiagosouto.marvelpoc.data.model.view.Character
import dev.thiagosouto.marvelpoc.home.fakes.FavoritesRepositoryFake
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

    private lateinit var repository: FavoritesRepository<Character>
    private lateinit var viewModel: FavoritesViewModel
    private lateinit var charactersList: MutableList<Character>
    private val character = Character(1011334, "name", "thumbnail", "description", false)
    private val characterFavorite = Character(1011334, "name", "thumbnail", "description", true)

    @Before
    fun setup() {
        charactersList = mutableListOf(character)
        repository = FavoritesRepositoryFake(charactersList)
        viewModel = FavoritesViewModel(repository)
    }

    @Test
    fun `favoriteClick should favorite item when favorite flag is true`() = runTest {

        viewModel.favoriteClick(characterFavorite)

        assertThat(charactersList).contains(characterFavorite)
    }

    @Test
    fun `favoriteClick should post the position of the item that was unfavorited`() = runTest {
        viewModel.favoriteClick(character)

        assertThat(charactersList).doesNotContain(character)
    }

    private companion object Mock {
        const val content = "content"
        const val ops = "Ops"
    }
}

