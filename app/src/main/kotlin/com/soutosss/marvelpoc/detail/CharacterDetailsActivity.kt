package com.soutosss.marvelpoc.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.soutosss.marvelpoc.R
import com.soutosss.marvelpoc.data.model.view.Character
import kotlinx.android.synthetic.main.activity_main.*

class CharacterDetailsActivity : AppCompatActivity(R.layout.activity_character_details) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_details)

        var fragment = supportFragmentManager.findFragmentByTag(TAG)
        if (fragment == null) {
            val character = intent.getSerializableExtra(CHARACTER_KEY) as Character
            fragment = CharacterDetailsFragment.createInstance(character)
            supportFragmentManager.beginTransaction().replace(R.id.content, fragment).commitNow()
        }

        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    companion object {
        const val CHARACTER_KEY = "CHARACTER_KEY"
        const val TAG = "CharacterDetailsFragment"
    }
}
