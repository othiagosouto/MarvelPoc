package com.soutosss.marvelpoc.home.search

import android.app.SearchManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.soutosss.marvelpoc.R
import com.soutosss.marvelpoc.home.HomeViewModel
import com.soutosss.marvelpoc.home.list.CharactersFragment
import org.koin.androidx.viewmodel.compat.SharedViewModelCompat.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchableActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searchable)
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                val fragment = supportFragmentManager.findFragmentByTag("eita")
                if (fragment == null) {
                    supportFragmentManager.beginTransaction()
                        .add(R.id.content, CharactersFragment.newInstance(query), "eita").commit()
                }
            }
        }

    }
}
