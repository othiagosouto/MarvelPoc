package com.soutosss.marvelpoc.data

import com.google.common.truth.Truth.assertThat
import com.soutosss.marvelpoc.data.local.CharacterDAO
import com.soutosss.marvelpoc.data.model.view.Character
import com.soutosss.marvelpoc.data.network.CharactersApi
import io.mockk.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

class CharactersRepositoryTest {

    private lateinit var mockApi: CharactersApi
    private lateinit var mockDao: CharacterDAO
    private lateinit var repository: CharactersRepository
    private lateinit var item: Character

    @Before
    fun setup() {
        mockApi = mockk(relaxed = true)
        mockDao = mockk(relaxed = true)
        repository = CharactersRepository(mockApi, mockDao)
        item = Character(1011334, "some name", "some url", "description", true)
    }

    @Test
    fun `fetchFavoriteCharacters should call dao to retrieve all favorite items`() =
        runBlockingTest {
            repository.fetchFavoriteCharacters()

            coVerify(exactly = 1) { mockDao.getAll() }
        }

    @Test
    fun `favoriteCharacter should call dao to insert favorite item`() = runBlockingTest {
        repository.favoriteCharacter(item)

        coVerify(exactly = 1) { mockDao.insertAll(item) }
    }

    @Test
    fun `unFavoriteCharacter should call dao to delete item`() = runBlockingTest {
        val result = repository.unFavoriteCharacter(item, listOf(item, item.copy(id = 300)))

        coVerify(exactly = 1) { mockDao.delete(item) }
        assertThat(result).isEqualTo(0)
        assertThat(item.favorite).isFalse()
    }

    @Test
    @Ignore
    fun `charactersDataSource should instantiate CharactersDataSource with expeceted parameters`() =
        runBlockingTest {
            mockkConstructor(CharactersDataSource::class)
            val queryText = "someTExt"
            val scope = this
            val exceptionHandler: (Exception) -> Unit = mockk()
            val loadCallback: () -> Unit = mockk()

            repository.charactersDataSource(queryText, scope, exceptionHandler, loadCallback)
            verify(exactly = 1) {
                CharactersDataSource(
                    queryText,
                    scope,
                    mockApi,
                    mockDao,
                    exceptionHandler,
                    loadCallback
                )
            }
        }

}
