package com.soutosss.marvelpoc.home.list

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
            loadingIsVisible()
            recyclerViewIsHidden()
        }
    }

    @Test
    fun init_homeMode_shouldLoadErrorWithExpectedMessage() {
        configure(composeTestRule) {
            withErrorHome()
            withNoFavorites()
        } launch {
        } check {
            loadingIsNotVisible()
            recyclerViewIsHidden()
            checkErrorHomeTab()
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
        }
    }
}
