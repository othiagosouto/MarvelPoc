package dev.thiagosouto.marvelpoc.home.list

import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class CharactersFragmentTest {
    @get:Rule
    val composeTestRule = createEmptyComposeRule()

    @Test
    fun init_homeMode_shouldPresentLoadingWhileContentIsBeingLoaded() {
        configure(composeTestRule) {
            withMockedViewModelLoading()
        } launch {
        } check {
            recyclerViewIsHidden()
            stop()
        }
    }

    @Test
    fun init_homeMode_shouldLoadErrorWithExpectedMessage() {
        configure(composeTestRule) {
            withErrorHome()
            withNoFavorites()
        } launch {
        } check {
            recyclerViewIsHidden()
            checkErrorHomeTab()
            stop()
        }
    }

    @Test
    fun init_homeMode_shouldPresentExpectedCharacters() {
        configure(composeTestRule) {
            withNoFavorites()
            withHomeCharacters()
        } launch {
        } check {
            recyclerViewVisible()
            loadingIsNotVisible()
            checkCharacterName()
            errorMessageNotAvailable()
            stop()
        }
    }

    @Test
    fun init_searchMode_shouldPresentExpectedCharacters() {
        configure(composeTestRule) {
            withNoFavorites()
            withSearchContent()
        } launchSearch  {
        } check {
            loadingIsNotVisible()
            checkCharacterName()
            errorMessageNotAvailable()
            stop()
        }
    }
}
