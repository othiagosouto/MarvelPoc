package com.soutosss.marvelpoc.data

import com.soutosss.marvelpoc.data.network.CharactersApi
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class CharactersRepositoryTest {

    @Test
    fun fetchAllCharacters_shouldCallListCharactersFromApi() = runBlockingTest {
        val mockApi = mockk<CharactersApi>(relaxed = true)
        val repository = CharactersRepository(mockApi)
        repository.fetchAllCharacters()

        coVerify(exactly = 1) { mockApi.listCharacters() }
    }

}
