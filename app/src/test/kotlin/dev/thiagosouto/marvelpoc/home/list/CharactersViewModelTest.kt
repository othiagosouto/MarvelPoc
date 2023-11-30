package dev.thiagosouto.marvelpoc.home.list

import com.google.common.truth.Truth.assertThat
import dev.thiagosouto.marvelpoc.R
import dev.thiagosouto.marvelpoc.data.PagingService
import dev.thiagosouto.marvelpoc.data.model.view.Character
import dev.thiagosouto.marvelpoc.home.CoroutineTestRule
import dev.thiagosouto.marvelpoc.home.fakes.FavoritesRepositoryFake
import dev.thiagosouto.domain.exception.EmptyDataException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class CharactersViewModelTest {
    private val repository: PagingService<Character> =
        PagingService<Character> { queryText, pageSize -> throw IllegalStateException() }
    private lateinit var viewModel: CharactersViewModel
    private lateinit var favoritesRepositoryFake: FavoritesRepositoryFake

    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

    @Before
    fun setup() {
        favoritesRepositoryFake = FavoritesRepositoryFake(
            mutableListOf(
                Character(
                    id = 1L,
                    name = "",
                    thumbnailUrl = "",
                    description = "",
                    favorite = true
                )
            )
        )
        viewModel = CharactersViewModel(
            repository, favoritesRepositoryFake
        )
    }

    @Test
    fun `init should initialize favoritesIds`() = runTest {
        assertThat(viewModel.favoritesIds).isEqualTo(listOf(1L))
    }

    @Test
    fun `handleException Given no search and exception EmptyDataException Then returns no data content`() {
        val result = viewModel.handleException(EmptyDataException())

        val expectedResult = Pair(
            R.string.empty_characters_home,
            dev.thiagosouto.marvelpoc.design.R.drawable.ic_deadpool
        )
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `handleException Given no search and exception Exception Then returns generic content`() {
        val result = viewModel.handleException(Exception())

        val expectedResult =
            Pair(R.string.home_error_loading, dev.thiagosouto.marvelpoc.design.R.drawable.thanos)
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
    fun `favoriteClick Given no favorite Then should unFavorite`() = runTest {
        val character = Character.EMPTY

        viewModel.favoriteClick(character)

        assertThat(favoritesRepositoryFake.favorites).doesNotContain(character)
    }

    @Test
    fun `favoriteClick Given favorite Then should favorite`() = runTest {
        val character = Character.EMPTY.copy(id = 2L, favorite = true)
        val ids = listOf(1L, 2L)

        viewModel.favoriteClick(character)

        assertThat(viewModel.favoritesIds).isEqualTo(ids)
        assertThat(favoritesRepositoryFake.favorites).contains(character)
    }
}
