package dev.thiagosouto.marvelpoc.data.repositories

import app.cash.turbine.test
import dev.thiagosouto.marvelpoc.data.character.CharacterLocalContract
import dev.thiagosouto.marvelpoc.domain.model.Character
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class DefaultFavoritesRepositoryTest {
    private val localSourceMock = FakeLocal(mutableListOf())
    private val item = Character(1011334, "some name", "some url", "description", true)
    private val repository: FavoritesRepository<Character> =
        DefaultFavoritesRepository(localSourceMock)

    @Test
    fun `favorites should call dao to retrieve all favorite items`() = runTest {
        localSourceMock.favorite(item)

        repository.favorites().test {
            assertEquals(expected = listOf(item), actual = awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `favoriteCharacter should call dao to insert favorite item`() = runTest {
        repository.favorite(item)

        localSourceMock.favoriteIds().test {
            assertEquals(expected = listOf(item.id), actual = awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `unFavoriteCharacter should call dao to delete item`() = runTest {
        val parameter = item.copy(favorite = true)
        repository.favorite(parameter)

        repository.unFavorite(parameter)

        localSourceMock.favoriteIds().test {
            assertEquals(expected = emptyList(), actual = awaitItem())
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
            assertEquals(expected = items.map { it.id }, actual = awaitItem())
            awaitComplete()
        }
    }

    private class FakeLocal(val characters: MutableList<Character>) :
        CharacterLocalContract<Character> {
        override fun favoritesList(): Flow<List<Character>> = flowOf(characters)

        override fun favoriteIds(): Flow<List<Long>> = flowOf(characters.map { it.id })

        override suspend fun unFavorite(item: Character): Unit {
            characters.remove(item)
        }

        override suspend fun favorite(item: Character): Unit {
            characters.add(item)
        }
    }
}
