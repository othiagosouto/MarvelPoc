package com.soutosss.marvelpoc.home.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.soutosss.marvelpoc.R
import com.soutosss.marvelpoc.data.model.view.CharacterHome
import com.soutosss.marvelpoc.home.HomeViewModel
import com.soutosss.marvelpoc.shared.livedata.Result
import kotlinx.android.synthetic.main.fragment_characters.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class CharactersFragment : Fragment(R.layout.fragment_characters) {
    private val homeViewModel: HomeViewModel by sharedViewModel()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recycler.layoutManager = GridLayoutManager(context, 2)
        val adapter = CharactersAdapter()
        recycler.adapter = adapter

        homeViewModel.characters.observe(this.viewLifecycleOwner, Observer {
            when (it) {
                is Result.Loaded -> adapter.submitList(it.item as List<CharacterHome>)
            }
        })
    }

    companion object {
        private const val KEY_FAVORITE_TAB = "KEY_FAVORITE_TAB"
        fun createHomeFragment(): Fragment = newInstance(false)
        fun createFavoriteFragment(): Fragment = newInstance(true)

        private fun newInstance(isFavoriteTab: Boolean): Fragment = CharactersFragment().apply {
            val arguments = Bundle()
            arguments.putBoolean(KEY_FAVORITE_TAB, isFavoriteTab)
            this.arguments = arguments
        }
    }
}
