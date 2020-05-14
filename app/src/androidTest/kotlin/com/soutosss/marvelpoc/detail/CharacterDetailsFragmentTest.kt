package com.soutosss.marvelpoc.detail

import org.junit.Test

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
            checkFavoriteMethodFiredToUnFavorite()
        }
    }
}