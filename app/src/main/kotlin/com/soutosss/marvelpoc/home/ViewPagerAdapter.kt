package com.soutosss.marvelpoc.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.soutosss.marvelpoc.home.list.CharactersFragment
import java.lang.IllegalArgumentException

class ViewPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fm, lifecycle) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            HOME_FRAGMENT -> CharactersFragment.createHomeFragment()
            FAVORITE_FRAGMENT -> CharactersFragment.createFavoriteFragment()
            else -> throw IllegalArgumentException("invalid position")
        }
    }

    companion object {
        private const val HOME_FRAGMENT = 0
        private const val FAVORITE_FRAGMENT = 1
    }

}