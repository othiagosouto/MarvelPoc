package dev.thiagosouto.marvelpoc.home.list

import com.google.common.truth.Truth.assertThat
import dev.thiagosouto.marvelpoc.domain.model.Character
import dev.thiagosouto.marvelpoc.domain.services.CharacterListService
import dev.thiagosouto.marvelpoc.home.CoroutineTestRule
import dev.thiagosouto.marvelpoc.home.fakes.FavoritesRepositoryFake
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class CharactersViewModelTest {
    private val repository: CharacterListService =
        CharacterListService { input -> throw IllegalStateException() }
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
}
