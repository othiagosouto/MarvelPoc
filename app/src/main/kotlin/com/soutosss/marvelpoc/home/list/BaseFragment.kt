package com.soutosss.marvelpoc.home.list

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.soutosss.marvelpoc.R
import com.soutosss.marvelpoc.data.model.view.Character
import com.soutosss.marvelpoc.detail.CharacterDetailsActivity
import com.soutosss.marvelpoc.home.HomeViewModel
import com.soutosss.marvelpoc.shared.livedata.Result
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

abstract class BaseFragment : Fragment(R.layout.fragment_characters) {
    protected val homeViewModel: HomeViewModel by sharedViewModel()
    private val recycler: RecyclerView
        get() = requireView().findViewById(R.id.recycler)

    private val progress: ProgressBar
        get() = requireView().findViewById(R.id.progress)

    private val group: Group
        get() = requireView().findViewById(R.id.group)

    private val message: TextView
        get() = requireView().findViewById(R.id.message)

    private val erroIcon: ImageView
        get() = requireView().findViewById(R.id.erroIcon)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val adapter = CharactersAdapter(homeViewModel::favoriteClick, ::startDetails, homeViewModel::isCharacterFavorite)

        recycler.adapter = adapter
        observeLiveData(adapter)
    }

    private fun startDetails(character: Character) {
        val intent = Intent(context, CharacterDetailsActivity::class.java)
        intent.putExtra(CharacterDetailsActivity.CHARACTER_KEY, character)
        startActivity(intent)
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
                    progress.visibility = View.GONE
                    group.visibility = View.GONE
                }
                is Result.Error -> {
                    recycler.visibility = View.GONE
                    progress.visibility = View.GONE
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
