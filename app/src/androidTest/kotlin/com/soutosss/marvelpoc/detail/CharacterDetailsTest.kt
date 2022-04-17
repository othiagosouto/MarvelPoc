package com.soutosss.marvelpoc.detail

import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CharacterDetailsTest {

    @get:Rule
    val composeTestRule = createEmptyComposeRule()

    @Test
    fun details_content() {
        configureDetail {
            withComposeTestRule(composeTestRule)
            withSomeDescription()
        } launch {

        } check {
            characterName()
            description()
        }
    }

    @Test
    fun details_withDefaultDescription() {
        configureDetail {
            withComposeTestRule(composeTestRule)
            withEmptyDescription()
        } launch {

        } check {
            characterName()
            defaultDescription()
        }
    }
}
