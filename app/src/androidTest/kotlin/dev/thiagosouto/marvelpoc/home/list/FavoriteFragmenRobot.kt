package dev.thiagosouto.marvelpoc.home.list

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.paging.DataSource
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import dev.thiagosouto.marvelpoc.data.character.CharacterLocalContract
import dev.thiagosouto.marvelpoc.data.model.view.Character
import dev.thiagosouto.marvelpoc.home.FavoritesViewModel
import dev.thiagosouto.marvelpoc.widget.ErrorScreenTestTags
import kotlinx.coroutines.flow.Flow
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
        composeTestRule
            .onNodeWithTag("character-0")
            .assertTextEquals(characterName)
            .assertIsDisplayed()
    }


    private fun checkErrorMessage(message: String) {
        composeTestRule.onNodeWithTag(ErrorScreenTestTags.IMAGE).assertIsDisplayed()
        composeTestRule.onNodeWithTag(ErrorScreenTestTags.MESSAGE).assertIsDisplayed().assertTextEquals(message)
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
    override fun favoriteList(): DataSource.Factory<Int, Character> {

        val data = if (isEmpty) emptyList() else listOf(
            Character(
                30,
                HERO_NAME,
                "http://www.google.com",
                DESCRIPTION,
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

    private companion object Mock {
        const val DESCRIPTION = "description"
    }

    override fun favoritesList(pageSize: Int, maxSize: Int): Flow<PagingData<Character>> {

        return Pager<Int, Character>(
            PagingConfig(
                pageSize = FavoritesViewModel.PAGE_SIZE,
                enablePlaceholders = true,
                maxSize = 200
            )
        ) {
            PagingSourceCharacter(
                if (isEmpty) {
                    emptyList<Character>()
                } else {
                    listOf(
                        Character(
                            30,
                            HERO_NAME,
                            "http://www.google.com",
                            DESCRIPTION,
                            true
                        )
                    )
                }, 20
            )
        }.flow
    }
}

private class PagingSourceCharacter(
    private val characters: List<Character>,
    private val pageSize: Int
) :
    PagingSource<Int, Character>() {

    override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(pageSize)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(pageSize)
        }
    }

    override suspend fun load(params: PagingSource.LoadParams<Int>): PagingSource.LoadResult<Int, Character> {
        val pageNumber = params.key ?: 0

        val prevKey = if (pageNumber > 0) pageNumber - pageSize else null
        val nextKey = if (characters.isNotEmpty()) pageNumber + pageSize else null
        return PagingSource.LoadResult.Page(
            data = characters,
            prevKey = prevKey,
            nextKey = nextKey
        )
    }
}

private const val HERO_NAME = "3-D Test HAHAH"

