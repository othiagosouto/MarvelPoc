package com.soutosss.marvelpoc.data

import com.google.common.truth.Truth.assertThat
import com.soutosss.marvelpoc.data.model.view.Character
import com.soutosss.marvelpoc.data.network.CharactersApi
import com.soutosss.marvelpoc.data.room_source.CharacterLocal
import com.soutosss.marvelpoc.shared.contracts.character.CharacterLocalContract
import io.mockk.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

class CharactersRepositoryTest {

    private lateinit var mockApi: CharactersApi
    private lateinit var mockDao: CharacterLocalContract<CharacterLocal>
    private lateinit var repository: CharactersRepository
    private lateinit var item: Character

    @Before
    fun setup() {
        mockApi = mockk(relaxed = true)
        mockDao = mockk(relaxed = true)
        repository = CharactersRepository(mockApi, mockDao)
        item = Character(1011334, "some name", "some url", "description", true)
    }

    @Test
    fun `fetchFavoriteCharacters should call dao to retrieve all favorite items`() =
        runBlockingTest {
            repository.fetchFavoriteCharacters()

            coVerify(exactly = 1) { mockDao.favoriteList() }
        }

    @Test
    fun `favoriteCharacter should call dao to insert favorite item`() = runBlockingTest {
        repository.favoriteCharacter(item)

        coVerify(exactly = 1) { mockDao.favorite(item.toCharacterLocal()) }
    }

    @Test
    fun `unFavoriteCharacter should call dao to delete item`() = runBlockingTest {
        val unFavoriteItem = item.copy(id = 300L)
        val parameter = unFavoriteItem.copy(favorite = false)

        coEvery { mockDao.unFavorite(parameter.toCharacterLocal()) } returns parameter.id
        val result = repository.unFavoriteCharacter(parameter, listOf(item, unFavoriteItem))

        assertThat(result).isEqualTo(1)
        assertThat(unFavoriteItem.favorite).isFalse()
        coVerify (exactly = 1){ mockDao.unFavorite(parameter.toCharacterLocal()) }
    }

    @Test
    @Ignore
    fun `charactersDataSource should instantiate CharactersDataSource with expeceted parameters`() =
        runBlockingTest {
            mockkConstructor(CharactersDataSource::class)
            val queryText = "someTExt"
            val scope = this
            val exceptionHandler: (Exception) -> Unit = mockk()
            val loadCallback: () -> Unit = mockk()

            repository.charactersDataSource(queryText, scope, exceptionHandler, loadCallback)
            verify(exactly = 1) {
                CharactersDataSource(
                    queryText,
                    scope,
                    mockApi,
                    mockDao,
                    exceptionHandler,
                    loadCallback
                )
            }
        }

    private fun Character.toCharacterLocal() =
        CharacterLocal(
            this.id.toLong(),
            this.name,
            this.thumbnailUrl,
            this.description,
            this.favorite
        )

    private fun CharacterLocal.toCharacter() =
        Character(
            this.id,
            this.name,
            this.thumbnailUrl,
            this.description,
            this.favorite
        )

}
