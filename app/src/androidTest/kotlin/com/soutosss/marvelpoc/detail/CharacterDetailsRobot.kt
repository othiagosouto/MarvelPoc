package com.soutosss.marvelpoc.detail

import android.content.Intent
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToIndex
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import com.soutosss.data.retrofit.koin.RetrofitInitializer
import com.soutosss.marvelpoc.data.model.view.Character
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
            )
        webServer.initDispatcher()
    }

    fun withSomeDescription() {
        webServer.mapping =
            mapOf(
                "/characters/details/1011334" to "characters/characters_details_ok.json",
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
    private val rule: ComposeTestRule,
    private val webServer: TestWebServer
) {

    fun characterName() {
        rule.onNodeWithTag("name").assert(hasText("3-D Man"))
    }

    fun description() {
        rule.onNodeWithTag("description").assert(hasText("some description"))
    }

    fun defaultDescription() {
        rule.onNodeWithTag("description")
            .assert(hasText("This character doesn't have any description available :("))
    }

    fun comics() {
        titles().forEachIndexed(::comics)
    }

    private fun comics(index: Int, title: String) {
        rule.onNodeWithTag("character-details-comics").performScrollTo().performScrollToIndex(index)
        rule.onNodeWithTag("comics-title-$index").assert(hasText(title)).assertIsDisplayed()
    }

    fun stop() {
        webServer.stop()
    }
}

private fun titles() = listOf(
    "Avengers: The Initiative (2007) #19",
    "Avengers: The Initiative (2007) #18 (ZOMBIE VARIANT)",
    "Avengers: The Initiative (2007) #18",
    "Avengers: The Initiative (2007) #17",
    "Avengers: The Initiative (2007) #16",
    "Avengers: The Initiative (2007) #15"
)
