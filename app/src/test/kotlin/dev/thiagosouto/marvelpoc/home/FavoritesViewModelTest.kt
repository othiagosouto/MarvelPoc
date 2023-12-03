package dev.thiagosouto.marvelpoc.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import dev.thiagosouto.domain.model.Character
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

    private lateinit var repository: FavoritesRepositoryFake
    private lateinit var viewModel: FavoritesViewModel
    private val character = Character(1011334, "name", "thumbnail", "description", false)
    private val characterFavorite = Character(1011334, "name", "thumbnail", "description", true)

    @Before
    fun setup() {
        repository = FavoritesRepositoryFake(mutableListOf(character))
        viewModel = FavoritesViewModel(repository)
    }

    @Test
    fun `favoriteClick should favorite item when favorite flag is true`() = runTest {

        viewModel.favoriteClick(characterFavorite)

        assertThat(repository.favorites).contains(characterFavorite)
    }

    @Test
    fun `favoriteClick should post the position of the item that was unfavorited`() = runTest {
        viewModel.favoriteClick(character)

        assertThat(repository.favorites).doesNotContain(character)
    }

    private companion object Mock {
        const val content = "content"
        const val ops = "Ops"
    }
}

