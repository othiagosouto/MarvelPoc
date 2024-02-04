package dev.thiagosouto.marvelpoc.detail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import dev.thiagosouto.marvelpoc.features.character.details.CharacterDetailsScreen
import dev.thiagosouto.marvelpoc.features.character.details.CharacterDetailsViewModel
import dev.thiagosouto.marvelpoc.features.character.details.Intent
import org.koin.androidx.viewmodel.ext.android.viewModel

internal class CharacterDetailsActivity : ComponentActivity() {

    private val characterDetailsViewModel: CharacterDetailsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            val characterId: Long = intent.extras!!.getLong(CHARACTER_KEY)
            characterDetailsViewModel.process(Intent.OpenScreen(characterId))
        }

        lifecycleScope.launchWhenCreated {
            characterDetailsViewModel.state.collect {
                setContent {
                    CharacterDetailsScreen(characterDetailsViewModel::process, it)
                }
            }
        }

    }

    companion object {
        const val CHARACTER_KEY = "CHARACTER_KEY"
        const val TAG = "CharacterDetailsFragment"
    }
}

