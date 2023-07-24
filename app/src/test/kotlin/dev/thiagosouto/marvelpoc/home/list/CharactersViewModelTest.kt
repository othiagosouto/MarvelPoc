package dev.thiagosouto.marvelpoc.home.list

import com.google.common.truth.Truth.assertThat
import dev.thiagosouto.marvelpoc.R
import dev.thiagosouto.marvelpoc.data.CharactersRepository
import dev.thiagosouto.marvelpoc.data.model.view.Character
import dev.thiagosouto.marvelpoc.home.CoroutineTestRule
import dev.thiagosouto.marvelpoc.shared.EmptyDataException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class CharactersViewModelTest {
    private val repository: CharactersRepository = mockk(relaxed = true)
    private lateinit var viewModel: CharactersViewModel

    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

    @Before
    fun setup() {
        coEvery { repository.fetchFavoriteIds() } returns listOf(1L)
        viewModel = CharactersViewModel(repository)
    }

    @Test
    fun `init should initialize favoritesIds`() = runTest {
        assertThat(viewModel.favoritesIds).isEqualTo(listOf(1L))
    }

    @Test
    fun `handleException Given no search and exception EmptyDataException Then returns no data content`() {
        val result = viewModel.handleException(EmptyDataException())

        val expectedResult = Pair(R.string.empty_characters_home, dev.thiagosouto.marvelpoc.design.R.drawable.ic_deadpool)
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `handleException Given no search and exception Exception Then returns generic content`() {
        val result = viewModel.handleException(Exception())

        val expectedResult = Pair(R.string.home_error_loading, dev.thiagosouto.marvelpoc.design.R.drawable.thanos)
        assertThat(result).isEqualTo(expectedResult)
    }


    @Test
    fun `handleException Given search and exception EmptyDataException Then returns no data content`() {
        viewModel.searchedQuery = "eita"
        val result = viewModel.handleException(EmptyDataException())

        val expectedResult = Pair(R.string.empty_characters_searched, R.drawable.search_not_found)
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `handleException Given search and exception Exception Then returns generic content`() {
        viewModel.searchedQuery = "eita"
        val result = viewModel.handleException(Exception())

        val expectedResult = Pair(R.string.search_error_loading, dev.thiagosouto.marvelpoc.design.R.drawable.thanos)
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `favoriteClick Given no favorite Then should unfavorite`() = runTest {
        val character = Character.EMPTY

        viewModel.favoriteClick(character)

        coVerify { repository.unFavoriteCharacter(character) }
    }

    @Test
    fun `favoriteClick Given favorite Then should favorte`() = runTest {
        val character = Character.EMPTY.copy(favorite = true)
        val ids = listOf(1L, 2L)
        coEvery { repository.fetchFavoriteIds() } returns ids

        viewModel.favoriteClick(character)

        assertThat(viewModel.favoritesIds).isEqualTo(ids)
        coVerify { repository.favoriteCharacter(character) }
    }
}
