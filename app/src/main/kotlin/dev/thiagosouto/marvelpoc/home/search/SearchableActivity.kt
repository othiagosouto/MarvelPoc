package dev.thiagosouto.marvelpoc.home.search

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.thiagosouto.marvelpoc.R
import dev.thiagosouto.marvelpoc.home.list.CharactersFragment

internal class SearchableActivity : AppCompatActivity() {
    
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
