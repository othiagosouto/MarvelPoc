package dev.thiagosouto.marvelpoc.home.list

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.thiagosouto.marvelpoc.domain.model.Character
import dev.thiagosouto.marvelpoc.domain.services.CharacterListParams
import dev.thiagosouto.marvelpoc.domain.services.CharacterListService
import dev.thiagosouto.marvelpoc.home.CoroutineTestRule
import dev.thiagosouto.marvelpoc.home.fakes.FavoritesRepositoryFake
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class CharactersViewModelTest {
    private val serviceFake = CharacterListServiceFake(listOf(character))

    private lateinit var viewModel: CharactersViewModel
    private lateinit var favoritesRepositoryFake: FavoritesRepositoryFake

    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

    @Before
    fun setup() {
        favoritesRepositoryFake = FavoritesRepositoryFake(mutableListOf(character))
        viewModel = CharactersViewModel(
            serviceFake, favoritesRepositoryFake
        )
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

        viewModel.favoriteClick(character)

        assertThat(favoritesRepositoryFake.favorites).contains(character)
    }

    @Test
    fun `load Given call Then paginate`() = runTest {
        serviceFake.params = CharacterListParams(pageSize = 20, null)

        viewModel.load()

        viewModel.state.test {
            assertThat(listOf(awaitItem()))
                .isEqualTo(listOf(CharacterViewState.Loaded(listOf(character))))
        }
    }

    private class CharacterListServiceFake(private val characters: List<Character>) :
        CharacterListService {
        lateinit var params: CharacterListParams

        override val source: Flow<List<Character>> = flowOf(characters)

        override suspend fun fetch(input: CharacterListParams) {
            assertThat(input).isEqualTo(params)
        }
    }

    private companion object {
        val character = Character(
            id = 1L,
            name = "",
            thumbnailUrl = "",
            description = "",
            favorite = true
        )
    }
}
