package com.soutosss.marvelpoc.data

import com.google.common.truth.Truth.assertThat
import com.soutosss.marvelpoc.data.local.CharacterHomeDAO
import com.soutosss.marvelpoc.data.model.view.CharacterHome
import com.soutosss.marvelpoc.data.network.CharactersApi
import io.mockk.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

class CharactersRepositoryTest {

    private lateinit var mockApi: CharactersApi
    private lateinit var mockDao: CharacterHomeDAO
    private lateinit var repository: CharactersRepository
    private lateinit var item: CharacterHome

    @Before
    fun setup() {
        mockApi = mockk(relaxed = true)
        mockDao = mockk(relaxed = true)
        repository = CharactersRepository(mockApi, mockDao)
        item = CharacterHome(1011334, "some name", "some url", true)
    }

    @Test
    fun `fetchFavoriteCharacters should call dao to retrieve all favorite items`() =
        runBlockingTest {
            repository.fetchFavoriteCharacters()

            coVerify(exactly = 1) { mockDao.getAll() }
        }

    @Test
    fun `favoriteCharacterHome should call dao to insert favorite item`() = runBlockingTest {
        repository.favoriteCharacterHome(item)

        coVerify(exactly = 1) { mockDao.insertAll(item) }
    }

    @Test
    fun `unFavoriteCharacterHome should call dao to delete item`() = runBlockingTest {
        val result = repository.unFavoriteCharacterHome(item, listOf(item, item.copy(id = 300)))

        coVerify(exactly = 1) { mockDao.delete(item) }
        assertThat(result).isEqualTo(0)
        assertThat(item.favorite).isFalse()
    }

    @Test
    @Ignore
    fun `charactersDataSource should instantiate CharactersDataSource with expeceted parameters`() = runBlockingTest {
        mockkConstructor(CharactersDataSource::class)
        val queryText = "someTExt"
        val scope = this
        val exceptionHandler: (Exception) -> Unit = mockk()
        val loadCallback: () -> Unit = mockk()

        repository.charactersDataSource(queryText, scope, exceptionHandler, loadCallback)
        verify(exactly = 1) {CharactersDataSource(queryText, scope, mockApi, mockDao, exceptionHandler, loadCallback)}
    }

}
