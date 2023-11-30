package dev.thiagosouto.marvelpoc.home.search

import android.app.SearchManager
import android.content.Intent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import dev.thiagosouto.compose.robots.BaseRobot
import dev.thiagosouto.compose.robots.Retryable
import dev.thiagosouto.marvelpoc.data.remote.koin.KtorInitializer
import dev.thiagosouto.marvelpoc.home.list.CharactersViewModel
import dev.thiagosouto.webserver.TestWebServer
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.loadKoinModules
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal fun configure(
    composeRule: ComposeTestRule,
    func: SearchableActivityConfiguration.() -> Unit
) =
    SearchableActivityConfiguration(composeRule).apply(func)

internal class SearchableActivityConfiguration(private val composeRule: ComposeTestRule) : KoinComponent {
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

    private lateinit var intent: Intent

    fun withSearchableIntent() {
        intent = Intent(
            InstrumentationRegistry.getInstrumentation().targetContext,
            SearchableActivity::class.java
        )
        intent.action = Intent.ACTION_SEARCH
        intent.putExtra(SearchManager.QUERY, "ops")
    }

    fun withIntentNotSearchable() {
        intent = Intent(
            InstrumentationRegistry.getInstrumentation().targetContext,
            SearchableActivity::class.java
        )
    }

    infix fun launch(func: SearchableActivityRobot.() -> Unit): SearchableActivityRobot {
        webServer.init(
            mapOf(
                "/characters/home?nameStartsWith=ops&offset=0&limit=20" to TestWebServer.Response("characters/characters_response_ok.json"),
                "/characters/home?offset=0&limit=20" to TestWebServer.Response("characters_response_ok_empty.json")

            ))

        ActivityScenario.launch<SearchableActivity>(intent)
        return SearchableActivityRobot(composeRule).apply(func)
    }
}

internal class SearchableActivityRobot(rule: ComposeTestRule) : BaseRobot(rule) {
    infix fun check(func: SearchableActivityResult.() -> Unit) =
        SearchableActivityResult(rule).apply(func)
}

internal class SearchableActivityResult(rule: ComposeTestRule) : BaseRobot(rule), KoinComponent {
    private val viewModel: CharactersViewModel by inject()

    fun callViewModelWithExpectedContent() = applyComposable {
        val characterName = "3-D Man"
        retry(Retryable.RetryConfig()) {
            waitUntil {
                onAllNodesWithText(characterName)
                    .fetchSemanticsNodes().size == 1
            }
        }

        onNodeWithText(characterName)
            .assertIsDisplayed()
    }


    fun notCallViewModel() = applyComposable {
        onNodeWithText("3-D Man")
            .assertDoesNotExist()
    }
}
