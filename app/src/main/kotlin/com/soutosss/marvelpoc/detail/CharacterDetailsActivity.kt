package com.soutosss.marvelpoc.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.soutosss.marvelpoc.R
import com.soutosss.marvelpoc.data.model.view.Character

class CharacterDetailsActivity : AppCompatActivity(R.layout.activity_character_details) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_details)

        supportFragmentManager.beginTransaction().replace(
            R.id.content,
            CharacterDetailsFragment.createInstance(intent.getSerializableExtra("CHARACTER") as Character)
        ).commitNow()
    }
}
