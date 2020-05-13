package com.soutosss.marvelpoc.home.list

import org.junit.Test

class FavoriteFragmentTest {

    @Test
    fun init_shouldLoadContentFromRoom() {
        configureFavorite {
            withNotEmptyList()
        } launch {
        } check {
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