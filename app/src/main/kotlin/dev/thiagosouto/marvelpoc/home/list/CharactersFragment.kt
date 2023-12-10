package dev.thiagosouto.marvelpoc.home.list

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dev.thiagosouto.marvelpoc.design.components.LoadingPage
import dev.thiagosouto.marvelpoc.detail.CharacterDetailsActivity
import dev.thiagosouto.marvelpoc.domain.model.Character
import dev.thiagosouto.marvelpoc.widget.CharacterItem
import dev.thiagosouto.marvelpoc.widget.ErrorScreen
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

internal class CharactersFragment : Fragment() {
    private val charactersViewModel: CharactersViewModel by viewModel()
    private lateinit var customView: ComposeView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        queryText()
        charactersViewModel.load()
        customView = ComposeView(requireContext())

        return customView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                charactersViewModel.state.collectLatest { state ->
                    customView.setContent {
                        when (state) {
                            is CharacterViewState.Loading -> {
                                LoadingPage(
                                    Modifier.testTag(
                                        CharactersListTestTags.LOADING
                                    )
                                )
                            }

                            is CharacterViewState.Error -> {
                                ErrorScreen(
                                    message = state.title,
                                    image = state.image
                                )
                            }

                            is CharacterViewState.Loaded -> {
                                LazyVerticalGrid(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .testTag(CharactersListTestTags.LIST),
                                    columns = GridCells.Fixed(2),
                                    verticalArrangement = Arrangement.spacedBy(1.dp),
                                    horizontalArrangement = Arrangement.spacedBy(1.dp)
                                ) {
                                    items(state.content.size) { index ->
                                        state.content[index].let { character ->
                                            CharacterItem(
                                                modifier = Modifier,
                                                character,
                                                ::startDetails
                                            ) {
                                                val charItem = character.copy(favorite = it)
                                                charItem.let(charactersViewModel::favoriteClick)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun startDetails(character: Character) {
        val intent = Intent(context, CharacterDetailsActivity::class.java)
        intent.putExtra(CharacterDetailsActivity.CHARACTER_KEY, character.id)
        startActivity(intent)
    }

    private fun queryText(): Unit {
        arguments?.getString(QUERY_TEXT_KEY, null)?.let(charactersViewModel::searchedQuery::set)
    }

    companion object {
        private const val QUERY_TEXT_KEY = "QUERY_TEXT_KEY"
        fun newInstance(queryText: String? = null): CharactersFragment {
            val fragment = CharactersFragment()
            fragment.arguments = Bundle().apply { putString(QUERY_TEXT_KEY, queryText) }
            return fragment
        }
    }
}
