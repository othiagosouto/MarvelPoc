package dev.thiagosouto.marvelpoc.data

import com.google.common.truth.Truth.assertThat
import dev.thiagosouto.marvelpoc.data.character.CharacterDetailsRemoteContract
import dev.thiagosouto.marvelpoc.data.character.CharacterLocalContract
import dev.thiagosouto.marvelpoc.data.character.CharacterRemoteContract
import dev.thiagosouto.marvelpoc.data.model.view.Character
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.lang.Exception

@OptIn(ExperimentalCoroutinesApi::class)
internal class CharactersRepositoryImplTest {

    private lateinit var remoteSourceMock: CharacterRemoteContract<Character>
    private lateinit var localSourceMock: CharacterLocalContract<Character>
    private lateinit var characterDetailsRemoteContract: CharacterDetailsRemoteContract<CharacterDetails>
    private lateinit var repository: CharactersRepositoryImpl
    private lateinit var item: Character

    @Before
    fun setup() {
        remoteSourceMock = mockk(relaxed = true)
        localSourceMock = mockk(relaxed = true)
        characterDetailsRemoteContract = mockk()
        repository =
            CharactersRepositoryImpl(
                localSourceMock,
                remoteSourceMock,
                characterDetailsRemoteContract
            )
        item = Character(1011334, "some name", "some url", "description", true)
    }

    @Test
    fun `fetchFavoriteCharacters should call dao to retrieve all favorite items`() = runTest {
        repository.fetchFavoriteCharacters()

        coVerify(exactly = 1) { localSourceMock.favoriteList() }
    }

    @Test
    fun `favoriteCharacter should call dao to insert favorite item`() = runTest {
        repository.favorite(item)

        coVerify(exactly = 1) { localSourceMock.favorite(item) }
    }

    @Test
    fun `unFavoriteCharacter should call dao to delete item`() = runTest {
        val parameter = item.copy(favorite = false)
        coEvery { localSourceMock.unFavorite(parameter) } returns parameter.id

        repository.unFavorite(parameter)

        coVerify(exactly = 1) { localSourceMock.unFavorite(parameter) }
    }

    @Test
    fun `charactersDataSource should call listCharacters with expected parameters `() {
        repository.charactersPagingDataSource(null, 10)

        coVerify {
            remoteSourceMock.listPagingCharacters(
                null,
                10,
                localSourceMock::favoriteIds
            )
        }
    }

    @Test
    fun `fetchFavoriteIds return a list of favorite ids`() = runTest {
        val ids: List<Long> = listOf(1, 2, 3)
        coEvery { localSourceMock.favoriteIds() } returns ids

        assertThat(repository.fetchFavoriteIds()).isEqualTo(ids)
    }
}
