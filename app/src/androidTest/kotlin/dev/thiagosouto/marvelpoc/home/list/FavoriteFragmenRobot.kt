package dev.thiagosouto.marvelpoc.home.list

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.fragment.app.testing.launchFragmentInContainer
import dev.thiagosouto.marvelpoc.data.character.CharacterLocalContract
import dev.thiagosouto.marvelpoc.domain.model.Character
import dev.thiagosouto.marvelpoc.widget.ErrorScreenTestTags
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
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
        checkCharacterName(HERO_NAME)
    }

    private fun checkCharacterName(characterName: String) {
        composeTestRule.waitUntil {
            composeTestRule.onAllNodesWithText(characterName)
                .fetchSemanticsNodes().size == 1
        }
    }


    private fun checkErrorMessage(message: String) {
        composeTestRule.onNodeWithTag(ErrorScreenTestTags.IMAGE).assertIsDisplayed()
        composeTestRule.onNodeWithTag(ErrorScreenTestTags.MESSAGE).assertIsDisplayed()
            .assertTextEquals(message)
    }

    fun recyclerViewIsHidden() {
        composeTestRule.onNodeWithTag(CharactersListTestTags.LIST).assertDoesNotExist()
    }

    fun recyclerViewVisible() {
        composeTestRule.onNodeWithTag(CharactersListTestTags.LIST).assertIsDisplayed()
    }

    fun checkFavoritesEmptyMessage() =
        checkErrorMessage("You don't have favorite marvel character :(")
}

private class FakeCharacterLocalContract(private val isEmpty: Boolean) :
    CharacterLocalContract<Character> {
    override fun favoritesList(): Flow<List<Character>> {

        val data = if (isEmpty) emptyList() else listOf(
            Character(
                30,
                HERO_NAME,
                "http://www.google.com",
                DESCRIPTION,
                true
            )
        )
        return flowOf(data)
    }

    override suspend fun favorite(item: Character): Long = 30L

    override fun favoriteIds(): Flow<List<Long>> {
        return if (isEmpty) emptyFlow() else flowOf(listOf(30))
    }

    override suspend fun unFavorite(item: Character): Long = 30L

    private companion object Mock {
        const val DESCRIPTION = "description"
    }
}


private const val HERO_NAME = "3-D Test HAHAH"

