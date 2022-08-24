package dev.thiagosouto.marvelpoc.detail

import android.content.Intent
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import dev.thiagosouto.marvelpoc.base.BaseRobot
import dev.thiagosouto.marvelpoc.data.model.view.Character
import dev.thiagosouto.marvelpoc.data.retrofit.koin.RetrofitInitializer
import dev.thiagosouto.webserver.TestWebServer
import org.koin.core.component.KoinComponent
import org.koin.core.context.loadKoinModules
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal fun configureDetail(func: CharacterDetailsConfiguration.() -> Unit) =
    CharacterDetailsConfiguration().apply(func)

internal class CharacterDetailsConfiguration : KoinComponent {
    private lateinit var rule: ComposeTestRule
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

    fun withComposeTestRule(rule: ComposeTestRule) {
        this.rule = rule
    }

    fun withEmptyDescription() {
        webServer.mapping =
            mapOf(
                "/characters/details/1011334" to "characters/characters_details_ok_no_desc.json",
                "/u/prod/marvel/i/mg/c/e0/535fecbbb9784.jpeg" to "characters/images/image.jpeg",
                "/u/prod/marvel/i/mg/d/03/58dd080719806.jpeg" to "characters/images/image.jpeg",
                "/u/prod/marvel/i/mg/6/20/58dd057d304d1.jpeg" to "characters/images/image.jpeg",
                "/u/prod/marvel/i/mg/1/10/4e94a23255996.jpeg" to "characters/images/image.jpeg",
                "/u/prod/marvel/i/mg/b/a0/58dd03dc2ec00.jpeg" to "characters/images/image.jpeg"
            )
        webServer.initDispatcher()
    }

    fun withSomeDescription() {
        webServer.mapping =
            mapOf(
                "/characters/details/1011334" to "characters/characters_details_ok.json",
                "/u/prod/marvel/i/mg/c/e0/535fecbbb9784.jpeg" to "characters/images/image.jpeg",
                "/u/prod/marvel/i/mg/d/03/58dd080719806.jpeg" to "characters/images/image.jpeg",
                "/u/prod/marvel/i/mg/6/20/58dd057d304d1.jpeg" to "characters/images/image.jpeg",
                "/u/prod/marvel/i/mg/1/10/4e94a23255996.jpeg" to "characters/images/image.jpeg",
                "/u/prod/marvel/i/mg/b/a0/58dd03dc2ec00.jpeg" to "characters/images/image.jpeg"
            )
        webServer.initDispatcher()
    }

    infix fun launch(func: CharacterDetailsRobot.() -> Unit): CharacterDetailsRobot {

        val intent = Intent(
            InstrumentationRegistry.getInstrumentation().targetContext,
            CharacterDetailsActivity::class.java
        )
        val character = Character.EMPTY.copy(id = 1011334)
        intent.putExtra("CHARACTER_KEY", character)
        ActivityScenario.launch<CharacterDetailsActivity>(intent)
        return CharacterDetailsRobot(rule, webServer).apply(func)
    }
}

internal class CharacterDetailsRobot(
    private val rule: ComposeTestRule,
    private val webServer: TestWebServer
) {
    infix fun check(func: CharacterDetailsResult.() -> Unit) =
        CharacterDetailsResult(rule, webServer).apply(func)
}

internal class CharacterDetailsResult(
    rule: ComposeTestRule,
    private val webServer: TestWebServer
) : BaseRobot(rule) {

    fun characterName() {
        rule.waitUntil {
            rule.onAllNodesWithTag(NAME).fetchSemanticsNodes().size == 1
        }
        rule.onNodeWithTag(NAME).performScrollTo()
        rule.onNodeWithTag(NAME).assert(hasText("3-D Man"))
    }

    fun description() {
        rule.onNodeWithTag(DESCRIPTION).waitUntilVisible()
        rule.onNodeWithTag(DESCRIPTION).performScrollTo()
        rule.onNodeWithTag(DESCRIPTION).assert(hasText("some description"))
    }

    fun defaultDescription() {
        rule.onNodeWithTag(DESCRIPTION).waitUntilVisible()
        rule.onNodeWithTag(DESCRIPTION).performScrollTo()
        rule.onNodeWithTag(DESCRIPTION)
            .assert(hasText("This character doesn't have any description available :("))
    }

    fun comics() {
        rule.swipeUpDetailsComics()
        titles().forEachIndexed(::comics)
    }

    private fun ComposeTestRule.swipeUpDetailsComics() {
        onNodeWithTag("character-details-comics")
            .performTouchInput { this.swipeUp() }
            .waitUntilVisible()
    }

    private fun comics(index: Int, title: String) = retry {
        scrollTo(index)
        onNodeWithTag("comics-title-$index")
            .assertTextEquals(title)
            .assertIsDisplayed()
    }

    private fun ComposeTestRule.scrollTo(index: Int) {
        onNodeWithTag("character-details-comics")
            .performScrollToIndex(index)
    }

    fun stop() {
        webServer.stop()
    }

    private companion object {
        const val DESCRIPTION = "description"
        const val NAME = "name"
    }
}

private fun titles() = listOf(
    "Avengers: The Initiative (2007) #19",
    "Avengers: The Initiative (2007) #18 (ZOMBIE VARIANT)",
    "Avengers: The Initiative (2007) #18",
    "Avengers: The Initiative (2007) #17",
    "Avengers: The Initiative (20071)",
    "Avengers: The Initiative (20072)",
    "Avengers: The Initiative (20073)",
    "Avengers: The Initiative (20074)",
    "Avengers: The Initiative (20075)",
    "Avengers: The Initiative (20076)",
    "Avengers: The Initiative (20077)",
    "Avengers: The Initiative (20078)",
    "Avengers: The Initiative (20079)",
    "Avengers: The Initiative (20080)",
    "Avengers: The Initiative (20081)"
)
