package com.soutosss.marvelpoc.detail

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.soutosss.marvelpoc.R
import com.soutosss.marvelpoc.data.model.view.Character
import com.soutosss.marvelpoc.widget.CharacterView
import org.koin.androidx.viewmodel.ext.android.viewModel

class CharacterDetailsFragment : Fragment(R.layout.fragment_character_details) {

    private val characterImage: CharacterView
        get() = requireView().findViewById(R.id.characterImage)

    private val description: TextView
        get() = requireView().findViewById(R.id.description)

    private val characterDetailsViewModel: CharacterDetailsViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val character: Character = arguments?.getSerializable(CHARACTER_KEY) as Character

        characterDetailsViewModel.loadFavoriteData(character.id.toString())
        characterImage.setListeners(favoriteClick = characterDetailsViewModel::favoriteClick)

        characterDetailsViewModel.characterDetails.observe(viewLifecycleOwner){ details ->
            characterImage.applyDetailMode()
            characterImage.updateCharacter(details)
            description.text =
                if (details.description.isNotBlank()) details.description else getString(
                    R.string.character_details_description_not_available
                )
        }
    }

    companion object {
        private const val CHARACTER_KEY = "CHARACTER_KEY"

        fun createInstance(character: Character): Fragment = CharacterDetailsFragment().also {
            it.arguments = Bundle().apply { putSerializable(CHARACTER_KEY, character) }
        }
    }
}