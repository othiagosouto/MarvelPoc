package com.soutosss.marvelpoc.home.list

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CharactersFragmentTest {

    @Test
    fun init_homeMode_shouldPresentLoadingWhileContentIsBeingLoaded() {
        configure {
            withMockedViewModelLoading()
        } launch {
        } check {
            loadingIsVisible()
            recyclerViewIsHidden()
        }
    }

    @Test
    fun init_homeMode_shouldLoadErrorWithExpectedMessage() {
        configure {
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
        configure {
            withNoFavorites()
            withHomeCharacters()
        } launch {
        } check {
            checkCharacterName()
            loadingIsNotVisible()
            errorMessageNotAvailable()
        }
    }

    @Test
    fun init_searchMode_shouldPresentExpectedCharacters() {
        configure {
            withNoFavorites()
            withSearchContent()
        } launchSearch  {
        } check {
            Thread.sleep(30_000)
            checkCharacterName()
            loadingIsNotVisible()
            errorMessageNotAvailable()
        }
    }

}