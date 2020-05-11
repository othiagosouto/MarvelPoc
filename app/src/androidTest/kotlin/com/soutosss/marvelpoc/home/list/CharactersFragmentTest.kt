package com.soutosss.marvelpoc.home.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test

class CharactersFragmentTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Test
    @Ignore
    fun init_homeMode_shouldPresentLoadingWhileContentIsBeingLoaded() {
        configure {
            withNoFavorites()
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
            checkCharacterHomeNamee()
            loadingIsNotVisible()
            errorMessageNotAvailable()
        }
    }
//
//    @Test
//    fun init_favoriteMode_shouldPresentLoadingWhileContentIsBeingLoaded() {
//        configure {
//            withFavoriteTab()
//        } launch {
//        } check {
//            loadingIsVisible()
//            recyclerViewIsHidden()
//        }
//    }
//
//    @Test
//    fun init_favoriteMode_shouldLoadErrorWithExpectedMessage() {
//        configure {
//            withFavoriteTab()
//            withErrorFavorite()
//        } launch {
//        } check {
//            loadingIsNotVisible()
//            recyclerViewIsHidden()
//            checkErrorFavoriteTab()
//        }
//    }
//
//    @Test
//    fun init_favoriteMode_shouldPresentExpectedCharacters() {
//        configure {
//            withFavoriteTab()
//            withFavoriteCharacters()
//        } launch {
//        } check {
//            characterFavoriteName()
//            loadingIsNotVisible()
//            errorMessageNotAvailable()
//        }
//    }
}