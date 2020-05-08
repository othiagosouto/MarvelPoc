package com.soutosss.marvelpoc.data

import com.soutosss.marvelpoc.data.local.CharacterHomeDAO
import com.soutosss.marvelpoc.data.model.view.CharacterHome
import com.soutosss.marvelpoc.data.network.CharactersApi
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

class CharactersRepositoryTest {

    private lateinit var mockApi: CharactersApi
    private lateinit var mockDao: CharacterHomeDAO
    private lateinit var repository: CharactersRepository
    private val item = CharacterHome(33, "some name", "some url", true)

    @Before
    fun setup() {
        mockApi = mockk(relaxed = true)
        mockDao = mockk(relaxed = true)
        repository = CharactersRepository(mockApi, mockDao)
    }

    @Test
    fun `fetchAllCharacters should call api to list all characters from marvel endpoint`() =
        runBlockingTest {
            repository.fetchAllCharacters()

            coVerify(exactly = 1) { mockApi.listCharacters() }
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
        repository.unFavoriteCharacterHome(item)

        coVerify(exactly = 1) { mockDao.delete(item) }
    }
}
