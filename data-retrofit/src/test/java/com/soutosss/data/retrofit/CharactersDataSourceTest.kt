package com.soutosss.data.retrofit

import androidx.paging.PositionalDataSource
import com.google.common.truth.Truth
import com.google.gson.Gson
import com.soutosss.data.retrofit.character.MarvelCharactersResponse
import com.soutosss.data.retrofit.ext.toCharacter
import com.soutosss.marvelpoc.data.model.view.Character
import com.soutosss.marvelpoc.shared.EmptyDataException
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

internal class CharactersDataSourceTest {

    private val charactersList = parseToJson()
    private lateinit var api: CharactersBFFApi
    private val exception = Exception()
    private lateinit var errorCallback: (Exception) -> Unit
    private lateinit var characterList: List<Character>
    private lateinit var characterFavoredList: List<Character>
    private lateinit var provideFavoriteIds: suspend () -> List<Long>

    @Before
    fun setup() {
        api = mockk()
        val character = charactersList.data.results.first().toCharacter()
        characterList = listOf(character)
        characterFavoredList = listOf(character.copy(favorite = true))
        errorCallback = mockk(relaxed = true)
        provideFavoriteIds = mockk()
        coEvery { provideFavoriteIds.invoke() } returns emptyList()
    }

    @Test
    fun `loadInitial should call callback with expected list and position`() = runBlockingTest {
        val loadFinishMock: () -> Unit = mockk(relaxed = true)
        val source = CharactersDataSource(
            null,
            this,
            api,
            {},
            loadFinishMock,
            provideFavoriteIds
        )
        coEvery { api.listCharacters(null, 0, 5) } returns parseToJson()

        val callback: PositionalDataSource.LoadInitialCallback<Character> =
            mockk(relaxed = true)

        source.loadInitial(PositionalDataSource.LoadInitialParams(0, 5, 5, false), callback)

        verify { callback.onResult(characterList, 0) }
        verify { loadFinishMock() }
    }

    @Test
    fun `loadInitial should call callback with expected transformed list with favorite and position`() =
        runBlockingTest {
            val loadFinishMock: () -> Unit = mockk(relaxed = true)

            coEvery { provideFavoriteIds.invoke() } returns listOf(1011334L)
            val source = CharactersDataSource(
                null,
                this,
                api,
                {},
                loadFinishMock,
                provideFavoriteIds
            )
            coEvery { api.listCharacters(null, 0, 5) } returns parseToJson()

            val callback: PositionalDataSource.LoadInitialCallback<Character> =
                mockk(relaxed = true)

            source.loadInitial(PositionalDataSource.LoadInitialParams(0, 5, 5, false), callback)

            verify { callback.onResult(characterFavoredList, 0) }
            verify { loadFinishMock() }
        }

    @Test
    fun `loadInitial should call error callback when an error occurs`() = runBlockingTest {
        val source = CharactersDataSource(
            null,
            this,
            api,
            errorCallback,
            mockk(),
            provideFavoriteIds
        )
        coEvery { api.listCharacters(null, 0, 5) } throws exception

        source.loadInitial(PositionalDataSource.LoadInitialParams(0, 5, 5, false), mockk())

        verify { errorCallback(exception) }
    }

    @Test
    fun `loadRange should call callback with expected list and position`() = runBlockingTest {
        val source = CharactersDataSource(
            null,
            this,
            api,
            {},
            mockk(),
            provideFavoriteIds
        )
        coEvery { api.listCharacters(null, 0, 5) } returns parseToJson()

        val callback: PositionalDataSource.LoadRangeCallback<Character> = mockk(relaxed = true)

        source.loadRange(PositionalDataSource.LoadRangeParams(0, 5), callback)

        verify { callback.onResult(characterList) }
    }

    @Test
    fun `loadRange should call callback with expected transformed list with favorite and position`() =
        runBlockingTest {
            coEvery { provideFavoriteIds() } returns listOf(1011334)
            val source = CharactersDataSource(
                null,
                this,
                api,
                {},
                mockk(),
                provideFavoriteIds
            )
            coEvery { api.listCharacters(null, 0, 5) } returns parseToJson()

            val callback: PositionalDataSource.LoadRangeCallback<Character> = mockk(relaxed = true)

            source.loadRange(PositionalDataSource.LoadRangeParams(0, 5), callback)

            verify { callback.onResult(characterFavoredList) }
        }

    @Test
    fun `loadRange should call error callback when an error occurs`() = runBlockingTest {
        val source = CharactersDataSource(
            null,
            this,
            api,
            errorCallback,
            mockk(),
            provideFavoriteIds
        )
        coEvery { api.listCharacters(null, 0, 5) } throws exception
        val callback: PositionalDataSource.LoadRangeCallback<Character> = mockk(relaxed = true)

        source.loadRange(PositionalDataSource.LoadRangeParams(0, 5), callback)

        verify { errorCallback(exception) }
    }

    @Test
    fun `loadInitial should call error callback with EmptyDataException when there is no items`() =
        runBlockingTest {
            val slot = slot<Exception>()
            val response = parseToJson().copy(data = parseToJson().data.copy(results = emptyList()))
            val source = CharactersDataSource(
                null,
                this,
                api,
                errorCallback,
                mockk(),
                provideFavoriteIds
            )
            every { errorCallback(any()) } returns Unit
            coEvery { api.listCharacters(null, 0, 5) } returns response

            every { errorCallback(capture(slot)) } returns Unit

            source.loadInitial(PositionalDataSource.LoadInitialParams(0, 5, 5, false), mockk())

            Truth.assertThat(slot.captured).isInstanceOf(EmptyDataException::class.java)
        }

    private fun parseToJson(): MarvelCharactersResponse {
        return Gson().fromJson(
            ClassLoader.getSystemResource("characters/characters_response_ok.json").readText(),
            MarvelCharactersResponse::class.java
        )
    }
}
