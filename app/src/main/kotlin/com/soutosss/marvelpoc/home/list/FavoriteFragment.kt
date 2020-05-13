package com.soutosss.marvelpoc.home.list

import android.view.View
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.soutosss.marvelpoc.data.model.view.Character
import com.soutosss.marvelpoc.shared.livedata.Result
import kotlinx.android.synthetic.main.fragment_characters.*

class FavoriteFragment : BaseFragment() {
    override fun paginatedContent(): LiveData<PagedList<Character>> =
        homeViewModel.charactersFavorite()

    override fun observeNotCommonContent(adapter: CharactersAdapter) {
        progress.hide()
        recycler.visibility = View.VISIBLE
    }

    override fun resultContent(): LiveData<Result>  = homeViewModel.favoriteCharacters

}
