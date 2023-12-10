package dev.thiagosouto.marvelpoc.data.room

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.thiagosouto.marvelpoc.data.room.ext.toCharacter
import dev.thiagosouto.marvelpoc.domain.model.Character
import kotlinx.coroutines.test.runTest
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
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        characterLocalDAO = appDatabase.charactersLocalDao()
        dataSource = CharacterLocalRoomDataSource(characterLocalDAO)
    }

    @Test
    fun favorite_shouldInsertTheExpectedItemToFavorite() = runTest {
        dataSource.favorite(character)

        dataSource.favoriteIds().test {
            assertThat(awaitItem()).contains(30L)
        }
    }

    @Test
    fun unFavorite_shouldUnFavoriteAndReturns() = runTest {
        dataSource.favorite(character)
        dataSource.unFavorite(character)

        dataSource.favoriteIds().test {
            assertThat(awaitItem()).doesNotContain(30L)
        }
    }

    @Test
    fun favoriteIds_shouldReturnAllIdsOfFavoriteItems() = runTest {
        characterLocalDAO.favorite(characterLocal.copy(id = 1))
        characterLocalDAO.favorite(characterLocal.copy(id = 2))
        characterLocalDAO.favorite(characterLocal.copy(id = 3))

        dataSource.favoriteIds().test {

            assertThat(awaitItem()).isEqualTo(listOf<Long>(1, 2, 3))
        }
    }

    @Test
    fun favoriteList_shouldReturnFavoriteList() = runTest {
        val character1 = characterLocal.copy(id = 1)
        val character2 = characterLocal.copy(id = 2)
        val character3 = characterLocal.copy(id = 3)

        characterLocalDAO.favorite(character1)
        characterLocalDAO.favorite(character2)
        characterLocalDAO.favorite(character3)

        dataSource.favoritesList().test {

            assertThat(awaitItem()).isEqualTo(
                listOf(
                    character1.toCharacter(),
                    character2.toCharacter(),
                    character3.toCharacter()
                )
            )
        }
    }

    private companion object Mock {
        const val DESCRIPTION = "description"
        const val NAME = "name"
        const val URL = "url"
    }
}
