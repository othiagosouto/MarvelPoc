package dev.thiagosouto.marvelpoc.data

import androidx.paging.DataSource
import androidx.paging.PagingData
import com.google.common.truth.Truth.assertThat
import dev.thiagosouto.marvelpoc.data.character.CharacterLocalContract
import dev.thiagosouto.marvelpoc.domain.data.remote.CharacterDetailsRemoteContract
import dev.thiagosouto.marvelpoc.domain.model.Character
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class CharactersRepositoryImplTest {

    private val localSourceMock = FakeLocal()
    private val characterDetailsRemoteContract: CharacterDetailsRemoteContract<CharacterDetails> =
        FakeDetails()
    private val repository: CharactersRepositoryImpl = CharactersRepositoryImpl(
        localSourceMock,
        characterDetailsRemoteContract
    )
    private lateinit var item: Character

    @Before
    fun setup() {
        item = Character(1011334, "some name", "some url", "description", true)
    }

    @After
    fun tearDown() {
        localSourceMock.favoriteIds.clear()
    }

    @Test(expected = FakeLocal.FavoriteListCalledException::class)
    fun `fetchFavoriteCharacters should call dao to retrieve all favorite items`() = runTest {
        repository.fetchFavoriteCharacters()

    }

    @Test
    fun `favoriteCharacter should call dao to insert favorite item`() = runTest {
        repository.favorite(item)

        assertThat(localSourceMock.favoriteIds).isNotEqualTo(listOf(item.id))
    }

    @Test
    fun `unFavoriteCharacter should call dao to delete item`() = runTest {
        val parameter = item.copy(favorite = false)
        repository.favorite(item)

        repository.unFavorite(parameter)

        assertThat(localSourceMock.favoriteIds).isEmpty()
    }

    @Test
    fun `fetchFavoriteIds return a list of favorite ids`() = runTest {
        val ids: List<Long> = listOf(1, 2, 3)
        localSourceMock.favoriteIds.addAll(ids)

        assertThat(repository.fetchFavoriteIds()).isEqualTo(ids)
    }

    class FakeLocal : CharacterLocalContract<Character> {

        val favoriteIds = mutableSetOf<Long>()
        override fun favoritesList(pageSize: Int, maxSize: Int): Flow<PagingData<Character>> {
            TODO("Not yet implemented")
        }

        override fun favoriteList(): DataSource.Factory<Int, Character> {
            throw FavoriteListCalledException()
        }

        override fun favoriteIds(): Flow<List<Long>> = flowOf(favoriteIds.toList())

        override suspend fun unFavorite(item: Character): Long {
            favoriteIds.remove(item.id)
            return item.id
        }

        override suspend fun favorite(item: Character): Long {
            favoriteIds.add(item.id)
            return item.id
        }

        class FavoriteListCalledException : Exception()
    }

    class FakeDetails : CharacterDetailsRemoteContract<CharacterDetails> {
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
