package com.soutosss.marvelpoc.home.list

import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import com.soutosss.marvelpoc.R
import dev.thiagosouto.marvelpoc.data.model.view.Character
import dev.thiagosouto.marvelpoc.shared.livedata.Result

class FavoriteFragment : BaseFragment() {

    private val recycler: RecyclerView
        get() = requireView().findViewById(R.id.recycler)

    private val progress: ProgressBar
        get() = requireView().findViewById(R.id.progress)

    override fun paginatedContent(): LiveData<PagedList<Character>> =
        homeViewModel.charactersFavorite()

    override fun observeNotCommonContent(adapter: CharactersAdapter) {
        progress.visibility = View.GONE
        recycler.visibility = View.VISIBLE
    }

    override fun resultContent(): LiveData<Result>  = homeViewModel.favoriteCharacters

}
