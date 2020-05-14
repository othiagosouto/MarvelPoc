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
}