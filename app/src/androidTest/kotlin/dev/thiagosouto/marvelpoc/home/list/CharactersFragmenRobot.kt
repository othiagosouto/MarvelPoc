package dev.thiagosouto.marvelpoc.home.list

import android.os.Bundle
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.fragment.app.testing.launchFragmentInContainer
import dev.thiagosouto.compose.robots.BaseRobot
import dev.thiagosouto.compose.robots.Retryable
import dev.thiagosouto.marvelpoc.data.remote.koin.KtorInitializer
import dev.thiagosouto.marvelpoc.widget.ErrorScreenTestTags
import dev.thiagosouto.webserver.TestWebServer
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
                named(KtorInitializer.SERVER_URL)
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
        webServer.init(mapOf(
            "/characters/home?offset=0&limit=20" to TestWebServer.Response("characters/characters_response_ok.json", 400)
        ))
    }

    fun withHomeCharacters() {
        webServer.init(
            mapOf(
                "/characters/home?offset=0&limit=20" to TestWebServer.Response("characters/characters_response_ok.json"),
                "/characters/home?offset=1&limit=20" to TestWebServer.Response("characters/characters_response_ok_empty.json"),
                "/u/prod/marvel/i/mg/c/e0/535fecbbb9784.jpg" to TestWebServer.Response("characters/images/image.jpeg")
            )
        )
    }

    fun withSearchContent() {
        webServer.init(
            mapOf(
                "/characters/home?nameStartsWith=searchQuery&offset=0&limit=20" to TestWebServer.Response(
                    "characters/characters_response_ok.json"
                ),
                "/characters/home?nameStartsWith=searchQuery&offset=1&limit=20" to TestWebServer.Response(
                    "characters/characters_response_ok_empty.json"
                ),
                "/u/prod/marvel/i/mg/c/e0/535fecbbb9784.jpg" to TestWebServer.Response("characters/images/image.jpeg")
            )
        )

    }

    fun withNoFavorites() = Unit
}

internal class CharactersFragmentRobot(
    private val composeTestRule: ComposeTestRule,
    private val webServer: TestWebServer
) {
    infix fun check(func: CharactersFragmentResult.() -> Unit) =
        CharactersFragmentResult(composeTestRule, webServer).apply(func)
}

internal class CharactersFragmentResult(
    rule: ComposeTestRule,
    private val testWebServer: TestWebServer
) : BaseRobot(rule) {

    fun recyclerViewIsHidden() = applyComposable {
        onNodeWithTag(CharactersListTestTags.LIST).assertDoesNotExist()
    }

    fun recyclerViewVisible() = applyComposable {
        waitUntilNodeWithTagVisible(CharactersListTestTags.LIST)
        onNodeWithTag(CharactersListTestTags.LIST)
            .assertIsDisplayed()
    }

    private fun waitUntilNodeWithTagVisible(tag: String) {
        retryWithDelay(func = {
            waitUntil {
                onAllNodesWithTag(tag).fetchSemanticsNodes().size == 1
            }
        })
    }

    private fun waitUntilNodeWithTagNotVisible(tag: String) = applyComposable {
        waitUntil {
            onAllNodesWithTag(tag).fetchSemanticsNodes().isEmpty()
        }
    }

    fun checkCharacterName() {
        checkCharacterName("3-D Man")
    }

    private fun checkCharacterName(characterName: String) = applyComposable {
        retry(Retryable.RetryConfig()) {
            waitUntil {
                onAllNodesWithText(characterName)
                    .fetchSemanticsNodes().size == 1
            }
        }

        onNodeWithText(characterName)
            .assertIsDisplayed()
    }

    fun checkErrorHomeTab() {
        checkErrorMessage("Looks like thanos didn't like you")
    }

    fun errorMessageNotAvailable() = applyComposable {
        assertTagDoesNotExist(ErrorScreenTestTags.IMAGE)
        assertTagDoesNotExist(ErrorScreenTestTags.MESSAGE)
    }

    private fun checkErrorMessage(message: String) = applyComposable {
        retry(Retryable.RetryConfig()) {
            waitUntil {
                onAllNodesWithTag(ErrorScreenTestTags.IMAGE)
                    .fetchSemanticsNodes().size == 1
            }
        }

        onNodeWithTag(ErrorScreenTestTags.IMAGE, useUnmergedTree = true)
            .assertIsDisplayed()
        onNodeWithTag(ErrorScreenTestTags.MESSAGE, useUnmergedTree = true)
            .assertIsDisplayed()
            .assertTextEquals(message)
    }

    fun stop() {
        testWebServer.stop()
    }
}
