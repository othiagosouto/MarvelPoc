package com.soutosss.marvelpoc.home.list

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithText
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.soutosss.marvelpoc.R
import com.soutosss.marvelpoc.data.CharactersRepository
import com.soutosss.marvelpoc.data.character.CharacterLocalContract
import com.soutosss.marvelpoc.data.model.view.Character
import com.soutosss.marvelpoc.home.HomeViewModel
import com.soutosss.marvelpoc.test.waitUntilNotVisible
import com.soutosss.marvelpoc.test.waitUntilVisible
import io.mockk.every
import io.mockk.mockk
import org.hamcrest.Matchers.not
import org.koin.core.component.KoinComponent
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

fun configureFavorite(composeTestRule: ComposeTestRule, func: FavoriteFragmentConfiguration.() -> Unit) =
    FavoriteFragmentConfiguration(composeTestRule).apply(func)

class FavoriteFragmentConfiguration(private val composeTestRule: ComposeTestRule) : KoinComponent {
    private val localSource: CharacterLocalContract<Character> = mockk(relaxed = true)
    private val repository: CharactersRepository = CharactersRepository(localSource, mockk(), mockk())
    private val viewModel: HomeViewModel = HomeViewModel(repository)

    infix fun launch(func: FavoriteFragmentRobot.() -> Unit): FavoriteFragmentRobot {
        loadKoinModules(
            module(override = true) {
                single { localSource }
                single { repository }
                single { viewModel }
            })

        launchFragmentInContainer<FavoriteFragment>()
        return FavoriteFragmentRobot(composeTestRule).apply(func)
    }


    fun withNotEmptyList() {
        every { localSource.favoriteList() } returns FakeHomeDataSource(
            listOf(
                Character(
                    30,
                    "3-D Test HAHAH",
                    "http://www.google.com",
                    "description",
                    true
                )
            )
        )
    }

    fun withNoFavorites() {
        every { localSource.favoriteList() } returns FakeHomeDataSource(emptyList())
    }
}

class FavoriteFragmentRobot(private val composeTestRule: ComposeTestRule) {
    infix fun check(func: FavoriteFragmentResult.() -> Unit) =
        FavoriteFragmentResult(composeTestRule).apply(func)

}

class FavoriteFragmentResult(private val composeTestRule: ComposeTestRule) {

    fun characterFavoriteName() {
        checkCharacterName("3-D Test HAHAH")
    }

    private fun checkCharacterName(characterName: String) {
        composeTestRule
            .onNodeWithText(characterName)
            .assertIsDisplayed()
    }


    private fun checkErrorMessage(message: String) {
        onView(withId(R.id.message)).check(matches(isDisplayed()))
        onView(withId(R.id.erroIcon)).check(matches(isDisplayed()))
        onView(withId(R.id.message)).check(matches(withText(message)))
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
