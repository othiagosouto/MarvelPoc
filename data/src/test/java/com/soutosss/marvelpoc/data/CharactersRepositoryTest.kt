package com.soutosss.marvelpoc.data

import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import com.soutosss.marvelpoc.data.local.CharacterHomeDAO
import com.soutosss.marvelpoc.data.model.character.MarvelCharactersResponse
import com.soutosss.marvelpoc.data.model.character.toCharacterHomeList
import com.soutosss.marvelpoc.data.model.view.CharacterHome
import com.soutosss.marvelpoc.data.network.CharactersApi
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

class CharactersRepositoryTest {

    private lateinit var mockApi: CharactersApi
    private lateinit var mockDao: CharacterHomeDAO
    private lateinit var repository: CharactersRepository
    private val item = CharacterHome(1011334, "some name", "some url", true)

    @Before
    fun setup() {
        mockApi = mockk(relaxed = true)
        mockDao = mockk(relaxed = true)
        repository = CharactersRepository(mockApi, mockDao)
    }

    @Test
    fun `fetchAllCharacters should call api to list all characters from marvel endpoint`() =
        runBlockingTest {
            coEvery { mockApi.listCharacters() } returns parseToJson()
            coEvery { mockDao.getAll() } returns emptyList()

            assertThat(repository.fetchAllCharacters()).isEqualTo(parseToJson().toCharacterHomeList())
        }

    @Test
    fun `fetchAllCharacters should update item favorite when there's is storage saved inside room`() =
        runBlockingTest {
            coEvery { mockApi.listCharacters() } returns parseToJson()
            coEvery { mockDao.getAll() } returns listOf(item)

            val expectedResult = parseToJson().toCharacterHomeList()
            expectedResult.first().favorite = true
            assertThat(repository.fetchAllCharacters()).isEqualTo(expectedResult)
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

    private fun parseToJson(): MarvelCharactersResponse {
        return Gson().fromJson(
            "/characters/characters_response_ok.json".toJson(),
            MarvelCharactersResponse::class.java
        )
    }

    private fun String.toJson(): String {
        return this::class.java.javaClass.getResource(this)!!.readText()
    }
}
