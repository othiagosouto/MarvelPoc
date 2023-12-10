package dev.thiagosouto.marvelpoc.data

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.thiagosouto.marvelpoc.data.character.CharacterLocalContract
import dev.thiagosouto.marvelpoc.domain.data.remote.CharacterDetailsRemoteContract
import dev.thiagosouto.marvelpoc.domain.model.Character
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

internal class CharactersRepositoryImplTest {
    private val item = Character(1011334, "some name", "some url", "description", true)

    private val localSourceMock = FakeLocal(mutableListOf())
    private val characterDetailsRemoteContract: CharacterDetailsRemoteContract<CharacterDetails> =
        FakeDetails()
    private val repository: CharactersRepositoryImpl = CharactersRepositoryImpl(
        localSourceMock,
        characterDetailsRemoteContract
    )

    @Test
    fun `favorites should call dao to retrieve all favorite items`() = runTest {
        localSourceMock.favorite(item)

        repository.favorites().test {
            assertThat(awaitItem()).isEqualTo(listOf(item))
            awaitComplete()
        }
    }

    @Test
    fun `favoriteCharacter should call dao to insert favorite item`() = runTest {
        repository.favorite(item)

        localSourceMock.favoriteIds().test {

            assertThat(awaitItem()).isEqualTo(listOf(item.id))
            awaitComplete()
        }
    }

    @Test
    fun `unFavoriteCharacter should call dao to delete item`() = runTest {
        val parameter = item.copy(favorite = true)
        repository.favorite(parameter)

        repository.unFavorite(parameter)

        localSourceMock.favoriteIds().test {
            assertThat(awaitItem()).isEmpty()
            awaitComplete()
        }

    }

    @Test
    fun `fetchFavoriteIds return a list of favorite ids`() = runTest {
        val items = listOf(item.copy(id = 1), item.copy(id = 2), item.copy(id = 3))

        items.forEach {
            localSourceMock.favorite(it)
        }

        repository.fetchFavoriteIds().test {
            assertThat(awaitItem()).isEqualTo(items.map { it.id })
            awaitComplete()
        }
    }

    @Test
    fun `fetch returns details`() = runTest {
            assertThat(
                repository.fetch("123")
            )
                .isEqualTo(
                    CharacterDetails(
                        id = 123L,
                        name = "name",
                        description = "description",
                        imageUrl = "",
                        comics = emptyList()
                    )
                )
    }

    private class FakeLocal(val characters: MutableList<Character>) :
        CharacterLocalContract<Character> {
        override fun favoritesList(): Flow<List<Character>> = flowOf(characters)

        override fun favoriteIds(): Flow<List<Long>> = flowOf(characters.map { it.id })

        override suspend fun unFavorite(item: Character): Long {
            characters.remove(item)
            return item.id
        }

        override suspend fun favorite(item: Character): Long {
            characters.add(item)
            return item.id
        }
    }


    private class FakeDetails : CharacterDetailsRemoteContract<CharacterDetails> {
        override suspend fun fetchCharacterDetails(characterId: String): CharacterDetails {
            return CharacterDetails(
                id = characterId.toLong(),
                name = "name",
                description = "description",
                imageUrl = "",
                comics = emptyList()
            )
        }
    }
}
