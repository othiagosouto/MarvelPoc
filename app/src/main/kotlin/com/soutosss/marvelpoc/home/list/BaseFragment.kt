package com.soutosss.marvelpoc.home.list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.bumptech.glide.Glide
import com.soutosss.marvelpoc.R
import com.soutosss.marvelpoc.data.model.view.Character
import com.soutosss.marvelpoc.home.HomeViewModel
import com.soutosss.marvelpoc.loadHomeImage
import com.soutosss.marvelpoc.shared.livedata.Result
import kotlinx.android.synthetic.main.fragment_characters.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

abstract class BaseFragment : Fragment(R.layout.fragment_characters) {
    protected val homeViewModel: HomeViewModel by sharedViewModel()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val adapter = CharactersAdapter(::loadHomeImage, homeViewModel::favoriteClick)

        recycler.adapter = adapter
        observeLiveData(adapter)
    }

    abstract fun paginatedContent(): LiveData<PagedList<Character>>
    abstract fun observeNotCommonContent(adapter: CharactersAdapter)
    abstract fun resultContent(): LiveData<Result>

    private fun observeLiveData(adapter: CharactersAdapter) {
        observeNotCommonContent(adapter)
        paginatedContent().observe(this.viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        resultContent().observe(this.viewLifecycleOwner, Observer {
            when (it) {
                is Result.Loaded -> {
                    recycler.visibility = View.VISIBLE
                    progress.hide()
                    group.visibility = View.GONE
                }
                is Result.Error -> {
                    recycler.visibility = View.GONE
                    progress.hide()
                    handleErrorState(it.errorMessage, it.drawableRes)
                }
            }
        })
    }

    private fun handleErrorState(messageResId: Int, drawableResId: Int) {
        group.visibility = View.VISIBLE
        message.text = getString(messageResId)
        Glide.with(erroIcon.context).load(drawableResId).into(erroIcon)
    }

}
