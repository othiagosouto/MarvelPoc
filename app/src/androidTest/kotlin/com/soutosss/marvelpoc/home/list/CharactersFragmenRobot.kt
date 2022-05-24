package com.soutosss.marvelpoc.home.list

import android.os.Bundle
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.google.gson.Gson
import com.soutosss.data.data_retrofit.CharactersBFFApi
import com.soutosss.data.data_retrofit.RetrofitCharacterRemote
import com.soutosss.data.data_retrofit.character.MarvelCharactersResponse
import com.soutosss.marvelpoc.R
import com.soutosss.marvelpoc.data.CharactersRepository
import com.soutosss.marvelpoc.data.room_source.CharacterLocalDAO
import com.soutosss.marvelpoc.data.room_source.CharacterLocalRoomDataSource
import com.soutosss.marvelpoc.home.HomeViewModel
import com.soutosss.marvelpoc.test.waitUntilNotVisible
import com.soutosss.marvelpoc.test.waitUntilVisible
import io.mockk.coEvery
import io.mockk.mockk
import org.hamcrest.Matchers.not
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

fun configure(composeTestRule: ComposeTestRule, func: CharactersFragmentConfiguration.() -> Unit) =
    CharactersFragmentConfiguration(composeTestRule).apply(func)

class CharactersFragmentConfiguration(private val composeTestRule: ComposeTestRule) :
    KoinComponent {
    private val api: CharactersBFFApi = mockk(relaxed = true)
    private val characterLocalDao: CharacterLocalDAO = mockk(relaxed = true)
    private val repository: CharactersRepository
    private var homeViewModel: HomeViewModel

    init {
        loadKoinModules(
            module(override = true) {
                single { api }
                single { characterLocalDao }
                single { CharacterLocalRoomDataSource(characterLocalDao) }
                single { RetrofitCharacterRemote(api) }
            })
        repository = CharactersRepository(get(), get(), mockk())
        homeViewModel = HomeViewModel(repository)
    }

    infix fun launch(func: CharactersFragmentRobot.() -> Unit): CharactersFragmentRobot {
        loadKoinModules(
            module(override = true) {
                single { api }
                single { repository }
                single { characterLocalDao }
                single { homeViewModel }
            })

        launchFragmentInContainer<CharactersFragment>()
        return CharactersFragmentRobot(composeTestRule).apply(func)
    }

    infix fun launchSearch(func: CharactersFragmentRobot.() -> Unit): CharactersFragmentRobot {
        loadKoinModules(
            module(override = true) {
                single { characterLocalDao }
                single { api }
                single { repository }
                single { homeViewModel }
            })

        launchFragmentInContainer<CharactersFragment>(Bundle().apply {
            putString(
                "QUERY_TEXT_KEY",
                "searchQuery"
            )
        })
        return CharactersFragmentRobot(composeTestRule).apply(func)
    }

    fun withErrorHome() {
        coEvery { api.listCharacters(null, any(), any()) } throws Exception()
    }

    fun withHomeCharacters() {
        val response = parseToJson()
        val data = parseToJson().data.copy(results = emptyList())
        coEvery { api.listCharacters(null, any(), any()) } returns response andThen response.copy(
            data = data
        )
    }

    fun withSearchContent() {
        val response = parseToJson()
        val data = parseToJson().data.copy(results = emptyList())
        coEvery { api.listCharacters("searchQuery", any(), any()) } returns parseToJson() andThen response.copy(data = data)
    }

    fun withNoFavorites() {
        coEvery { characterLocalDao.favoriteIds() } returns emptyList()
    }

    fun withMockedViewModelLoading() {
        homeViewModel = mockk(relaxed = true)
    }

}

class CharactersFragmentRobot(private val composeTestRule: ComposeTestRule) {
    infix fun check(func: CharactersFragmentResult.() -> Unit) =
        CharactersFragmentResult(composeTestRule).apply(func)
}

class CharactersFragmentResult(private val composeTestRule: ComposeTestRule) {

    fun recyclerViewIsHidden() {
        onView(withId(R.id.recycler)).waitUntilNotVisible().check(matches(not(isDisplayed())))
    }

    fun recyclerViewVisible() {
        onView(withId(R.id.recycler)).waitUntilVisible().check(matches(isDisplayed()))
    }

    fun checkCharacterName() {
        checkCharacterName("3-D Man")
    }

    private fun checkCharacterName(characterName: String) {
        composeTestRule
            .onNodeWithText(characterName)
            .assertIsDisplayed()
    }

    fun loadingIsVisible() {
        onView(withId(R.id.progress)).waitUntilVisible().check(matches(isDisplayed()))
    }

    fun loadingIsNotVisible() {
        onView(withId(R.id.progress)).waitUntilNotVisible().check(matches(not(isDisplayed())))
    }

    fun checkErrorHomeTab() {
        checkErrorMessage("Looks like thanos didn't like you")
    }

    fun errorMessageNotAvailable() {
        onView(withId(R.id.message)).waitUntilNotVisible(10_000).check(matches(not(isDisplayed())))
        onView(withId(R.id.erroIcon)).waitUntilNotVisible(10_000).check(matches(not(isDisplayed())))
    }

    private fun checkErrorMessage(message: String) {
        onView(withId(R.id.message)).waitUntilVisible(10_000).check(matches(isDisplayed()))
        onView(withId(R.id.erroIcon)).waitUntilVisible(10_000).check(matches(isDisplayed()))
        onView(withId(R.id.message)).waitUntilVisible(10_000).check(matches(withText(message)))
    }

}

private fun parseToJson(): MarvelCharactersResponse {
    return Gson().fromJson(
        "characters/characters_response_ok.json".toJson(),
        MarvelCharactersResponse::class.java
    )
}

private fun String.toJson(): String {
    return CharactersFragmentConfiguration::class.java.classLoader!!.getResource(this)!!.readText()
}
