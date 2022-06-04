package dev.thiagosouto.marvelpoc.data.room

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PositionalDataSource
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import dev.thiagosouto.marvelpoc.data.model.view.Character
import dev.thiagosouto.marvelpoc.data.room.ext.toCharacter
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class CharacterLocalRoomDataSourceTest {

    private lateinit var context: Context
    private lateinit var appDatabase: AppDatabase
    private lateinit var characterLocalDAO: CharacterLocalDAO
    private lateinit var dataSource: CharacterLocalRoomDataSource
    private val characterLocal =
        CharacterLocal(
            30,
            NAME,
            URL,
            DESCRIPTION,
            true
        )

    private val character =
        Character(
            30,
            NAME,
            URL,
            DESCRIPTION,
            true
        )

    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        characterLocalDAO = appDatabase.charactersLocalDao()
        dataSource = CharacterLocalRoomDataSource(characterLocalDAO)
    }

    @Test
    fun favorite_shouldInsertTheExpectedItemToFavoriteAndReturnItsId() =
        coroutineTestRule.testDispatcher.runBlockingTest {
            val character =
                Character(
                    30,
                    NAME,
                    URL,
                    DESCRIPTION,
                    true
                )
            assertThat(dataSource.favorite(character)).isEqualTo(30)
        }

    @Test
    fun unFavorite_shouldUnFavoriteAndReturnsTheIdOfItemRemoved() =
        coroutineTestRule.testDispatcher.runBlockingTest {
            characterLocalDAO.favorite(characterLocal)
            assertThat(dataSource.unFavorite(character)).isEqualTo(30)
        }

    @Test
    fun favoriteIds_shouldReturnAllIdsOfFavoriteItems() =
        coroutineTestRule.testDispatcher.runBlockingTest {
            characterLocalDAO.favorite(characterLocal.copy(id = 1))
            characterLocalDAO.favorite(characterLocal.copy(id = 2))
            characterLocalDAO.favorite(characterLocal.copy(id = 3))
            assertThat(dataSource.favoriteIds()).isEqualTo(listOf<Long>(1, 2, 3))
        }

    @Test
    fun favoriteList_shouldReturnFavoriteList() =
        coroutineTestRule.testDispatcher.runBlockingTest {
            val character1 = characterLocal.copy(id = 1)
            val character2 = characterLocal.copy(id = 2)
            val character3 = characterLocal.copy(id = 3)

            characterLocalDAO.favorite(character1)
            characterLocalDAO.favorite(character2)
            characterLocalDAO.favorite(character3)

            val favorites = dataSource.favoriteList()

            val params = PositionalDataSource.LoadRangeParams(0, 10)
            var characters: List<Character>? = null
            val teste = object : PositionalDataSource.LoadRangeCallback<Character>() {
                override fun onResult(data: List<Character>) {
                    characters = data
                }

            }
            (favorites.create() as PositionalDataSource).loadRange(params, teste)
            assertThat(characters).isEqualTo(
                listOf(
                    character1.toCharacter(),
                    character2.toCharacter(),
                    character3.toCharacter()
                )
            )
        }

    private companion object Mock{
        const val DESCRIPTION = "description"
        const val NAME = "name"
        const val URL = "url"
    }
}
