package com.soutosss.marvelpoc.home.list

import android.os.Bundle
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import dev.thiagosouto.data.retrofit.koin.RetrofitInitializer
import com.soutosss.marvelpoc.R
import com.soutosss.marvelpoc.test.waitUntilNotVisible
import com.soutosss.marvelpoc.test.waitUntilVisible
import dev.thiagosouto.webserver.TestWebServer
import org.hamcrest.Matchers.not
import org.koin.core.component.KoinComponent
import org.koin.core.context.loadKoinModules
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal fun configure(
    composeTestRule: ComposeTestRule,
    func: CharactersFragmentConfiguration.() -> Unit
) =
    CharactersFragmentConfiguration(composeTestRule).apply(func)

internal class CharactersFragmentConfiguration(private val composeTestRule: ComposeTestRule) :
    KoinComponent {

    private val webServer = TestWebServer()

    init {
        webServer.start()
        val serverUrl = webServer.url()
        val newtworkModule = module {
            single(
                named(RetrofitInitializer.SERVER_URL)
            ) { serverUrl }
        }
        loadKoinModules(newtworkModule)
    }

    infix fun launch(func: CharactersFragmentRobot.() -> Unit): CharactersFragmentRobot {
        launchFragmentInContainer<CharactersFragment>()
        return CharactersFragmentRobot(composeTestRule, webServer).apply(func)
    }

    infix fun launchSearch(func: CharactersFragmentRobot.() -> Unit): CharactersFragmentRobot {
        launchFragmentInContainer<CharactersFragment>(Bundle().apply {
            putString(
                "QUERY_TEXT_KEY",
                "searchQuery"
            )
        })
        return CharactersFragmentRobot(composeTestRule, webServer).apply(func)
    }

    fun withErrorHome() {
        webServer.initDispatcher()
    }

    fun withHomeCharacters() {
        webServer.mapping =
            mapOf(
                "/characters/home?offset=0&limit=60" to "characters/characters_response_ok.json",
                "/characters/home?offset=1&limit=20" to "characters/characters_response_ok_empty.json"
            )
        webServer.initDispatcher()
    }

    fun withSearchContent() {
        webServer.mapping =
            mapOf(
                "/characters/home?nameStartsWith=searchQuery&offset=0&limit=60" to "characters/characters_response_ok.json",
                "/characters/home?nameStartsWith=searchQuery&offset=1&limit=20" to "characters/characters_response_ok_empty.json"
            )
        webServer.initDispatcher()

    }

    fun withNoFavorites() = Unit

    fun withMockedViewModelLoading() = Unit
}

internal class CharactersFragmentRobot(
    private val composeTestRule: ComposeTestRule,
    private val webServer: TestWebServer
) {
    infix fun check(func: CharactersFragmentResult.() -> Unit) =
        CharactersFragmentResult(composeTestRule, webServer).apply(func)
}

internal class CharactersFragmentResult(
    private val composeTestRule: ComposeTestRule,
    private val testWebServer: TestWebServer
) {

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
        composeTestRule.waitUntil {
            composeTestRule.onAllNodesWithText(characterName)
                .fetchSemanticsNodes().size == 1
        }

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
        composeTestRule.onNodeWithTag("error-image").assertDoesNotExist()
        composeTestRule.onNodeWithTag("error-message").assertDoesNotExist()
    }

    private fun checkErrorMessage(message: String) {
        composeTestRule.onNodeWithTag("error-image").assertIsDisplayed()
        composeTestRule.onNodeWithTag("error-message").assertIsDisplayed().assertTextEquals(message)
    }

    fun stop() {
        testWebServer.stop()
    }
}
