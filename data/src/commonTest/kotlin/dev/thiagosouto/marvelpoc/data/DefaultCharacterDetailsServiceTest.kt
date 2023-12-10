package dev.thiagosouto.marvelpoc.data

import dev.thiagosouto.marvelpoc.domain.data.remote.CharacterDetailsRemoteContract
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class DefaultCharacterDetailsServiceTest {
    private val characterDetailsRemoteContract: CharacterDetailsRemoteContract<CharacterDetails> =
        FakeDetails()
    private val repository: DefaultCharacterDetailsService = DefaultCharacterDetailsService(
        characterDetailsRemoteContract
    )

    @Test
    fun `fetch returns details`() = runTest {

        assertEquals(
            expected = CharacterDetails(
                id = 123L,
                name = CHARACTER_NAME,
                description = CHARACTER_DESCRIPTION,
                imageUrl = "",
                comics = emptyList()
            ),
            actual = repository.fetch("123")
        )
    }

    private class FakeDetails : CharacterDetailsRemoteContract<CharacterDetails> {
        override suspend fun fetchCharacterDetails(characterId: String): CharacterDetails {
            return CharacterDetails(
                id = characterId.toLong(),
                name = CHARACTER_NAME,
                description = CHARACTER_DESCRIPTION,
                imageUrl = "",
                comics = emptyList()
            )
        }
    }

    private companion object {
        const val CHARACTER_NAME = "name"
        const val CHARACTER_DESCRIPTION = "description"
    }
}
