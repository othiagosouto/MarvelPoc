package com.soutosss.marvelpoc.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.soutosss.marvelpoc.R
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val homeViewModel: HomeViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lifecycle.addObserver(homeViewModel)

        viewPager.adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
    }
}
