package dev.thiagosouto.marvelpoc.data

import com.google.common.truth.Truth.assertThat
import dev.thiagosouto.marvelpoc.data.character.CharacterDetailsRemoteContract
import dev.thiagosouto.marvelpoc.data.character.CharacterLocalContract
import dev.thiagosouto.marvelpoc.data.character.CharacterRemoteContract
import dev.thiagosouto.marvelpoc.data.model.view.Character
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import java.lang.Exception

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
            CharactersRepositoryImpl(localSourceMock, remoteSourceMock, characterDetailsRemoteContract)
        item = Character(1011334, "some name", "some url", "description", true)
    }

    @Test
    fun `fetchFavoriteCharacters should call dao to retrieve all favorite items`() =
        runBlockingTest {
            repository.fetchFavoriteCharacters()

            coVerify(exactly = 1) { localSourceMock.favoriteList() }
        }

    @Test
    fun `favoriteCharacter should call dao to insert favorite item`() = runBlockingTest {
        repository.favorite(item)

        coVerify(exactly = 1) { localSourceMock.favorite(item) }
    }

    @Test
    fun `unFavoriteCharacter should call dao to delete item`() = runBlockingTest {
        val parameter = item.copy(favorite = false)
        coEvery { localSourceMock.unFavorite(parameter) } returns parameter.id

        repository.unFavorite(parameter)

        coVerify(exactly = 1) { localSourceMock.unFavorite(parameter) }
    }

    @Test
    fun `charactersDataSource should call listCharacters with expected parameters `() =
        runBlockingTest {
            val exceptionHandler: (Exception) -> Unit = mockk(relaxed = true)
            val sucessCallback: () -> Unit = mockk(relaxed = true)

            repository.charactersDataSource(null, this, exceptionHandler, sucessCallback)
            coVerify {
                remoteSourceMock.listCharacters(
                    this@runBlockingTest,
                    null,
                    exceptionHandler,
                    sucessCallback,
                    localSourceMock::favoriteIds
                )
            }
        }

    @Test
    fun `fetchFavoriteIds return a list of favorite ids`() = runBlockingTest {
        val ids: List<Long> = listOf(1, 2, 3)

        coEvery { localSourceMock.favoriteIds() } returns ids

        assertThat(repository.fetchFavoriteIds()).isEqualTo(ids)
    }
}
