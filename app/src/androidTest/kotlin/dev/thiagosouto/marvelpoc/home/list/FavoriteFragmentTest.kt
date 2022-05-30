package dev.thiagosouto.marvelpoc.home.list

import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class FavoriteFragmentTest {
    @get:Rule
    val composeTestRule = createEmptyComposeRule()

    @Test
    fun init_shouldLoadContentFromRoom() {
        configureFavorite(composeTestRule) {
            withNotEmptyList()
        } launch {
        } check {
            recyclerViewVisible()
            characterFavoriteName()
        }
    }

    @Test
    fun init_shouldLoadEmptyMessageWhenThereIsNoFavorites() {
        configureFavorite(composeTestRule) {
            withNoFavorites()
        } launch {
        } check {
            recyclerViewIsHidden()
            checkFavoritesEmptyMessage()
        }
    }
}
