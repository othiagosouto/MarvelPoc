package com.soutosss.marvelpoc.home.list

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.soutosss.marvelpoc.data.model.view.CharacterHome
import com.soutosss.marvelpoc.shared.livedata.Result

class CharactersFragment : BaseFragment() {
    override fun paginatedContent(): LiveData<PagedList<CharacterHome>> =
        homeViewModel.charactersPageListContent()

    override fun observeNotCommonContent(adapter: CharactersAdapter) {
        arguments?.getString(QUERY_TEXT_KEY, null)?.let(homeViewModel::initSearchQuery)
        homeViewModel.changeAdapter.observe(this.viewLifecycleOwner, Observer {
            adapter.notifyItemChanged(it)
        })
    }

    override fun resultContent(): LiveData<Result> = homeViewModel.characters

    companion object {
        private const val QUERY_TEXT_KEY = "QUERY_TEXT_KEY"
        fun newInstance(queryText: String? = null): CharactersFragment {
            val fragment = CharactersFragment()
            fragment.arguments = Bundle().apply { putString(QUERY_TEXT_KEY, queryText) }
            return fragment
        }
    }

}
