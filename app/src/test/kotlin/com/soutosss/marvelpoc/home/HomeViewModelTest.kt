package com.soutosss.marvelpoc.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.paging.PositionalDataSource
import com.google.common.truth.Truth.assertThat
import com.soutosss.marvelpoc.R
import com.soutosss.marvelpoc.data.CharactersRepository
import com.soutosss.marvelpoc.data.character.CharacterLocalContract
import com.soutosss.marvelpoc.data.character.CharacterRemoteContract
import com.soutosss.marvelpoc.data.model.view.Character
import com.soutosss.marvelpoc.shared.EmptyDataException
import com.soutosss.marvelpoc.shared.livedata.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class HomeViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

    private lateinit var repository: CharactersRepository
    private lateinit var viewModel: HomeViewModel
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

        characterRemoteContract = mockk()
        characterLocalContract = mockk()

        repository =
            spyk(CharactersRepository(characterLocalContract, characterRemoteContract, mockk()))
        viewModel = spyk(HomeViewModel(repository))
    }

    @Test
    fun `charactersPageListContent should post the characters from datasource`() = runTest {

            val successSlot = slot<() -> Unit>()
            every {
                repository.charactersDataSource(
                    null,
                    any(),
                    any(),
                    capture(successSlot)
                )
            } returns remotePageSource

            val item = viewModel.charactersPageListContent()

            var list: List<Character>? = null
            item.observe(provideLifecycleState(Lifecycle.State.RESUMED), Observer {
                list = it.snapshot()
            })
            successSlot.captured.invoke()
            assertThat(list).isEqualTo(charactersList)
            assertThat(viewModel.characters.value!!).isEqualTo(Result.Loaded)
        }

    @Test
    fun `charactersPageListContent should post an expected message for Exception`() = runTest {
            remotePageSource = FakeDataSource(emptyList(), Exception())
            val errorSlot = slot<(java.lang.Exception) -> Unit>()

            every {
                repository.charactersDataSource(
                    null,
                    any(),
                    capture(errorSlot),
                    any()
                )
            } returns remotePageSource
            var list: List<Character>? = null
            viewModel.charactersPageListContent()
                .observe(provideLifecycleState(Lifecycle.State.RESUMED), Observer {
                    list = it.snapshot()
                })

            errorSlot.captured.invoke(Exception())
            assertThat(list).isEqualTo(emptyList<Character>())

            assertThat(viewModel.characters.value!!).isEqualTo(
                Result.Error(
                    R.string.home_error_loading,
                    R.drawable.thanos
                )
            )
        }

    @Test
    fun `charactersPageListContent should post error when there is no characters available`() = runTest {
            remotePageSource = FakeDataSource(emptyList(), EmptyDataException())
            val errorSlot = slot<(java.lang.Exception) -> Unit>()
            every {
                repository.charactersDataSource(
                    null,
                    any(),
                    capture(errorSlot),
                    any()
                )
            } returns remotePageSource

            val item = viewModel.charactersPageListContent()

            var list: List<Character>? = null
            item.observe(provideLifecycleState(Lifecycle.State.RESUMED), Observer {
                list = it.snapshot()
            })
            errorSlot.captured.invoke(EmptyDataException())
            assertThat(list).isEqualTo(emptyList<Character>())

            assertThat(viewModel.characters.value!!).isEqualTo(
                Result.Error(
                    R.string.empty_characters_home,
                    R.drawable.ic_deadpool
                )
            )
        }

    @Test
    fun `charactersPageListContent should post error search error when didn't found any characters from search`() =
        runTest {
            remotePageSource = FakeDataSource(emptyList(), EmptyDataException())
            val errorSlot = slot<(java.lang.Exception) -> Unit>()
            every {
                repository.charactersDataSource(
                    "content",
                    any(),
                    capture(errorSlot),
                    any()
                )
            } returns remotePageSource

            viewModel.searchedQuery = "content"
            val item = viewModel.charactersPageListContent()

            var list: List<Character>? = null
            item.observe(provideLifecycleState(Lifecycle.State.RESUMED), Observer {
                list = it.snapshot()
            })

            errorSlot.captured.invoke(EmptyDataException())
            assertThat(list).isEqualTo(emptyList<Character>())
            assertThat(viewModel.characters.value!!).isEqualTo(
                Result.Error(
                    R.string.empty_characters_searched,
                    R.drawable.search_not_found
                )
            )
        }

    @Test
    fun `charactersPageListContent should post search fail error when search wasn't finished as expected`() =
        runTest {
            remotePageSource = FakeDataSource(emptyList(), Exception())
            val errorSlot = slot<(Exception) -> Unit>()
            every {
                repository.charactersDataSource(
                    "content",
                    any(),
                    capture(errorSlot),
                    any()
                )
            } returns remotePageSource

            viewModel.searchedQuery = "content"
            val item = viewModel.charactersPageListContent()

            var list: List<Character>? = null
            item.observe(provideLifecycleState(Lifecycle.State.RESUMED), Observer {
                list = it.snapshot()
            })

            errorSlot.captured.invoke(Exception())
            assertThat(list).isEqualTo(emptyList<Character>())

            assertThat(viewModel.characters.value!!).isEqualTo(
                Result.Error(
                    R.string.search_error_loading,
                    R.drawable.thanos
                )
            )
        }

    @Test
    fun `charactersFavorite should post all favorite characters available`() = runTest {
            val favoriteCharacters = listOf(characterFavorite)

            coEvery { characterLocalContract.favoriteList() } returns FakeCharacterDataSource(
                favoriteCharacters,
                null
            )

            val item = viewModel.charactersFavorite()
            var list: List<Character>? = null
            item.observe(provideLifecycleState(Lifecycle.State.RESUMED), Observer {
                list = it.snapshot()
            })
            assertThat(list).isEqualTo(favoriteCharacters)
        }

    @Test
    fun `charactersFavorite should post error with expected content when there's no favorite characters available`() =
        runTest {
            coEvery { characterLocalContract.favoriteList() } returns FakeCharacterDataSource(
                emptyList(),
                null
            )

            val item = viewModel.charactersFavorite()
            var list: List<Character>? = null
            item.observe(provideLifecycleState(Lifecycle.State.RESUMED), Observer {
                list = it.snapshot()
            })
            assertThat(list).isEqualTo(emptyList<Character>())
            assertThat(viewModel.favoriteCharacters.value).isEqualTo(
                Result.Error(
                    R.string.empty_characters_favorites,
                    R.drawable.ic_favorites
                )
            )
        }

    @Test
    fun `favoriteClick should favorite item when favorite flag is true`() = runTest {

            coEvery { repository.favoriteCharacter(characterFavorite) } returns Unit

            viewModel.favoriteClick(characterFavorite)

            coVerify { repository.favoriteCharacter(characterFavorite) }
        }

    @Test
    fun `favoriteClick should post the position of the item that was unfavorited`() = runTest {

            every {
                repository.charactersDataSource(
                    any(),
                    any(),
                    any(),
                    any()
                )
            } returns FakeDataSource(listOf(character.copy(id = 33), character))

            coEvery { characterLocalContract.favoriteIds() } returns listOf(1011334)
            coEvery { characterLocalContract.unFavorite(character) } returns character.id

            viewModel.charactersPageListContent()
                .observe(provideLifecycleState(Lifecycle.State.RESUMED), Observer {
                })

            viewModel.favoriteClick(character)

            assertThat(viewModel.changeAdapter.value).isEqualTo(1)
        }

    @Test
    fun `updating searchedQuery should init searchContent and do search for characters starting expected name`() =
        runTest {

            coEvery { characterLocalContract.favoriteIds() } returns emptyList()

            every {
                repository.charactersDataSource(
                    "Ops",
                    any(),
                    any(),
                    any()
                )
            } returns remotePageSource

            val item = viewModel.charactersPageListContent()

            viewModel.searchedQuery = "Ops"
            var characters: List<Character>? = null

            item.observe(provideLifecycleState(Lifecycle.State.RESUMED), Observer {
                characters = it.snapshot()
            })

            assertThat(characters).isEqualTo(charactersList)
        }

    @Test
    fun `isCharacterFavorite returns true for favorite recipe id`() =
        runTest {
            coEvery { characterLocalContract.favoriteIds() } returns listOf(1,2,3)
            val viewModel = spyk(HomeViewModel(repository))


            assertThat(viewModel.isCharacterFavorite(2)).isTrue()
        }

    @Test
    fun `isCharacterFavorite returns false for non favorite recipe id`() =
        runTest {
            coEvery { characterLocalContract.favoriteIds() } returns listOf(1,2,3)
            val viewModel = spyk(HomeViewModel(repository))

            assertThat(viewModel.isCharacterFavorite(6)).isFalse()
        }
}
