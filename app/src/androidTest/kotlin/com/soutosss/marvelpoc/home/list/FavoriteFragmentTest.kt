package com.soutosss.marvelpoc.home.list

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavoriteFragmentTest {

    @Test
    fun init_shouldLoadContentFromRoom() {
        configureFavorite {
            withNotEmptyList()
        } launch {
        } check {
            recyclerViewVisible()
            characterFavoriteName()
        }
    }

    @Test
    fun init_shouldLoadEmptyMessageWhenThereIsNoFavorites() {
        configureFavorite {
            withNoFavorites()
        } launch {
        } check {
            recyclerViewIsHidden()
            checkFavoritesEmptyMessage()
        }
    }
}