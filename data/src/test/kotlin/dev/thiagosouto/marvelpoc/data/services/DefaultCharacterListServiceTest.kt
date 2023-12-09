package dev.thiagosouto.marvelpoc.data.services

import com.google.common.truth.Truth
import com.google.common.truth.Truth.*
import dev.thiagosouto.marvelpoc.domain.data.remote.CharactersRemoteContract
import dev.thiagosouto.marvelpoc.domain.model.Character
import dev.thiagosouto.marvelpoc.domain.services.CharacterListParams
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
        val service = DefaultCharacterListService(fakeRemote)
        val params = CharacterListParams(pageSize = 10, queryText = "query", provideFavoriteIds = {
            emptyList()
        })

        assertThat(service.fetch(params)).isEqualTo(expectedResult)
    }

    private class FakeCharactersRemoteContract(private val characters: List<Character>) :
        CharactersRemoteContract {
        override suspend fun listPagingCharacters(
            queryText: String?,
            pageSize: Int,
            provideFavoriteIds: suspend () -> List<Long>
        ): List<Character> {
            return characters
        }
    }
}
