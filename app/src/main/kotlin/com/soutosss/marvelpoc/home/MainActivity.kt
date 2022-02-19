package com.soutosss.marvelpoc.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.soutosss.marvelpoc.R

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val viewPager: ViewPager2
        get() = findViewById(R.id.viewPager)

    private val toolbar: Toolbar
        get() = findViewById(R.id.toolbar)

    private val tabLayout: TabLayout
        get() = findViewById(R.id.tabLayout)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewPager.adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text =
                if (position == 0) getString(R.string.home_tab_title) else getString(R.string.home_tab_favorites)
        }.attach()

        toolbar.setOnMenuItemClickListener {
            onSearchRequested()
            true
        }
    }

}


