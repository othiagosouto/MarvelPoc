package com.soutosss.marvelpoc.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.soutosss.marvelpoc.R
import com.soutosss.marvelpoc.data.model.view.Character
import kotlinx.android.synthetic.main.fragment_character_details.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CharacterDetailsFragment : Fragment(R.layout.fragment_character_details) {

    private val characterDetailsViewModel: CharacterDetailsViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val character: Character = arguments?.getSerializable(CHARACTER_KEY) as Character

        characterImage.setListeners(favoriteClick = characterDetailsViewModel::favoriteClick)
        characterImage.applyDetailMode()
        characterImage.updateCharacter(character)
        description.text =
            if (character.description.isNotBlank()) character.description else getString(
                R.string.character_details_description_not_available
            )
    }

    companion object {
        private const val CHARACTER_KEY = "CHARACTER_KEY"

        fun createInstance(character: Character): Fragment = CharacterDetailsFragment().also {
            it.arguments = Bundle().apply { putSerializable(CHARACTER_KEY, character) }
        }
    }
}