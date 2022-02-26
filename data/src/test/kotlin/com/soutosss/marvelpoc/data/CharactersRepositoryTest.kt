package com.soutosss.marvelpoc.data

import com.google.common.truth.Truth.assertThat
import com.soutosss.marvelpoc.data.character.CharacterDetailsRemoteContract
import com.soutosss.marvelpoc.data.character.CharacterLocalContract
import com.soutosss.marvelpoc.data.character.CharacterRemoteContract
import com.soutosss.marvelpoc.data.model.view.Character
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import java.lang.Exception

class CharactersRepositoryTest {

    private lateinit var remoteSourceMock: CharacterRemoteContract<Character>
    private lateinit var localSourceMock: CharacterLocalContract<Character>
    private lateinit var characterDetailsRemoteContract: CharacterDetailsRemoteContract<CharacterDetails>
    private lateinit var repository: CharactersRepository
    private lateinit var item: Character

    @Before
    fun setup() {
        remoteSourceMock = mockk(relaxed = true)
        localSourceMock = mockk(relaxed = true)
        characterDetailsRemoteContract = mockk()
        repository =
            CharactersRepository(localSourceMock, remoteSourceMock, characterDetailsRemoteContract)
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
        repository.favoriteCharacter(item)

        coVerify(exactly = 1) { localSourceMock.favorite(item) }
    }

    @Test
    fun `unFavoriteCharacter should call dao to delete item`() = runBlockingTest {
        val unFavoriteItem = item.copy(id = 300L)
        val parameter = unFavoriteItem.copy(favorite = false)

        coEvery { localSourceMock.unFavorite(parameter) } returns parameter.id
        val result = repository.unFavoriteCharacter(parameter, listOf(item, unFavoriteItem))

        assertThat(result).isEqualTo(1)
        assertThat(unFavoriteItem.favorite).isFalse()
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


}
