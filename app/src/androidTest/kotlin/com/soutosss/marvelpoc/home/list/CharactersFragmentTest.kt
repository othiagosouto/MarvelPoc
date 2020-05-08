package com.soutosss.marvelpoc.home.list

import org.junit.Test

class CharactersFragmentTest {

    @Test
    fun init_shouldPresentLoadingWhileContentIsBeingLoaded() {
        configure {
            withHomeTab()
        } launch {
        } check {
            loadingIsVisible()
            recyclerViewIsHidden()
        }
    }

    @Test
    fun init_favoriteMode_shouldLoadEmptyViewWithExpectedFavoriteMessage() {
        configure {
            withFavoriteTab()
            withEmptyFavoriteResult()
        } launch {
        } check {
            loadingIsNotVisible()
            recyclerViewIsHidden()
            checkEmptyFavoriteTab()
        }
    }

    @Test
    fun init_homeMode_shouldLoadEmptyViewWithExpectedHomeeMessage() {
        configure {
            withHomeTab()
            withEmptyHomeResult()
        } launch {
        } check {
            loadingIsNotVisible()
            recyclerViewIsHidden()
            checkEmptyHomeTab()
        }
    }

    @Test
    fun init_homeMode_shouldPresentExpectedCharacters() {
        configure {
            withHomeTab()
            withHomeCharacters()
        } launch {
        } check {
            checkCharacterHomeNamee()
            loadingIsNotVisible()
            errorMessageNotAvailable()
        }
    }

    @Test
    fun init_favorite_shouldPresentExpectedCharacters() {
        configure {
            withFavoriteTab()
            withFavoriteCharacters()
        } launch {
        } check {
            characterFavoriteName()
            loadingIsNotVisible()
            errorMessageNotAvailable()
        }
    }
}