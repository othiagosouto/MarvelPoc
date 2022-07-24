package dev.thiagosouto.marvelpoc.home.list

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.paging.LoadState
import androidx.paging.PagedList
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.recyclerview.widget.RecyclerView
import dev.thiagosouto.marvelpoc.R
import dev.thiagosouto.marvelpoc.data.model.view.Character
import dev.thiagosouto.marvelpoc.design.components.LoadingPage
import dev.thiagosouto.marvelpoc.detail.CharacterDetailsActivity
import dev.thiagosouto.marvelpoc.home.FavoritesViewModel
import dev.thiagosouto.marvelpoc.shared.livedata.Result
import dev.thiagosouto.marvelpoc.widget.CharacterItem
import dev.thiagosouto.marvelpoc.widget.ErrorData
import dev.thiagosouto.marvelpoc.widget.ErrorScreen
import dev.thiagosouto.marvelpoc.widget.ErrorView
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

internal class FavoriteFragment : Fragment(R.layout.fragment_characters) {

    private val favoritesViewModel: FavoritesViewModel by sharedViewModel()
    private val recycler: RecyclerView
        get() = requireView().findViewById(R.id.recycler)

    private val progress: ProgressBar
        get() = requireView().findViewById(R.id.progress)
    private val errorView: ErrorView
        get() = requireView().findViewById(R.id.errorView)

    fun paginatedContent(): LiveData<PagedList<Character>> =
        favoritesViewModel.charactersFavorite()

    fun observeNotCommonContent(adapter: CharactersAdapter) {
        progress.visibility = View.GONE
        recycler.visibility = View.VISIBLE
    }

    fun resultContent(): LiveData<Result> = favoritesViewModel.favoriteCharacters

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val adapter = CharactersAdapter(
            favoritesViewModel::favoriteClick,
            ::startDetails
        )

        recycler.adapter = adapter
        observeLiveData(adapter)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                val pager = remember { charactersViewModel.createPager() }
                val lazyPagingItems = pager
                    .flow
                    .collectAsLazyPagingItems()

                when (val result = lazyPagingItems.loadState.refresh) {
                    is LoadState.Loading -> LoadingPage(Modifier.testTag("loading-characters"))
                    is LoadState.Error -> {
                        val (message, image) = charactersViewModel.handleException(result.error)
                        ErrorScreen(
                            message = message,
                            image = image
                        )
                    }
                    else -> LazyVerticalGrid(
                        modifier = Modifier.testTag("characters-list"),
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(1.dp),
                        horizontalArrangement = Arrangement.spacedBy(1.dp)
                    ) {
                        items(lazyPagingItems.itemCount) { index ->
                            lazyPagingItems[index]?.let { character ->
                                CharacterItem(
                                    modifier = Modifier,
                                    character.copy(
                                        favorite = charactersViewModel.favoritesIds.isCharacterFavorite(
                                            character.id
                                        )
                                    ),
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

    private fun startDetails(character: Character) {
        val intent = Intent(context, CharacterDetailsActivity::class.java)
        intent.putExtra(CharacterDetailsActivity.CHARACTER_KEY, character)
        startActivity(intent)
    }

    private fun observeLiveData(adapter: CharactersAdapter) {
        observeNotCommonContent(adapter)
        paginatedContent().observe(this.viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        resultContent().observe(this.viewLifecycleOwner, Observer {
            when (it) {
                is Result.Loaded -> {
                    recycler.visibility = View.VISIBLE
                    progress.visibility = View.GONE
                    errorView.bind(null)
                }
                is Result.Error -> {
                    recycler.visibility = View.GONE
                    progress.visibility = View.GONE
                    errorView.bind(ErrorData(it.errorMessage, it.drawableRes))
                }
            }
        })
    }

}
