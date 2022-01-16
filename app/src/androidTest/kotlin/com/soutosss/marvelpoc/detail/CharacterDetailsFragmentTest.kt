package com.soutosss.marvelpoc.detail

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CharacterDetailsFragmentTest {

    @Test
    fun details_withFavoriteCharacter() {
        configureDetail {
            withEmptyDescriptionAndFavorite()
        } launch {

        } check {
            checkCharacterName()
            favoriteChecked()
            checkEmptyCharacterDescription()
        }
    }

    @Test
    fun details_withNotFavoriteCharacter() {
        configureDetail {
            withSomeDescriptionNotFavorite()
        } launch {

        } check {
            checkCharacterName()
            favoriteNotChecked()
            checkExpectedCharacterDescription()
        }
    }

    @Test
    fun favorite_whenClicked_shouldFireViewModelMethodToFavorite() {
        configureDetail {
            withSomeDescriptionNotFavorite()
        } launch {
            clickOnFavorite()
        } check {
            checkFavoriteMethodFiredToFavorite()
        }
    }

    @Test
    fun favorite_whenClicked_shouldFireViewModelMethodToUnFavoriteFavorite() {
        configureDetail {
            withEmptyDescriptionAndFavorite()
        } launch {
            clickOnFavorite()
        } check {
            unFavorite()
        }
    }
}
