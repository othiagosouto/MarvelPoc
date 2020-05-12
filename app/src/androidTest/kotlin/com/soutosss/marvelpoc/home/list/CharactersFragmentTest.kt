package com.soutosss.marvelpoc.home.list

import org.junit.Test

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
            checkCharacterHomeNamee()
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