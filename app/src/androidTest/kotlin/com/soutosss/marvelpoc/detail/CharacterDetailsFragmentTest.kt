package com.soutosss.marvelpoc.detail

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CharacterDetailsFragmentTest {

    @Test
    fun details_content() {
        configureDetail {
            withEmptyDescriptionAndFavorite()
        } launch {

        } check {
            checkCharacterName()
            checkEmptyCharacterDescription()
        }
    }
}
