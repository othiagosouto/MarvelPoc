package com.soutosss.marvelpoc.home.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.ContentLoadingProgressBar
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.soutosss.marvelpoc.R
import com.soutosss.marvelpoc.data.model.view.Character
import com.soutosss.marvelpoc.home.list.CharactersAdapter.CharacterHomeViewHolder

class CharactersAdapter(
    private val renderImage: (ImageView, String, ContentLoadingProgressBar) -> Unit,
    private val favoriteClick: (Character) -> Unit
) : PagedListAdapter<Character, CharacterHomeViewHolder>(CharacterDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterHomeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.characters_item, parent, false)
        return CharacterHomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: CharacterHomeViewHolder, position: Int) {
        getItem(position)?.let(holder::bind)
    }

    inner class CharacterHomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val headerText: TextView = itemView.findViewById(R.id.text)
        private val imageView: ImageView = itemView.findViewById(R.id.image)
        private val progress: ContentLoadingProgressBar = itemView.findViewById(R.id.progressItem)
        private val favoriteCheckBox: CheckBox = itemView.findViewById(R.id.favorite)

        init {
            favoriteCheckBox.setOnClickListener {
                val item = getItem(adapterPosition)
                item?.favorite = item?.favorite?.not() ?: false
                item?.let(favoriteClick)
            }
        }

        fun bind(character: Character) {
            headerText.text = character.name
            renderImage(imageView, character.thumbnailUrl, progress)
            favoriteCheckBox.isChecked = character.favorite
        }
    }

    class CharacterDiff : DiffUtil.ItemCallback<Character>() {
        override fun areItemsTheSame(oldItem: Character, newItem: Character) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Character, newItem: Character) =
            oldItem == newItem
    }

}