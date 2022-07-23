package dev.thiagosouto.marvelpoc.home.list

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import dev.thiagosouto.marvelpoc.R
import dev.thiagosouto.marvelpoc.data.model.view.Character
import dev.thiagosouto.marvelpoc.detail.CharacterDetailsActivity
import dev.thiagosouto.marvelpoc.home.FavoritesViewModel
import dev.thiagosouto.marvelpoc.shared.livedata.Result
import dev.thiagosouto.marvelpoc.widget.ErrorData
import dev.thiagosouto.marvelpoc.widget.ErrorView
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

internal abstract class BaseFragment : Fragment(R.layout.fragment_characters) {
    protected val favoritesViewModel: FavoritesViewModel by sharedViewModel()
    private val recycler: RecyclerView
        get() = requireView().findViewById(R.id.recycler)

    private val progress: ProgressBar
        get() = requireView().findViewById(R.id.progress)

    private val errorView: ErrorView
        get() = requireView().findViewById(R.id.errorView)


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val adapter = CharactersAdapter(
            favoritesViewModel::favoriteClick,
            ::startDetails
        )

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
