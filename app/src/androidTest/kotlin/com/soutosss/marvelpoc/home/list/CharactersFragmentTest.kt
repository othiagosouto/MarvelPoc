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
}