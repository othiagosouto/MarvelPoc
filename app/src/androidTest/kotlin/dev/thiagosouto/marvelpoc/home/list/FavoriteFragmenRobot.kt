package dev.thiagosouto.marvelpoc.home.list

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.paging.DataSource
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import dev.thiagosouto.marvelpoc.R
import dev.thiagosouto.marvelpoc.data.character.CharacterLocalContract
import dev.thiagosouto.marvelpoc.data.model.view.Character
import dev.thiagosouto.marvelpoc.test.waitUntilNotVisible
import dev.thiagosouto.marvelpoc.test.waitUntilVisible
import org.hamcrest.Matchers.not
import org.koin.core.component.KoinComponent
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

internal fun configureFavorite(
    composeTestRule: ComposeTestRule,
    func: FavoriteFragmentConfiguration.() -> Unit
) =
    FavoriteFragmentConfiguration(composeTestRule).apply(func)

internal class FavoriteFragmentConfiguration(private val composeTestRule: ComposeTestRule) :
    KoinComponent {
    private lateinit var localSource: CharacterLocalContract<Character>

    infix fun launch(func: FavoriteFragmentRobot.() -> Unit): FavoriteFragmentRobot {
        loadKoinModules(
            module {
                single { localSource }
            })

        launchFragmentInContainer<FavoriteFragment>()
        return FavoriteFragmentRobot(composeTestRule).apply(func)
    }


    fun withNotEmptyList() {
        localSource = FakeCharacterLocalContract(isEmpty = false)
    }

    fun withNoFavorites() {
        localSource = FakeCharacterLocalContract(isEmpty = true)
    }
}

internal class FavoriteFragmentRobot(private val composeTestRule: ComposeTestRule) {
    infix fun check(func: FavoriteFragmentResult.() -> Unit) =
        FavoriteFragmentResult(composeTestRule).apply(func)

}

internal class FavoriteFragmentResult(private val composeTestRule: ComposeTestRule) {

    fun characterFavoriteName() {
        checkCharacterName("3-D Test HAHAH")
    }

    private fun checkCharacterName(characterName: String) {
        composeTestRule
            .onNodeWithText(characterName)
            .assertIsDisplayed()
    }


    private fun checkErrorMessage(message: String) {
        composeTestRule.onNodeWithTag("error-image").assertIsDisplayed()
        composeTestRule.onNodeWithTag("error-message").assertIsDisplayed().assertTextEquals(message)
    }

    fun recyclerViewIsHidden() {
        onView(withId(R.id.recycler)).waitUntilNotVisible().check(matches(not(isDisplayed())))
    }

    fun recyclerViewVisible() {
        onView(withId(R.id.recycler)).waitUntilVisible().check(matches(isDisplayed()))
    }

    fun checkFavoritesEmptyMessage() =
        checkErrorMessage("You don't have favorite marvel character :(")
}

private class FakeCharacterLocalContract(private val isEmpty: Boolean) :
    CharacterLocalContract<Character> {
    override fun favoriteList(): DataSource.Factory<Int, Character> {

        val data = if (isEmpty) emptyList() else listOf(
            Character(
                30,
                "3-D Test HAHAH",
                "http://www.google.com",
                "description",
                true
            )
        )
        return FakeHomeDataSource(data)

    }

    override suspend fun favorite(item: Character): Long = 30L

    override suspend fun favoriteIds(): List<Long> {
        return if (isEmpty) emptyList() else listOf(30)
    }

    override suspend fun unFavorite(item: Character): Long = 30L
}
