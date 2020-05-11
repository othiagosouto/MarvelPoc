package com.soutosss.marvelpoc.data

import androidx.paging.PositionalDataSource
import com.google.gson.Gson
import com.soutosss.marvelpoc.data.local.CharacterHomeDAO
import com.soutosss.marvelpoc.data.model.character.MarvelCharactersResponse
import com.soutosss.marvelpoc.data.model.character.toCharacterHomeList
import com.soutosss.marvelpoc.data.model.view.CharacterHome
import com.soutosss.marvelpoc.data.network.CharactersApi
import io.mockk.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import java.lang.Exception

class CharactersDataSourceTest {

    private val charactersList = parseToJson().toCharacterHomeList()
    private lateinit var api: CharactersApi
    private lateinit var dao: CharacterHomeDAO
    private val exception = Exception()
    private lateinit var errorCallback: (Exception) -> Unit

    @Before
    fun setup() {
        api = mockk()
        dao = mockk()
        errorCallback = mockk(relaxed = true)
    }

    @Test
    fun `loadInitial should call callback with expected list and position`() = runBlockingTest {
        val loadFinishMock: () -> Unit = mockk(relaxed = true)
        val source = CharactersDataSource(null, this, api, dao, {}, loadFinishMock)
        coEvery { api.listCharacters(null, 0, 5) } returns parseToJson()
        coEvery { dao.favoriteIds() } returns emptyList()

        val callback: PositionalDataSource.LoadInitialCallback<CharacterHome> =
            mockk(relaxed = true)

        source.loadInitial(PositionalDataSource.LoadInitialParams(0, 5, 5, false), callback)

        verify { callback.onResult(charactersList, 0) }
        verify { loadFinishMock() }
    }

    @Test
    fun `loadInitial should call error callback when an error occurs`() = runBlockingTest {
        val source = CharactersDataSource(null, this, api, dao, errorCallback, mockk())
        coEvery { api.listCharacters(null, 0, 5) } throws exception

        source.loadInitial(PositionalDataSource.LoadInitialParams(0, 5, 5, false), mockk())

        verify { errorCallback(exception) }
    }

    @Test
    fun `loadRange should call callback with expected list and position`() = runBlockingTest {
        val source = CharactersDataSource(null, this, api, dao, {}, mockk())
        coEvery { api.listCharacters(null, 0, 5) } returns parseToJson()
        coEvery { dao.favoriteIds() } returns emptyList()

        val callback: PositionalDataSource.LoadRangeCallback<CharacterHome> = mockk(relaxed = true)

        source.loadRange(PositionalDataSource.LoadRangeParams(0, 5), callback)

        verify { callback.onResult(charactersList) }
    }

    @Test
    fun `loadRange should call error callback when an error occurs`() = runBlockingTest {
        val source = CharactersDataSource(null, this, api, dao, errorCallback, mockk())
        coEvery { api.listCharacters(null, 0, 5) } throws exception
        coEvery { dao.favoriteIds() } returns emptyList()

        val callback: PositionalDataSource.LoadRangeCallback<CharacterHome> = mockk(relaxed = true)

        source.loadRange(PositionalDataSource.LoadRangeParams(0, 5), callback)

        verify { errorCallback(exception) }
    }

    private fun parseToJson(): MarvelCharactersResponse {
        return Gson().fromJson(
            "/characters/characters_response_ok.json".toJson(),
            MarvelCharactersResponse::class.java
        )
    }

    private fun String.toJson(): String {
        return this::class.java.javaClass.getResource(this)!!.readText()
    }
}