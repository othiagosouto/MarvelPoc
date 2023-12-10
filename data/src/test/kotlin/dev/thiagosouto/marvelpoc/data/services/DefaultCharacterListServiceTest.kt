package dev.thiagosouto.marvelpoc.data.services

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.thiagosouto.marvelpoc.domain.data.remote.CharactersRemoteContract
import dev.thiagosouto.marvelpoc.domain.model.Character
import dev.thiagosouto.marvelpoc.domain.services.CharacterListParams
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

internal class DefaultCharacterListServiceTest {
    @Test
    fun `fetch Given CharacterListParams Then returns Characters`() = runTest {
        val expectedResult = listOf(
            Character(
                id = 10,
                name = "",
                thumbnailUrl = "",
                description = "",
                favorite = false
            )
        )
        val fakeRemote = FakeCharactersRemoteContract(expectedResult)
        val service = DefaultCharacterListService(fakeRemote) { flowOf(listOf(1)) }
        val params = CharacterListParams(pageSize = 10, queryText = "query")

        service.source.test {
            service.fetch(params)

            assertThat(awaitItem()).isEqualTo(expectedResult)
        }
    }

    private class FakeCharactersRemoteContract(private val characters: List<Character>) :
        CharactersRemoteContract {
        override suspend fun listPagingCharacters(
            queryText: String?,
            pageSize: Int
        ): List<Character> {
            return characters
        }
    }
}
