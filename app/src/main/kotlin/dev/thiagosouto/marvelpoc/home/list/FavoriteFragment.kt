package dev.thiagosouto.marvelpoc.home.list

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import dev.thiagosouto.domain.model.Character
import dev.thiagosouto.marvelpoc.design.components.LoadingPage
import dev.thiagosouto.marvelpoc.detail.CharacterDetailsActivity
import dev.thiagosouto.marvelpoc.home.FavoritesViewModel
import dev.thiagosouto.marvelpoc.widget.CharacterItem
import dev.thiagosouto.marvelpoc.widget.ErrorScreen
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

internal class FavoriteFragment : Fragment() {

    private val favoritesViewModel: FavoritesViewModel by sharedViewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                val pager = remember { favoritesViewModel.createPager() }
                val lazyPagingItems = pager
                    .collectAsLazyPagingItems()

                val result = lazyPagingItems.loadState.refresh
                when {
                    result is LoadState.Loading -> LoadingPage(Modifier.testTag(CharactersListTestTags.LOADING))
                    result is LoadState.Error -> {
                        val (message, image) = favoritesViewModel.handleException()
                        ErrorScreen(
                            message = message,
                            image = image
                        )
                    }
                    lazyPagingItems.itemCount == 0 -> {
                        val (message, image) = favoritesViewModel.handleException()
                        ErrorScreen(
                            message = message,
                            image = image
                        )
                    }
                    else -> LazyVerticalGrid(
                        modifier = Modifier.testTag(CharactersListTestTags.LIST),
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(1.dp),
                        horizontalArrangement = Arrangement.spacedBy(1.dp)
                    ) {
                        items(lazyPagingItems.itemCount) { index ->
                            lazyPagingItems[index]?.let { character ->
                                CharacterItem(
                                    modifier = Modifier.testTag("character-$index"),
                                    character,
                                    ::startDetails
                                ) {
                                    val charItem = character.copy(favorite = it)
                                    charItem.let(favoritesViewModel::favoriteClick)
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
}
